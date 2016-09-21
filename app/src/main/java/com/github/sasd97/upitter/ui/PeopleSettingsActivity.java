package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.constants.RequestCodesConstants;
import com.github.sasd97.upitter.holders.PeopleHolder;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.PeopleModel;
import com.github.sasd97.upitter.models.response.pointers.UserPointerModel;
import com.github.sasd97.upitter.services.query.FileUploadQueryService;
import com.github.sasd97.upitter.services.query.UserSettingsQueryService;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Gallery;
import com.github.sasd97.upitter.utils.Names;
import com.orhanobut.logger.Logger;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.PUT_CROPPED_IMAGE;

public class PeopleSettingsActivity extends BaseActivity
        implements FileUploadQueryService.OnFileUploadListener,
        UserSettingsQueryService.OnSettingsListener {

    private PeopleModel people;
    private FileUploadQueryService fileUploadQueryService;
    private UserSettingsQueryService userSettingsQueryService;

    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.blured_background) ImageView bluredBackground;
    @BindView(R.id.user_avatar) CircleImageView userAvatar;
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.user_nickname_edt) MaterialEditText userNicknameEdt;
    @BindView(R.id.user_name_edt) MaterialEditText userNameEdt;
    @BindView(R.id.user_surname_edt) MaterialEditText userSurnameEdt;
    @BindView(R.id.sex_radio_group) RadioGroup sexSelectorRdg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_settings);
        ButterKnife.bind(this);
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar, true);
        people = ((PeopleHolder) getHolder()).get();
        collapsingToolbarLayout.setTitle(" ");

        fileUploadQueryService = FileUploadQueryService.getService(this);
        userSettingsQueryService = UserSettingsQueryService.getService(this);
        RadioButton radioButton = (RadioButton) sexSelectorRdg.getChildAt(people.getSex());
        radioButton.setChecked(true);

        obtainPeopleLogo(userAvatar, bluredBackground, people.getAvatarUrl());
        userName.setText(people.getName());

        userNameEdt.setText(people.getName());
        userNicknameEdt.setText(people.getNickname());
        userSurnameEdt.setText(people.getSurname());

        sexSelectorRdg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                people.setSex(checkedId - 1);
            }
        });

        userNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                people.setName(editable.toString());
            }
        });
        userNicknameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                people.setNickname(editable.toString());
            }
        });
        userSurnameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                people.setSurname(editable.toString());
            }
        });
    }

    @OnClick(R.id.save_button)
    public void onSave(View v) {
        Logger.e(String.valueOf(people.getSex()));
        userSettingsQueryService.edit(people.getAccessToken(),
                people.getName(),
                people.getSurname(),
                people.getNickname(),
                people.getSex());
    }

    @OnClick(R.id.user_avatar_click_area)
    public void onUploadAvatar(View v) {
        Intent gallery = new Gallery
                .Builder()
                .from(this)
                .multiSelectionMode(false)
                .build();

        startActivityForResult(gallery, RequestCodesConstants.GALLERY_ACTIVITY_REQUEST);
    }

    private void obtainPeopleLogo(ImageView holder, ImageView background, String logoUrl) {
        if (logoUrl == null) {
            String preview = Names.getNamePreview(people.getName());

            TextDrawable textDrawable = TextDrawable
                    .builder()
                    .beginConfig()
                    .width(Dimens.dpToPx(75))
                    .height(Dimens.dpToPx(75))
                    .endConfig()
                    .buildRoundRect(preview,
                            ContextCompat.getColor(this, R.color.colorShadowDark),
                            Dimens.dpToPx(4));

            holder.setImageDrawable(textDrawable);
            return;
        }

        Glide
                .with(this)
                .load(logoUrl)
                .bitmapTransform(new CenterCrop(this), new RoundedCornersTransformation(this, Dimens.drr(), 0))
                .into(holder);

        Glide
                .with(this)
                .load(logoUrl)
                .bitmapTransform(new CenterCrop(this), new BlurTransformation(this))
                .into(background);
    }

    @Override
    public void onUpload(String path) {
        Logger.i(path);
        userSettingsQueryService.changeAvatar(people.getAccessToken(), path);
    }

    @Override
    public void onChangedAvatar(String path) {
        people.setAvatarUrl(path);
        obtainPeopleLogo(userAvatar, bluredBackground, people.getAvatarUrl());
        ((PeopleHolder) getHolder()).save(people);
    }

    @Override
    public void onChangedUser(UserPointerModel user) {
        ((PeopleHolder) getHolder()).save(people);
        finish();
    }

    @Override
    public void onError(ErrorModel error) {
        Logger.e(error.toString());
    }

    private void handleImage(Intent data) {
        String path = data.getStringExtra(PUT_CROPPED_IMAGE);
        Logger.i(path);
        fileUploadQueryService.uploadAvatar(people.getUId(), path);
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
