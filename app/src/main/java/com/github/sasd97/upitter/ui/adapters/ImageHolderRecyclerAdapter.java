package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.skeletons.ImageSkeleton;
import com.github.sasd97.upitter.utils.Names;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 05.07.16.
 */
public class ImageHolderRecyclerAdapter extends RecyclerView.Adapter<ImageHolderRecyclerAdapter.ImageViewHolder> {

    public interface OnAmountChangeListener {
        void onEmpty();
    }

    private Context context;
    private ArrayList<String> paths;
    private OnAmountChangeListener listener;

    public ImageHolderRecyclerAdapter(Context context, ArrayList<String> paths, OnAmountChangeListener listener) {
        this.context = context;
        this.paths = paths;
        this.listener = listener;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imagePreview = null;
        private ImageButton deletePreview = null;

        public ImageViewHolder(View itemView) {
            super(itemView);

            imagePreview = (ImageView) itemView.findViewById(R.id.image_preview);
            deletePreview = (ImageButton) itemView.findViewById(R.id.delete_publication);
            deletePreview.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            removeAt(getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_placeholder_single_view, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Glide
                .with(context)
                .load(Names.getInstance()
                        .getFilePath(paths.get(position))
                        .toString())
                .centerCrop()
                .into(holder.imagePreview);
    }

    public void addAll(List<String> skeletonList) {
        paths.addAll(skeletonList);
        notifyItemInserted(paths.size());
    }

    public void removeAt(int position) {
        paths.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, paths.size());
        if (listener != null && paths.size() == 0) listener.onEmpty();
    }
}