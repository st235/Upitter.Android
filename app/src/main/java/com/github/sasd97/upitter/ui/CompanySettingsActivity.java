package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.constants.RequestCodesConstants;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.services.query.FileUploadQueryService;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Gallery;
import com.github.sasd97.upitter.utils.Keyboard;
import com.github.sasd97.upitter.utils.Names;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;
import com.rengwuxian.materialedittext.MaterialEditText;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.PUT_CROPPED_IMAGE;

public class CompanySettingsActivity extends BaseActivity
        implements FileUploadQueryService.OnFileUploadListener {

    private final static String TAG = "Company Settings";

    private final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private final int ALPHA_ANIMATIONS_DURATION = 200;
    private boolean mIsTheTitleVisible = false;

    private CompanyModel companyModel;
    private FileUploadQueryService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_settings_activity);
        setToolbar(R.id.toolbar, true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT, 0.1f));
        this.service = FileUploadQueryService.getService(this);

        companyModel = (CompanyModel)  getHolder().get();
    }

    @Override
    protected void bindViews() {
    }

    public void onPhotosClick(View v) {
        Intent gallery = new Gallery
                .Builder()
                .from(this)
                .multiSelectionMode(false)
                .build();
        startActivityForResult(gallery, RequestCodesConstants.GALLERY_ACTIVITY_REQUEST);
    }

    @Override
    public void onUpload(String path) {
        Log.d(TAG, path);
        //obtainCompanyLogo(companyModel, profileAvatarImageView, path);
    }

    @Override
    public void onError(ErrorModel error) {
        Log.d(TAG, error.toString());
    }

    private void handleImage(Intent data) {
        String path = data.getStringExtra(PUT_CROPPED_IMAGE);
        service.uploadImage("1", path, "image", "photo");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        if (requestCode == RequestCodesConstants.GALLERY_ACTIVITY_REQUEST) {
            handleImage(data);
            return;
        }
    }
}
