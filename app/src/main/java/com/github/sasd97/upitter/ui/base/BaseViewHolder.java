package com.github.sasd97.upitter.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Alexadner Dadukin on 02.08.2016.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        setupViews();
    }

    protected abstract void setupViews();
}
