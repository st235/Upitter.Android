package com.github.sasd97.upitter.ui.base;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        bindViews();
    }

    protected <T extends View> T findById(@IdRes int resourceId) {
        return (T) findViewById(resourceId);
    }

    protected abstract void bindViews();
}
