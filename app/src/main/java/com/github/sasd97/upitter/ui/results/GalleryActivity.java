package com.github.sasd97.upitter.ui.results;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.components.AutoFitRecyclerView;
import com.github.sasd97.upitter.events.OnGalleryInteractionListener;
import com.github.sasd97.upitter.events.OnSearchListener;
import com.github.sasd97.upitter.models.FolderModel;
import com.github.sasd97.upitter.models.skeletons.ImageSkeleton;
import com.github.sasd97.upitter.ui.adapters.GalleryRecyclerAdapter;
import com.github.sasd97.upitter.ui.adapters.GallerySpinnerAdapter;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.schemas.GalleryAlbumPreviewActivity;
import com.github.sasd97.upitter.utils.Gallery;
import com.github.sasd97.upitter.utils.Search;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.github.sasd97.upitter.utils.mutators.PhotoMutator;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.*;

public class GalleryActivity extends BaseActivity
        implements View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        OnSearchListener,
        OnGalleryInteractionListener {

    private final int DEFAULT_MULTI_SELECT_MAX_AMOUNT = 3;
    private final boolean IS_GALLERY_IN_MULTI_SELECTION_MODE = false;

    private int multiSelectMaxAmount;
    private boolean isMultiSelectionMode = false;

    private Spinner spinner;
    private GallerySpinnerAdapter gallerySpinnerAdapter;

    private FloatingActionButton applyFab;
    private AutoFitRecyclerView imageGridRecyclerView;
    private GalleryRecyclerAdapter galleryRecyclerAdapter;

    private RelativeLayout progressRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gallery_activity);
        setToolbar(R.id.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));

        initConfiguration(getIntent());
        changeModes(isMultiSelectionMode);

        spinner.setOnItemSelectedListener(this);

        galleryRecyclerAdapter = new GalleryRecyclerAdapter(isMultiSelectionMode, multiSelectMaxAmount,
                this, new ArrayList<ImageSkeleton>());

        imageGridRecyclerView.setHasFixedSize(true);
        galleryRecyclerAdapter.setOnImageChooserListener(this);
        imageGridRecyclerView.setAdapter(galleryRecyclerAdapter);

        applyFab.setOnClickListener(this);

        Search.search(this);
    }

    @Override
    protected void bindViews() {
        progressRelativeLayout = findById(R.id.images_loading_view_gallery_activity);
        imageGridRecyclerView = findById(R.id.images_grid_gallery_activity);
        spinner = findById(R.id.spinner_nav_gallery_activity);
        applyFab = findById(R.id.fab_gallery_activity);
    }

    private void initConfiguration(Intent config) {
        isMultiSelectionMode = config.getBooleanExtra(GALLERY_MULTI_SELECTION_MODE, IS_GALLERY_IN_MULTI_SELECTION_MODE);
        multiSelectMaxAmount = config.getIntExtra(GALLERY_MULTI_SELECT_ITEMS_AMOUNT, DEFAULT_MULTI_SELECT_MAX_AMOUNT);
    }

    private void changeModes(boolean isMultiSelect) {
        if (!isMultiSelect) {
            applyFab.hide();
            return;
        }

        imageGridRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    if (applyFab.isShown()) {
                        applyFab.hide();
                    }
                } else if (dy < 0) {
                    if (!applyFab.isShown()) {
                        applyFab.show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        ArrayList<String> finalPaths = new ArrayList<>();
        for (ImageSkeleton photo: galleryRecyclerAdapter.getFilterImageList())
            if (photo.isChecked()) finalPaths.add(photo.getPath());

        if (finalPaths.size() == 0) return;

        Intent data = new Intent();
        data.putStringArrayListExtra(GALLERY_MULTI_SELECTED_PHOTOS_LIST, finalPaths);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onSearched(ArrayList<String> paths) {
        spinner.setAdapter(gallerySpinnerAdapter);
        spinner.setVisibility(View.VISIBLE);

        galleryRecyclerAdapter.addAll(PhotoMutator.mutate(paths));
        imageGridRecyclerView.setVisibility(View.VISIBLE);
        progressRelativeLayout.setVisibility(View.GONE);
    }

    @Override
    public void onFoldersParsed(ArrayList<FolderModel> folders) {
        folders.add(FIRST_POSITION, Gallery.createAllFolder(this, folders));
        gallerySpinnerAdapter = new GallerySpinnerAdapter(this, R.layout.gallery_spinner_single_view, folders);
    }

    @Override
    public Uri getMediaType() {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return new String[]{MediaStore.Images.ImageColumns.DATA};
    }

    @Override
    public boolean onCompare(File file) {
        String fileName = file.getName();

        return  file.length() != 0 &&
                (fileName.contains(".png") || fileName.contains(".PNG")
                || fileName.contains(".jpg") || fileName.contains(".JPG")
                || fileName.contains(".bmp") || fileName.contains(".BMP"));
    }

    @Override
    public void onSearchError() {
        // TODO Search error ???
        Log.d("ERROR", "Error happenes");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       galleryRecyclerAdapter.getFilter().filter(gallerySpinnerAdapter.getAlbum(position).getPath());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onThumbnailClick(int position, ImageSkeleton path) {
        Intent intent = new Intent(this, GalleryAlbumPreviewActivity.class);
        intent.putExtra(PATH_ATTACH, path.getPath());
        intent.putExtra(POSITION_ATTACH, position);
        intent.putStringArrayListExtra(LIST_ATTACH, galleryRecyclerAdapter.getFilterPathList());

        if (!isMultiSelectionMode) {
            startActivityForResult(intent, 123);
            return;
        }

        startActivity(intent);
    }

    @Override
    public void onMultiSelectionCounterClick(int position, ArrayList<ImageSkeleton> selectedPhotos) {
    }

    @Override
    public void onMultiSelectionLimitExceeded() {
        Animation shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
        if (!applyFab.isShown()) applyFab.show();
        applyFab.startAnimation(shakeAnimation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        Intent intent = new Intent();
        intent.putExtra(PUT_CROPPED_IMAGE, data.getStringExtra(PUT_CROPPED_IMAGE));
        setResult(RESULT_OK, intent);
        finish();
    }
}
