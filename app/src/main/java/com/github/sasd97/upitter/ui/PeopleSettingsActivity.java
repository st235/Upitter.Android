package com.github.sasd97.upitter.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.holders.PeopleHolder;
import com.github.sasd97.upitter.models.PeopleModel;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Names;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.Upitter.getHolder;

public class PeopleSettingsActivity extends BaseActivity {


    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.blured_background) ImageView bluredBackground;
    @BindView(R.id.user_avatar) CircleImageView userAvatar;
    @BindView(R.id.user_name) TextView userName;

    private PeopleModel people;

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

        obtainPeopleLogo(userAvatar, bluredBackground, people.getAvatarUrl());
        userName.setText(people.getName());
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
}
