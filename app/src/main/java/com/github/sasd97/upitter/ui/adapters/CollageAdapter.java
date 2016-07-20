package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.utils.CollageUtils;

import java.util.List;

/**
 * Created by alexander on 20.07.16.
 */

public class CollageAdapter extends RecyclerView.Adapter<CollageAdapter.CollageViewHolder> {

    private List<Bitmap> images;

    public CollageAdapter(List<Bitmap> images) {
        this.images = images;
    }

    public class CollageViewHolder extends RecyclerView.ViewHolder {

        private ImageView rootImage;

        public CollageViewHolder(View itemView) {
            super(itemView);

            rootImage = (ImageView) itemView;
        }
    }

    @Override
    public CollageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.collage_single_view, parent, false);
        return new CollageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CollageViewHolder holder, int position) {
        Bitmap bitmap = images.get(position);
        holder.rootImage.setImageBitmap(images.get(position));

        Log.d("IMAGE_SPEC", String.format("Aspect: %1$f, Type: %2$d",
                CollageUtils.calculateAspectRatio(bitmap.getHeight(), bitmap.getWidth()),
                CollageUtils.calculateImageType(bitmap.getHeight(), bitmap.getWidth())));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
