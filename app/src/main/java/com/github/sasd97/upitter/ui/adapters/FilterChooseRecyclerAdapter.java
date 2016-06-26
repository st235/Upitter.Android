package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.sasd97.upitter.R;

import java.util.ArrayList;

/**
 * Created by Alex on 31.01.2016.
 */

public class FilterChooseRecyclerAdapter extends RecyclerView.Adapter<FilterChooseRecyclerAdapter.FilterChooseViewHolder> {

    public interface OnFilterChooseListener {
        void onChoose(int position);
    }

    private Context context;
    private ArrayList<String> filterList;
    private ArrayList<Integer> previewsList;
    private static OnFilterChooseListener onFilterChooseListener;

    public FilterChooseRecyclerAdapter(Context context, ArrayList<String> filterList, ArrayList<Integer> previewsList) {
        this.context = context;
        this.filterList = filterList;
        this.previewsList = previewsList;
    }

    public <T extends AppCompatActivity & OnFilterChooseListener> void setListener(T context) {
        onFilterChooseListener = context;
    }

    public void setListener(OnFilterChooseListener listener) {
        onFilterChooseListener = listener;
    }

    public static class FilterChooseViewHolder extends RecyclerView.ViewHolder
                                                implements View.OnClickListener {

        private ImageView filterPreview;
        private TextView filterTitle;

        public FilterChooseViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            filterPreview = (ImageView) itemView.findViewById(R.id.filter_preview);
            filterTitle = (TextView) itemView.findViewById(R.id.filter_title);
        }

        @Override
        public void onClick(View v) {
            if (onFilterChooseListener != null)
                onFilterChooseListener.onChoose(getAdapterPosition());
        }
    }

    @Override
    public FilterChooseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_image_single_view, parent, false);
        return new FilterChooseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FilterChooseViewHolder holder, final int position) {
        holder.filterTitle.setText(filterList.get(position));
        Glide.with(context).load(previewsList.get(position)).centerCrop().into(holder.filterPreview);
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }
}
