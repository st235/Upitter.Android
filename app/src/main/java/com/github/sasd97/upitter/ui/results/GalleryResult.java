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
import com.github.sasd97.upitter.ui.adapters.recyclers.GalleryImageGridRecycler;
import com.github.sasd97.upitter.ui.adapters.spinner.GalleryAlbumPreviewSpinner;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.schemas.AlbumPreviewGallerySchema;
import com.github.sasd97.upitter.utils.Gallery;
import com.github.sasd97.upitter.utils.Search;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.github.sasd97.upitter.utils.mutators.PhotoMutator;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.*;

public class GalleryResult extends BaseActivity
        implements View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        OnSearchListener,
        OnGalleryInteractionListener {

    private static final String TAG = "Gallery Activity";

    private final static int DEFAULT_MULTI_SELECT_MAX_AMOUNT = 3;
    private final static boolean IS_GALLERY_IN_MULTI_SELECTION_MODE = false;

    private int multiSelectMaxAmount;
    private boolean isMultiSelectionMode = false;

    private GalleryAlbumPreviewSpinner galleryAlbumPreviewSpinner;
    private GalleryImageGridRecycler galleryImageGridRecycler;

    @BindView(R.id.spinner_nav_gallery_activity) Spinner spinner;
    @BindView(R.id.fab_gallery_activity) FloatingActionButton applyFab;
    @BindView(R.id.images_grid_gallery_activity) AutoFitRecyclerView imageGridRecyclerView;
    @BindView(R.id.images_loading_view_gallery_activity) RelativeLayout progressRelativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar, true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));

        initConfiguration(getIntent());
        changeModes(isMultiSelectionMode);

        spinner.setOnItemSelectedListener(this);

        galleryImageGridRecycler = new GalleryImageGridRecycler(isMultiSelectionMode, multiSelectMaxAmount,
                this, new ArrayList<ImageSkeleton>());

        imageGridRecyclerView.setHasFixedSize(true);
        galleryImageGridRecycler.setOnImageChooserListener(this);
        imageGridRecyclerView.setAdapter(galleryImageGridRecycler);

        applyFab.setOnClickListener(this);

        Search.search(this);
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
        for (ImageSkeleton photo: galleryImageGridRecycler.getFilterImageList())
            if (photo.isChecked()) finalPaths.add(photo.getPath());

        if (finalPaths.size() == 0) return;

        Intent data = new Intent();
        data.putStringArrayListExtra(GALLERY_MULTI_SELECTED_PHOTOS_LIST, finalPaths);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onSearched(ArrayList<String> paths) {
        spinner.setAdapter(galleryAlbumPreviewSpinner);
        spinner.setVisibility(View.VISIBLE);

        galleryImageGridRecycler.addAll(PhotoMutator.mutate(paths));
        imageGridRecyclerView.setVisibility(View.VISIBLE);
        progressRelativeLayout.setVisibility(View.GONE);
    }

    @Override
    public void onFoldersParsed(ArrayList<FolderModel> folders) {
        folders.add(FIRST_POSITION, Gallery.createAllFolder(this, folders));
        galleryAlbumPreviewSpinner = new GalleryAlbumPreviewSpinner(this, R.layout.row_gallery_album_preview, folders);
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
        Log.d(TAG, "Error happenes");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       galleryImageGridRecycler.getFilter().filter(galleryAlbumPreviewSpinner.getAlbum(position).getPath());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onThumbnailClick(int position, ImageSkeleton path) {
        Log.d(TAG, path.getPath());

        Intent intent = new Intent(this, AlbumPreviewGallerySchema.class);
        intent.putExtra(PATH_ATTACH, path.getPath());
        intent.putExtra(POSITION_ATTACH, position);
        intent.putStringArrayListExtra(LIST_ATTACH, galleryImageGridRecycler.getFilterPathList());

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
