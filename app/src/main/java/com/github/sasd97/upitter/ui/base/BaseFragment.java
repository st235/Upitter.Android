package com.github.sasd97.upitter.ui.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */
public abstract class BaseFragment extends Fragment {

    private int layoutId;
    private View rootView;

    public BaseFragment(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(layoutId, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
        setupViews();
    }

    protected View findViewById(@IdRes int resourceId) {
        return rootView.findViewById(resourceId);
    }

    protected <T extends View> T findById(@IdRes int resourceId) {
        return (T) rootView.findViewById(resourceId);
    }

    protected abstract void setupViews();
}
