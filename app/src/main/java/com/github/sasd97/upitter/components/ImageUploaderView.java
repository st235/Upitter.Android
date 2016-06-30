package com.github.sasd97.upitter.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
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
import com.github.sasd97.upitter.services.query.FileUploadQueryService;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Names;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Alexadner Dadukin on 30.06.2016.
 */
public class ImageUploaderView extends LinearLayout implements FileUploadQueryService.OnFileUploadListener {

    public interface OnImageUploadListener {
        void onUpload(String path);
    }

    private Context context;
    private FileUploadQueryService service;

    private View rootView;
    private ImageView imagePreviewImageView;
    private TextView helpTextView;
    private CircularProgressView circularProgressView;

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

    }

    public void uploadPhoto(@NonNull String photoPath, @NonNull OnImageUploadListener listener) {
        Glide
                .with(context)
                .load(Names.getInstance().getFilePath(photoPath).toString())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .bitmapTransform(new CenterCrop(getContext()), new RoundedCornersTransformation(getContext(), Dimens.dpToPx(4), 0))
                .into(imagePreviewImageView);

        helpTextView.setVisibility(GONE);
        circularProgressView.setVisibility(VISIBLE);
    }

    @Override
    public void onUpload() {
        circularProgressView.setVisibility(GONE);
    }

    @Override
    public void onError() {

    }
}
