package com.github.sasd97.upitter.ui.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */
public abstract class BaseFragment extends Fragment {

    private View rootView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
        bindViews();
    }

    protected View findViewById(@IdRes int resourceId) {
        return rootView.findViewById(resourceId);
    }

    protected <T extends View> T findById(@IdRes int resourceId) {
        return (T) rootView.findViewById(resourceId);
    }

    protected abstract void bindViews();
}
