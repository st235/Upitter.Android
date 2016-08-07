package com.github.sasd97.upitter.ui.fragments;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
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
import com.github.sasd97.upitter.services.query.CompanyQueryService;
import com.github.sasd97.upitter.services.query.FileUploadQueryService;
import com.github.sasd97.upitter.ui.CompanySettingsActivity;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Gallery;
import com.github.sasd97.upitter.utils.Names;
import com.orhanobut.logger.Logger;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.PUT_CROPPED_IMAGE;

/**
 * Created by alexander on 06.08.16.
 */

public class CompanyBaseSettingsFragment extends BaseFragment
        implements FileUploadQueryService.OnFileUploadListener,
        CompanyQueryService.OnCompanyListener {

    private CompanyModel companyModel;
    private CompanyQueryService companyQueryService;
    private FileUploadQueryService fileUploadQueryService;

    @BindView(R.id.company_avatar_base_settings) ImageView companyAvatar;
    @BindView(R.id.current_avatar_state) TextView uploadState;
    @BindView(R.id.company_title_edittext) EditText companyTitleEdt;
    @BindView(R.id.company_description_edittext) EditText companyDescriptionEdt;

    @BindString(R.string.avatar_upload_state_success) String UPLOAD_SUCCESS;
    @BindString(R.string.avatar_upload_state_error) String UPLOAD_ERROR;
    @BindString(R.string.avatar_upload_state_pending) String UPLOAD_PENDING;

    public CompanyBaseSettingsFragment() {
        super(R.layout.fragment_company_base_settings);
    }

    public static CompanyBaseSettingsFragment getFragment() {
        return new CompanyBaseSettingsFragment();
    }

    @Override
    protected void setupViews() {
        companyModel = (CompanyModel) getHolder().get();
        companyQueryService = CompanyQueryService.getService(this);
        fileUploadQueryService = FileUploadQueryService.getService(this);

        obtainCompanyLogo(companyAvatar, companyModel.getAvatarUrl());
        companyTitleEdt.setText(companyModel.getName());
        companyDescriptionEdt.setText(companyModel.getDescription());
    }

    @Override
    public void onAvatarChanged(String path) {
        uploadState.setText(UPLOAD_SUCCESS);
        getHolder().saveAvatar(path);
        obtainCompanyLogo(companyAvatar, path);
    }

    @Override
    public void onUpload(String path) {
        Logger.d(path);
        companyQueryService.changeAvatar(companyModel.getAccessToken(), path);
    }

    @Override
    public void onError(ErrorModel error) {
        uploadState.setText(UPLOAD_ERROR);
    }

    @OnClick(R.id.company_avatar_upload_area)
    public void onPhotosClick(View v) {
        Intent gallery = new Gallery
                .Builder()
                .from(getContext())
                .multiSelectionMode(false)
                .build();

        uploadState.setText(UPLOAD_PENDING);
        startActivityForResult(gallery, RequestCodesConstants.GALLERY_ACTIVITY_REQUEST);
    }

    private void handleImage(Intent data) {
        String path = data.getStringExtra(PUT_CROPPED_IMAGE);
        fileUploadQueryService.uploadAvatar(companyModel.getUId(), path);
    }

    private void obtainCompanyLogo(ImageView holder, String logoUrl) {
        if (logoUrl == null) {
            String preview = Names.getNamePreview(companyModel.getName());

            TextDrawable textDrawable = TextDrawable
                    .builder()
                    .buildRoundRect(preview,
                            ContextCompat.getColor(getContext(), R.color.colorShadowDark),
                            Dimens.dpToPx(4));

            holder.setImageDrawable(textDrawable);
            return;
        }

        Glide
                .with(this)
                .load(logoUrl)
                .bitmapTransform(new CenterCrop(getContext()),
                        new RoundedCornersTransformation(getContext(), Dimens.dpToPx(4), 0))
                .into(holder);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != BaseActivity.RESULT_OK) return;

        if (requestCode == RequestCodesConstants.GALLERY_ACTIVITY_REQUEST) {
            handleImage(data);
            return;
        }
    }
}
