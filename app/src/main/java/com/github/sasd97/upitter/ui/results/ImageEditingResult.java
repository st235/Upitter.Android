package com.github.sasd97.upitter.ui.results;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.view.Menu;
import android.view.MenuItem;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.OnApplyLongListener;
import com.github.sasd97.upitter.events.OnEditImageChooseListener;
import com.github.sasd97.upitter.events.OnSaveListener;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.FileUtils;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.orhanobut.logger.Logger;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import java.io.File;
import java.util.ArrayList;

public class ImageEditingResult extends BaseActivity
        implements FileUtils.OnInteractionListener {

    private static final String PUT_IMAGE_PATH = "IMAGE_PATH";
    private static final String PUT_CROPPED_IMAGE = "IMAGE_CROPPED";

    private String originalPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_editing);

        setToolbar(R.id.toolbar, true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.VERTICAL, 0.1f));

        originalPath = getIntent().getStringExtra("PATH_ATTACH");

        File photosPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File upitterFolder = new File(photosPath, "/Upitter");

        if (!upitterFolder.exists()) upitterFolder.mkdir();

        FileUtils
                .getUtil(this)
                .copyFile(new File(originalPath), new File(upitterFolder, "213"));
    }

    @Override
    protected void setupViews() {

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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCopy(File file) {
        Logger.d(file.toString());
    }

    @Override
    public void onError() {
        Logger.e("Error");
    }
}
