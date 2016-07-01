package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.sasd97.upitter.R;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by alexander on 28.06.16.
 */

public class AddressRecyclerAdapter extends RecyclerView.Adapter<AddressRecyclerAdapter.AddressViewHolder> {

    public class AddressViewHolder extends RecyclerView.ViewHolder {

        //private MaterialEditText

        public AddressViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.address_single_view, parent, false);
        return new AddressViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

