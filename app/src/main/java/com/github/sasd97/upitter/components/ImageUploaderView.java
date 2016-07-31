package com.github.sasd97.upitter.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.services.query.FileUploadQueryService;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Names;

import java.io.File;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Alexadner Dadukin on 30.06.2016.
 */

public class ImageUploaderView extends LinearLayout implements
        FileUploadQueryService.OnFileUploadListener {

    private static final String TAG = "Image Uploader View";

    public interface OnImageUploadListener {
        void onUpload(String path);
    }

    private Context context;
    private FileUploadQueryService service;
    private OnImageUploadListener listener;

    private View rootView;
    private ImageView imagePreviewImageView;
    private TextView helpTextView;
    private CircularProgressView circularProgressView;
    private ImageView successIndicatorImageView;
    private ImageView errorIndicatorImageView;

    public ImageUploaderView(Context context) {
        super(context);
        init(context);
    }

    public ImageUploaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageUploaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setClickable(true);
        this.context = context;
        this.service = FileUploadQueryService.getService(this);

        rootView = LayoutInflater.from(context).inflate(R.layout.image_uploader_single_view, this);

        imagePreviewImageView = (ImageView) rootView.findViewById(R.id.image_preview_image_uploader_single_view);
        helpTextView = (TextView) rootView.findViewById(R.id.text_view_image_uploader_single_view);
        circularProgressView = (CircularProgressView) rootView.findViewById(R.id.progress_view_image_uploader_single_view);
        successIndicatorImageView = (ImageView) rootView.findViewById(R.id.success_indicator_image_uploader_single_view);
        errorIndicatorImageView = (ImageView) rootView.findViewById(R.id.error_indicator_image_uploader_single_view);
    }

    public void setOnImageUploadListener(@NonNull OnImageUploadListener listener) {
        this.listener = listener;
    }

    public void uploadPhoto(@NonNull String photoPath) {
        Log.d(TAG, photoPath);
        hideIndicators();
        File imageFile = new File(photoPath);

        Glide
                .with(context)
                .load(imageFile)
                .bitmapTransform(new CenterCrop(getContext()), new RoundedCornersTransformation(getContext(), Dimens.drr(), Dimens.ABSOLUTE_ZERO))
                .into(imagePreviewImageView);

        helpTextView.setVisibility(GONE);
        circularProgressView.setVisibility(VISIBLE);
        Log.d(TAG, photoPath);
        service.uploadImage("1", photoPath, "image", "photo");
    }

    @Override
    public void onUpload(String path) {
        circularProgressView.setVisibility(GONE);
        successIndicatorImageView.setVisibility(VISIBLE);
        if (listener != null) listener.onUpload(path);
    }

    @Override
    public void onError(ErrorModel errorModel) {
        Log.d("PATH", errorModel.toString());
        circularProgressView.setVisibility(GONE);
        errorIndicatorImageView.setVisibility(VISIBLE);
    }

    private void hideIndicators() {
        successIndicatorImageView.setVisibility(GONE);
        errorIndicatorImageView.setVisibility(GONE);
    }
}
