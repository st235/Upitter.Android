package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.sasd97.upitter.ui.base.BaseViewHolder;

/**
 * Created by alexander on 18.08.16.
 */
public class SocialIconsRecycler extends RecyclerView.Adapter<SocialIconsRecycler.SocialIconsViewHolder> {

    public class SocialIconsViewHolder extends BaseViewHolder {

        public SocialIconsViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {

        }
    }

    @Override
    public void onBindViewHolder(SocialIconsViewHolder holder, int position) {

    }

    @Override
    public SocialIconsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
