package com.github.sasd97.upitter.ui.fragments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.services.query.CompanyQueryService;
import com.github.sasd97.upitter.services.query.FileUploadQueryService;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.results.CategoriesSelectionResult;
import com.github.sasd97.upitter.ui.results.SetupLocationResult;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Gallery;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.Names;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.CATEGORIES_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.PUT_CROPPED_IMAGE;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.LOCATION_LIST;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.CATEGORIES_ACTIVITY_REQUEST;

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
    @BindView(R.id.company_alias_edittext) EditText companyAliasEdt;
    @BindView(R.id.company_description_edittext) EditText companyDescriptionEdt;

    @BindString(R.string.avatar_upload_state_success) String UPLOAD_SUCCESS;
    @BindString(R.string.avatar_upload_state_error) String UPLOAD_ERROR;
    @BindString(R.string.avatar_upload_state_pending) String UPLOAD_PENDING;

    public CompanyBaseSettingsFragment() {
        super(R.layout.fragment_company_base_settings);
    }

    public static CompanyBaseSettingsFragment getFragment(CompanyModel companyModel) {
        CompanyBaseSettingsFragment fragment = new CompanyBaseSettingsFragment();
        fragment.setCompany(companyModel);
        return fragment;
    }

    public void setCompany(CompanyModel company) {
        companyModel = company;
    }

    @Override
    protected void setupViews() {
        companyQueryService = CompanyQueryService.getService(this);
        fileUploadQueryService = FileUploadQueryService.getService(this);

        obtainCompanyLogo(companyAvatar, companyModel.getAvatarUrl());
        companyTitleEdt.setText(companyModel.getName());
        companyAliasEdt.setText(companyModel.getAlias());
        companyDescriptionEdt.setText(companyModel.getDescription());

        companyTitleEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                companyModel.setName(editable.toString());
            }
        });
        companyAliasEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                companyModel.setAlias(editable.toString());
            }
        });
        companyDescriptionEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                companyModel.setDescription(editable.toString());
            }
        });
    }

    @OnClick(R.id.choose_location_fragment_company_base_settings)
    public void onLocationChoose(View v) {
        Intent intent = new Intent(getContext(), SetupLocationResult.class);
        ArrayList<CoordinatesModel> concreteCoordinates = new ArrayList<>(companyModel.getCoordinates());
        intent.putParcelableArrayListExtra(LOCATION_LIST, concreteCoordinates);
        startActivity(intent);
    }

    @OnClick(R.id.choose_category_fragment_company_base_settings)
    public void onCategoryChooseClick(View v) {
        Intent intent = new Intent(getActivity(), CategoriesSelectionResult.class);
        intent.putIntegerArrayListExtra(CATEGORIES_ATTACH, new ArrayList<>(companyModel.getCategories()));
        startActivityForResult(intent, CATEGORIES_ACTIVITY_REQUEST);
    }

    @Override
    public void onAvatarChanged(String path) {
        uploadState.setText(UPLOAD_SUCCESS);
        getHolder().saveAvatar(path);
        obtainCompanyLogo(companyAvatar, path);
    }

    @Override
    public void onAliasChanged(String alias) {
        Logger.i(alias);
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

    private void handleCategoriesIntent(@NonNull Intent intent) {
        ArrayList<Integer> categoriesSelected = intent.getIntegerArrayListExtra(CATEGORIES_ATTACH);
        companyModel.setCategories(categoriesSelected);
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

        if (requestCode == CATEGORIES_ACTIVITY_REQUEST) {
            handleCategoriesIntent(data);
            return;
        }

        if (requestCode == RequestCodesConstants.GALLERY_ACTIVITY_REQUEST) {
            handleImage(data);
            return;
        }
    }
}
