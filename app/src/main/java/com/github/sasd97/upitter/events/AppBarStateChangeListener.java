package com.github.sasd97.upitter.events;

import android.support.design.widget.AppBarLayout;

/**
 * Created by Alexadner Dadukin on 12/8/2016.
 */

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private double maxOffset = -1;
    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (maxOffset == -1) maxOffset = appBarLayout.getTotalScrollRange();

        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= maxOffset) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }

        onPercentage(Math.abs(i / maxOffset + 1));
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
    public void onPercentage(double percentage) {};
}