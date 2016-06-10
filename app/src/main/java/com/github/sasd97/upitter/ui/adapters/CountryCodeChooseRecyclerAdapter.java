package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.CountryModel;
import com.github.sasd97.upitter.utils.Palette;
import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LinearSLM;

import java.util.ArrayList;

/**
 * Created by Alex on 09.06.2016.
 */
public class CountryCodeChooseRecyclerAdapter
        extends RecyclerView.Adapter<CountryCodeChooseRecyclerAdapter.CountryCodeChooseViewHolder> {

    public interface OnItemClickListener {

        void onClick(CountryModel country);
    }

    private static final int VIEW_TYPE_HEADER = 0x01;
    private static final int VIEW_TYPE_CONTENT = 0x00;

    private Context context;

    private OnItemClickListener listener;
    private ArrayList<CountryModel> countryList;

    public CountryCodeChooseRecyclerAdapter(@NonNull OnItemClickListener listener,
                                            @NonNull ArrayList<CountryModel> countryList) {
        this.listener = listener;
        this.countryList = countryList;
    }

    public class CountryCodeChooseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnItemClickListener listener;

        private ImageView countryPreviewImageView;
        private TextView countryNativeTextView;
        private TextView countryOfficialTextView;
        private TextView countryPrefixTextView;

        public CountryCodeChooseViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            countryPreviewImageView = (ImageView) itemView.findViewById(R.id.country_preview_country_code_single_view);
            countryNativeTextView = (TextView) itemView.findViewById(R.id.country_native_country_code_single_view);
            countryOfficialTextView = (TextView) itemView.findViewById(R.id.country_official_country_code_single_view);
            countryPrefixTextView = (TextView) itemView.findViewById(R.id.code_country_code_single_view);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onClick(countryList.get(getAdapterPosition()));
        }
    }


    @Override
    public CountryCodeChooseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_code_single_view, parent, false);
        CountryCodeChooseViewHolder holder = new CountryCodeChooseViewHolder(v);
        holder.setOnItemClickListener(listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(CountryCodeChooseViewHolder holder, int position) {
        CountryModel country = countryList.get(position);
        int color = CountryModel.RegionType.getType(country.getRegion()).getColorResId();

        TextDrawable circleCode = TextDrawable
                .builder()
                .buildRound(country.getCode(), Palette.obtainColor(color));

        holder.countryNativeTextView.setText(country.getNativeName());
        holder.countryOfficialTextView.setText(country.getName());
        holder.countryPrefixTextView.setText(country.getDialCode());
        holder.countryPreviewImageView.setImageDrawable(circleCode);

        final View itemView = holder.itemView;

        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        lp.setSlm(LinearSLM.ID);
        lp.setFirstPosition(country.getSectionFirstPosition());
        itemView.setLayoutParams(lp);
    }

    @Override
    public int getItemViewType(int position) {
        return countryList.get(position).isHeader() ?
                VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }
}
