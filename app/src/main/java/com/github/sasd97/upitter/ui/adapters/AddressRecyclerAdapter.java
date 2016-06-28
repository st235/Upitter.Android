package com.github.sasd97.upitter.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by alexander on 28.06.16.
 */
public class AddressRecyclerAdapter extends RecyclerView.Adapter<AddressRecyclerAdapter.AddressViewHolder> {

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        public AddressViewHolder(View itemView) {
            super(itemView);

        }
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

