package com.github.sasd97.upitter.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.categories.CategoryResponseModel;
import com.github.sasd97.upitter.services.query.BusinessRegistrationQueryService;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Gallery;
import com.github.sasd97.upitter.utils.Names;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.PUT_CROPPED_IMAGE;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.GALLERY_ACTIVITY_REQUEST;

/**
 * Created by Alexadner Dadukin on 24.06.2016.
 */
public class BaseBusinessRegistrationFragment extends BaseFragment
        implements BusinessRegistrationQueryService.OnBusinessRegistrationListener {

    private ImageView avatarImageView;
    private LinearLayout avatarLayout;
    private RelativeLayout categoriesLayout;

    private BusinessRegistrationQueryService queryService;

    public static BaseBusinessRegistrationFragment getFragment() {
        return new BaseBusinessRegistrationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.business_registration_base_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        queryService = BusinessRegistrationQueryService.getService(this);
        queryService.getCategories();

        avatarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAvatarChooseClick();
            }
        });
        categoriesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCategoryChooseClick();
            }
        });
    }

    @Override
    protected void bindViews() {
        avatarImageView = findById(R.id.avatar_business_registration_base_fragment);
        avatarLayout = findById(R.id.avatar_url_business_registration_base_fragment);
        categoriesLayout = findById(R.id.categories_choose_business_registration_base_fragment);
    }

    public void onAvatarChooseClick() {
        startActivityForResult(new Gallery.Builder()
                .from(getContext())
                .multiSelectionMode(false)
                .build(), GALLERY_ACTIVITY_REQUEST);
    }

    public void onCategoryChooseClick() {

    }

    private void handleAvatarIntent(@NonNull Intent intent) {
        String path = intent.getStringExtra(PUT_CROPPED_IMAGE);

        Glide
                .with(this)
                .load(Names
                        .getInstance()
                        .getFilePath(path)
                        .toString())
                .bitmapTransform(new CenterCrop(getContext()), new RoundedCornersTransformation(getContext(), Dimens.dpToPx(4), 0))
                .into(avatarImageView);
    }

    @Override
    public void onGetCategories(List<CategoryResponseModel> categories) {
        for (CategoryResponseModel category: categories) {
            Log.d("HELLO_WORLD", category.toString());
        }
    }

    @Override
    public void onError() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != BaseActivity.RESULT_OK) return;
        if (requestCode == GALLERY_ACTIVITY_REQUEST) handleAvatarIntent(data);
    }
}
