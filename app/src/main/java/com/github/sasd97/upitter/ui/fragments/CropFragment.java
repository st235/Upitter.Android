package com.github.sasd97.upitter.ui.fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.OnApplyLongListener;
import com.github.sasd97.upitter.events.OnEditImageChooseListener;
import com.github.sasd97.upitter.events.OnSaveListener;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.utils.Names;
import com.github.sasd97.upitter.utils.SaveUtils;
import com.isseiaoki.simplecropview.CropImageView;


import java.util.ArrayList;

/**
 * Created by Alexander Dadukin on 10.05.2016.
 */
public class CropFragment extends BaseFragment implements OnEditImageChooseListener {

    private static final String IMAGE_PATH = "IMAGE_PATH";

    private boolean isInFreeMode = false;
    private LinearLayout rotateLinearLayout = null;
    private LinearLayout freeModeLinearLayout = null;

    public static CropFragment getInstance(String path) {
        CropFragment fragment = new CropFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_PATH, path);
        fragment.setArguments(args);
        return fragment;
    }

    CropImageView cropImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.crop_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String path = getArguments().getString(IMAGE_PATH);

        cropImageView = (CropImageView) findViewById(R.id.image_cropper);
        rotateLinearLayout = (LinearLayout) findViewById(R.id.rotate_view_crop_fragment);
        freeModeLinearLayout = (LinearLayout) findViewById(R.id.free_mode_view_crop_fragment);

        Glide
                .with(this)
                .load(Names.getInstance().getFilePath(path).toString())
                .asBitmap().fitCenter().into(new BitmapImageViewTarget(cropImageView) {
            @Override
            protected void setResource(Bitmap resource) {
                super.setResource(resource);
                cropImageView.setImageBitmap(resource);
            }
        });

        rotateLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
            }
        });

        freeModeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInFreeMode = !isInFreeMode;
                CropImageView.CropMode cropMode = isInFreeMode ? CropImageView.CropMode.FREE : CropImageView.CropMode.SQUARE;
                cropImageView.setCropMode(cropMode);
            }
        });
    }

    @Override
    protected void bindViews() {

    }

    @Override
    public void save(final OnApplyLongListener listener) {
        SaveUtils.save(new OnSaveListener() {
            @Override
            public void onSave(ArrayList<String> paths) {
                listener.onApplied(paths.get(0));
            }

            @Override
            public void onSaveError() {

            }
        }, cropImageView.getCroppedBitmap());
    }
}
