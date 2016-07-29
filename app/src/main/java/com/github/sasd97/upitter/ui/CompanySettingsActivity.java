package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
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

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.PUT_CROPPED_IMAGE;

public class CompanySettingsActivity extends BaseActivity
        implements AppBarLayout.OnOffsetChangedListener,
        FileUploadQueryService.OnFileUploadListener {

    private final static String TAG = "Company Settings";

    private final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private final int ALPHA_ANIMATIONS_DURATION = 200;
    private boolean mIsTheTitleVisible = false;

    private CompanyModel companyModel;
    private FileUploadQueryService service;

    private AppBarLayout appBarLayout;
    private TextView profileToolbarNameTextView;
    private ImageView profileAvatarImageView;
    private EditText profileNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_settings_activity);
        setToolbar(R.id.toolbar, true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT, 0.1f));
        this.service = FileUploadQueryService.getService(this);

        companyModel = (CompanyModel)  getHolder().get();

        appBarLayout.addOnOffsetChangedListener(this);
        bindUser(companyModel);
    }

    @Override
    protected void bindViews() {
        appBarLayout = findById(R.id.appbar);
        profileToolbarNameTextView = findById(R.id.profile_toolbar_name_text);
        profileAvatarImageView = findById(R.id.profile_image_settings);
        profileNameEditText = findById(R.id.profile_name_settings);
    }

    private void bindUser(CompanyModel company) {
        profileToolbarNameTextView.setText(company.getName());
        profileNameEditText.setText(company.getName());
        obtainCompanyLogo(company, profileAvatarImageView, company.getAvatarUrl());
    }

    private void obtainCompanyLogo(CompanyModel company, ImageView holder, String logoUrl) {
        if (logoUrl == null) {
            String preview = Names.getNamePreview(Names.getNamePreview(company.getName()));

            TextDrawable textDrawable = TextDrawable
                    .builder()
                    .buildRoundRect(preview,
                            ContextCompat.getColor(this, R.color.colorShadowDark),
                            Dimens.dpToPx(4));

            holder.setImageDrawable(textDrawable);
            return;
        }

        Glide
                .with(this)
                .load(logoUrl)
                .bitmapTransform(new CenterCrop(this), new RoundedCornersTransformation(this, Dimens.dpToPx(4), 0))
                .into(holder);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(profileToolbarNameTextView, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                if (getCurrentFocus() != null)
                    Keyboard.hide(getCurrentFocus().getWindowToken());
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(profileToolbarNameTextView, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
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
        obtainCompanyLogo(companyModel, profileAvatarImageView, path);
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
