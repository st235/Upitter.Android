package com.github.sasd97.upitter.ui.results;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.Menu;
import android.view.MenuItem;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.OnApplyLongListener;
import com.github.sasd97.upitter.events.OnEditImageChooseListener;
import com.github.sasd97.upitter.events.OnSaveListener;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.ArrayList;

public class ImageEditingResult extends BaseActivity
        implements OnSaveListener, OnEditImageChooseListener {

    private static final String PUT_IMAGE_PATH = "IMAGE_PATH";
    private static final String PUT_CROPPED_IMAGE = "IMAGE_CROPPED";

    private String originalPath;
    private OnEditImageChooseListener editImageChooseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_editing);

        setToolbar(R.id.toolbar, true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.VERTICAL, 0.1f));

        originalPath = getIntent().getStringExtra("PATH_ATTACH");
        editImageChooseListener = this;
    }

    @Override
    protected void setupViews() {

    }

    @Override
    public void onSave(ArrayList<String> paths) {
        Intent result = new Intent();
        result.putExtra(PUT_CROPPED_IMAGE, paths.get(0));
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void onSaveError() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_cropper, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            case R.id.action_save:
                editImageChooseListener.save(new OnApplyLongListener() {
                    @Override
                    public void onApplied(String path) {
                        Intent intent = new Intent();
                        intent.putExtra(PUT_CROPPED_IMAGE, path);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void save(OnApplyLongListener listener) {
        listener.onApplied(originalPath);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onDestroy() {
        editImageChooseListener = this;
        super.onDestroy();
    }
}
