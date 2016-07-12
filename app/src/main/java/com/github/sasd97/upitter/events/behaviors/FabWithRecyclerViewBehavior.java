package com.github.sasd97.upitter.events.behaviors;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;

/**
 * Created by alexander on 11.07.16.
 */
public abstract class FabWithRecyclerViewBehavior extends RecyclerView.OnScrollListener {

    private FloatingActionButton fab;

    public FabWithRecyclerViewBehavior(FloatingActionButton fab) {
        this.fab = fab;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy > 0) fab.hide();
        else if (dy < 0) fab.show();
    }
}
