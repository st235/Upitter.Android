package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.categories.CategoryResponseModel;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.Palette;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 27.06.16.
 */

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {

    public interface OnItemClickListener {
        void onClick(CategoryResponseModel category, int position);
    }

    private Context context;
    private OnItemClickListener listener;
    private List<CategoryResponseModel> categories;

    public CategoryRecyclerAdapter(Context context, List<CategoryResponseModel> categories) {
        this.context = context;
        this.categories = categories;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView categoryPreviewImageView;
        private TextView categoryTitleTextView;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            categoryTitleTextView = (TextView) itemView.findViewById(R.id.category_title_category_single_view);
            categoryPreviewImageView = (ImageView) itemView.findViewById(R.id.category_preview_category_single_view);
        }

        @Override
        public void onClick(View view) {
            if (listener != null)
                listener.onClick(categories.get(getAdapterPosition()), getAdapterPosition());
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
        final String preview = getCategoryPreview(category.getTitle());

        TextDrawable circlePreview = TextDrawable
                .builder()
                .beginConfig()
                    .textColor(R.color.colorPrimary)
                .endConfig()
                .buildRound(preview, Palette.obtainColor(R.color.colorLightBabyBlue));

        if (category.getSelectedSubcategories() == null
                || category.getSelectedSubcategories().length == 0
                || category.getSelectedSubcategoriesIds() == null
                || category.getSelectedSubcategoriesIds().length == 0)
            holder.categoryTitleTextView.setTextColor(Palette.obtainColor(R.color.colorBlack));
        else
            holder.categoryTitleTextView.setTextColor(Palette.obtainColor(R.color.colorAccent));

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

    public ArrayList<Integer> getSelected() {
        ArrayList<Integer> result = new ArrayList<>();

        for (CategoryResponseModel category: categories) {
            if (category.getSelectedSubcategoriesIds() != null)
                for (Integer id: category.getSelectedSubcategoriesIds())
                    result.add(id);
        }

        return result;
    }

    public void each(@NonNull ListUtils.OnIteratorListener<CategoryResponseModel> listener) {
        final List<CategoryResponseModel> parents = ListUtils.filter(categories, new ListUtils.OnListInteractionListener<CategoryResponseModel>() {
            @Override
            public boolean isFit(CategoryResponseModel other) {
                return other.isParent();
            }
        });

        ListUtils.each(parents, listener);
    }

    private String getCategoryPreview(@NonNull String title) {
        return title.substring(0, 1);
    }
}
