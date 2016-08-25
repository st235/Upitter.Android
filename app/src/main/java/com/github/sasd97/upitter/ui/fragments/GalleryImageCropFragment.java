package com.github.sasd97.upitter.ui.fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.OnGalleryModificationlistener;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;


import java.io.File;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Alexander Dadukin on 10.05.2016.
 */

public class GalleryImageCropFragment extends BaseFragment {

    private File file;
    private boolean isInFreeMode = false;

    @BindView(R.id.image_cropper) CropImageView cropImageView;

    public GalleryImageCropFragment() {
        super(R.layout.fragment_gallery_image_crop);
    }

    public static GalleryImageCropFragment getFragment(@NonNull File file) {
        GalleryImageCropFragment fragment = new GalleryImageCropFragment();
        fragment.setFile(file);
        return fragment;
    }

    @Override
    protected void setupViews() {
        Glide
                .with(this)
                .load(file)
                .asBitmap()
                .fitCenter()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(cropImageView);
    }

    public void reloadFile() {
        Glide
                .with(this)
                .load(file)
                .asBitmap()
                .fitCenter()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(cropImageView);
    }

    private void setFile(File file) {
        this.file = file;
    }

    @OnClick(R.id.rotate_view_crop_fragment)
    public void onRotateClick(View v) {
        cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
    }

    @OnClick(R.id.free_mode_view_crop_fragment)
    public void onFreeModeClick(View v) {
        isInFreeMode = !isInFreeMode;
        CropImageView.CropMode cropMode = isInFreeMode ? CropImageView.CropMode.FREE : CropImageView.CropMode.SQUARE;
        cropImageView.setCropMode(cropMode);
    }

    public void save(@NonNull final OnGalleryModificationlistener listener) {
        cropImageView.startCrop(Uri.fromFile(file), new CropCallback() {
            @Override
            public void onSuccess(Bitmap cropped) {

            }

            @Override
            public void onError() {

            }
        }, new SaveCallback() {
            @Override
            public void onSuccess(Uri outputUri) {
                listener.onSave(outputUri.getPath());
            }

            @Override
            public void onError() {

            }
        });
    }
}
