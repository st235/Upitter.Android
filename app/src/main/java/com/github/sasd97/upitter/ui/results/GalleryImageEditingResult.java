package com.github.sasd97.upitter.ui.results;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.OnGalleryModificationlistener;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.fragments.GalleryImageCropFragment;
import com.github.sasd97.upitter.ui.fragments.GalleryImageFilterFragment;
import com.github.sasd97.upitter.utils.FileUtils;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.orhanobut.logger.Logger;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import java.io.File;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.PATH_ATTACH;
import static com.github.sasd97.upitter.constants.GalleryConstants.UPITTER_FOLDER;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.PUT_CROPPED_IMAGE;
import static com.github.sasd97.upitter.utils.Gallery.getCopyFile;

public class GalleryImageEditingResult extends BaseActivity
        implements FileUtils.OnInteractionListener,
        OnGalleryModificationlistener {

    private final int FILTER_MODE = 0;
    private final int CROP_MODE = 1;

    private int currentMode = FILTER_MODE;

    private File copiedFile;
    private FileUtils fileUtils = FileUtils.getUtil(this);

    private GalleryImageFilterFragment imageFilterFragment;
    private GalleryImageCropFragment imageCropFragment;

    final RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
            Logger.i("clicked radio %1$d", i);

            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
                view.setChecked(view.getId() == i);
                view.setClickable(view.getId() != i);
            }
        }
    };

    final OnGalleryModificationlistener ApplyListener = new OnGalleryModificationlistener() {
        @Override
        public void onSave(String path) {
            Logger.i("SAVED!!!");

            switch (currentMode) {
                case FILTER_MODE:
                    imageFilterFragment.reloadFile();
                    break;
                case CROP_MODE:
                    imageCropFragment.reloadFile();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_editing);
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar, true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.VERTICAL, 0.1f));
        String originalPath = getIntent().getStringExtra(PATH_ATTACH);

        File photosFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File upitterFolder = new File(photosFolder, UPITTER_FOLDER);
        File originalFile = new File(originalPath);

        ((RadioGroup) findViewById(R.id.toggleGroup)).setOnCheckedChangeListener(ToggleListener);

        if (!upitterFolder.exists()) upitterFolder.mkdir();

        fileUtils
                .copyFile(originalFile, getCopyFile(upitterFolder, originalFile));
    }

    private void navigate(BaseFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
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
            case R.id.action_apply:
                save(ApplyListener);
                return true;
            case R.id.action_save:
                save(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onToggle(View view) {
        ((RadioGroup)view.getParent()).check(view.getId());

        Logger.i("clicked toggle");

        switch (view.getId()) {
            case R.id.filter_toggle:
                currentMode = FILTER_MODE;
                navigate(imageFilterFragment);
                break;
            case R.id.crop_toggle:
                currentMode = CROP_MODE;
                navigate(imageCropFragment);
                break;
        }
    }

    private void save(@NonNull OnGalleryModificationlistener listener) {
        switch (currentMode) {
            case FILTER_MODE:
                imageFilterFragment.save(listener);
                break;
            case CROP_MODE:
                imageCropFragment.save(listener);
                break;
        }
    }

    @Override
    public void onSave(String path) {
        Logger.i(path);
        Intent intent = new Intent();
        intent.putExtra(PUT_CROPPED_IMAGE, copiedFile.getAbsolutePath());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCopy(File file) {
        Logger.d(file.toString());
        copiedFile = file;

        imageCropFragment = GalleryImageCropFragment.getFragment(copiedFile);
        imageFilterFragment = GalleryImageFilterFragment.getFragment(copiedFile);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, imageFilterFragment)
                .commit();
    }

    @Override
    public void onError() {
        Logger.e("Error");
    }
}
