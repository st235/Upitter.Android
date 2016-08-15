package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.pointers.ImagePointerModel;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;

import java.util.List;

/**
 * Created by alexander on 20.07.16.
 */

public class ImageCollageRecycler extends RecyclerView.Adapter<ImageCollageRecycler.CollageViewHolder> {

    private Context context;
    private List<ImagePointerModel> images;

    private OnImageClickListener listener;

    public interface OnImageClickListener {
        void onImageClick(int position);
    }

    public ImageCollageRecycler(Context context, List<ImagePointerModel> images) {
        this.context = context;
        this.images = images;
    }

    public void setOnItemClickListener(OnImageClickListener listener) {
        this.listener = listener;
    }

    public class CollageViewHolder extends BaseViewHolder
            implements View.OnClickListener {

        private ImageView rootImage;

        public CollageViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {
            rootImage = (ImageView) itemView;
            rootImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null)
                listener.onImageClick(getAdapterPosition());
        }
    }

    @Override
    public CollageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.row_image_collage, parent, false);
        return new CollageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CollageViewHolder holder, int position) {
        ImagePointerModel media = images.get(position);

        Glide
                .with(context)
                .load(media.getThumbUrl())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.rootImage);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
