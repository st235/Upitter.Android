package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.categories.CategoryResponseModel;

import java.util.List;
import java.util.Locale;

/**
 * Created by alexander on 27.06.16.
 */

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {

    private List<CategoryResponseModel> categories;

    public CategoryRecyclerAdapter(List<CategoryResponseModel> categories) {
        this.categories = categories;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView categoryPreviewImageView;
        private TextView categoryTitleTextView;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            categoryTitleTextView = (TextView) itemView.findViewById(R.id.category_title_category_single_view);
            categoryPreviewImageView = (ImageView) itemView.findViewById(R.id.category_preview_category_single_view);
        }
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.category_single_view, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        CategoryResponseModel category = categories.get(position);

        TextDrawable circlePreview = TextDrawable
                .builder()
                .buildRound(category.getTitle().substring(0, 1), Color.BLUE);

        holder.categoryTitleTextView.setText(category.getTitle());
        holder.categoryPreviewImageView.setImageDrawable(circlePreview);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void addAll(List<CategoryResponseModel> list) {
        categories.addAll(list);
        notifyItemInserted(categories.size());
    }
}
