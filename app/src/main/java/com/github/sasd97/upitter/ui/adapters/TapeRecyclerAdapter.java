package com.github.sasd97.upitter.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by alexander on 08.07.16.
 */
public class TapeRecyclerAdapter extends RecyclerView.Adapter<TapeRecyclerAdapter.TapeViewHolder> {


    public class TapeViewHolder extends RecyclerView.ViewHolder {

        public TapeViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public TapeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TapeViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
