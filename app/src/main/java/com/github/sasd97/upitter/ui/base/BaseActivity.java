package com.github.sasd97.upitter.ui.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import butterknife.ButterKnife;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private View rootView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        bindViews();
    }

    protected <T extends View> T findById(@IdRes int resourceId) {
        return (T) findViewById(resourceId);
    }

    protected abstract void bindViews();

    protected void setToolbar(@IdRes int toolbarId) {
        toolbar = findById(toolbarId);
        setSupportActionBar(toolbar);
    }

    protected void setToolbar(@IdRes int toolbarId, boolean homeEnabled) {
        toolbar = findById(toolbarId);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(homeEnabled);
    }

    protected View getRootView() {
        if (rootView == null) rootView = findViewById(android.R.id.content);
        return rootView;
    }

    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return false;
        }
    }
}
