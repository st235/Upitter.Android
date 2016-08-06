package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.constants.RequestCodesConstants;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.services.query.FileUploadQueryService;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Gallery;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.orhanobut.logger.Logger;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import butterknife.BindView;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.PUT_CROPPED_IMAGE;

public class CompanySettingsActivity extends BaseActivity
        implements FileUploadQueryService.OnFileUploadListener {

    private final static String TAG = "Company Settings";

    private CompanyModel companyModel;
    private FileUploadQueryService service;

    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_settings);
        setToolbar(R.id.toolbar, true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT, 0.1f));
    }

    @Override
    protected void setupViews() {
        companyModel = (CompanyModel) getHolder().get();
        service = FileUploadQueryService.getService(this);
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
        Logger.d(path);
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
