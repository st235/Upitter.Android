package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.skeletons.SearchableSkeleton;
import com.github.sasd97.upitter.utils.Dimens;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 05.07.16.
 */
public class SearchableRecyclerAdapter<T extends SearchableSkeleton>
        extends RecyclerView.Adapter<SearchableRecyclerAdapter<T>.LocationSearchViewHolder> {

    public interface OnItemSelectedListener<T extends SearchableSkeleton> {
        void onItemSelected(boolean isChecked, T location, int position);
    }

    private Context context;
    private boolean isMultiChoose;
    private boolean isSubtitles;
    private List<T> locationList;
    private OnItemSelectedListener onItemSelectedListener;

    public SearchableRecyclerAdapter(boolean isMultiChoose,
                                     boolean isSubtitles,
                                     Context context,
                                     List<T> locationList) {
        this.locationList = locationList;
        this.context = context;
        this.isMultiChoose = isMultiChoose;
        this.isSubtitles = isSubtitles;
    }

    public SearchableRecyclerAdapter(boolean isMultiChoose,
                                     boolean isSubtitles,
                                     Context context,
                                     ArrayList<T> locationList,
                                     OnItemSelectedListener listener) {
        this.locationList = locationList;
        this.context = context;
        this.isMultiChoose = isMultiChoose;
        this.isSubtitles = isSubtitles;
        onItemSelectedListener = listener;
    }

    public class LocationSearchViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView locationIcon;
        TextView locationText;
        TextView locationSubText;
        CheckBox locationChecker;

        private OnItemSelectedListener onItemSelectedListener;

        public LocationSearchViewHolder(View itemView) {
            super(itemView);

            locationIcon = (ImageView) itemView.findViewById(R.id.location_chooser_icon);
            locationText = (TextView) itemView.findViewById(R.id.location_chooser_text);
            locationSubText = (TextView) itemView.findViewById(R.id.location_chooser_subtext);
            locationChecker = (CheckBox) itemView.findViewById(R.id.location_chooser_checker);

            itemView.setOnClickListener(this);

            if (!isSubtitles) locationSubText.setVisibility(View.GONE);

            if (!isMultiChoose) {
                locationChecker.setVisibility(View.GONE);
                return;
            }

            locationChecker.setOnClickListener(this);
        }

        public void setOnItemSelectedListener(OnItemSelectedListener listener) {
            onItemSelectedListener = listener;
        }

        @Override
        public void onClick(View v) {
            if (!(v instanceof CheckBox)) locationChecker.setChecked(!locationChecker.isChecked());

            if (onItemSelectedListener != null)
                this.onItemSelectedListener.onItemSelected(locationChecker.isChecked(),
                        locationList.get(getAdapterPosition()),
                        getAdapterPosition());
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        onItemSelectedListener = listener;
    }

    @Override
    public LocationSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_search_single_view, parent, false);
        LocationSearchViewHolder holder = new LocationSearchViewHolder(v);
        if (onItemSelectedListener != null) holder.setOnItemSelectedListener(onItemSelectedListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(LocationSearchViewHolder holder, final int position) {
        SearchableSkeleton skeleton = locationList.get(position);
        final String title = skeleton.getTitle();
        final String preview = skeleton.getPreview();

        if (!locationList.get(position).isImage()) {
            TextDrawable drawable = TextDrawable
                    .builder()
                    .beginConfig()
                    .width(Dimens.dpToPx(50))
                    .height(Dimens.dpToPx(50))
                    .endConfig()
                    .buildRound(preview, Color.CYAN);
            holder.locationIcon.setImageDrawable(drawable);
        } else {
            RequestManager glide = Glide.with(context);
            SearchableSkeleton.IMAGE_TYPE type = skeleton.getImageType();

            switch (type) {
                case STRING:
                    glide.load(skeleton.getStringImage()).centerCrop().into(holder.locationIcon);
                    break;
                case INT:
                    glide.load(skeleton.getIntImage()).centerCrop().into(holder.locationIcon);
                    break;
            }
        }

        if (isSubtitles) holder.locationSubText.setText(locationList.get(position).getSubTitle());
        if (isMultiChoose) holder.locationChecker.setChecked(locationList.get(position).isChecked());
        holder.locationText.setText(title);
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    @Override
    public long getItemId(int position) {
        return locationList.get(position).hashCode();
    }

    public void updateDataSet(List<T> list) {
        locationList = list;
        notifyDataSetChanged();
    }
}
