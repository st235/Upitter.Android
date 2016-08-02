package com.github.sasd97.upitter.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Alexadner Dadukin on 02.08.2016.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    protected abstract void setupViews();
}
