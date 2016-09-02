package com.github.sasd97.upitter.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.models.response.containers.PostsContainerModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.models.response.pointers.PostPointerModel;
import com.github.sasd97.upitter.services.query.CompanyProfileQueryService;
import com.github.sasd97.upitter.ui.adapters.pagers.CompanyProfilePager;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Names;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.COMPANY_ALIAS;

public class CompanyProfileActivity extends BaseActivity
        implements CompanyProfileQueryService.OnCompanySearchListener {

    private final String SPACE = " ";

    private String alias;
    private UserModel userModel;
    private CompanyProfileQueryService findQueryService;

    @BindView(R.id.logo_company_profile) ImageView logoImageView;
    @BindView(R.id.title_company_profile) TextView titleTextView;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.collpasingToolbar) CollapsingToolbarLayout collapsingToolbarLayout;

    @BindArray(R.array.company_profile_tabs_titile) String[] titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar);
        userModel = getHolder().get();
        findQueryService = CompanyProfileQueryService.getService(this);
        collapsingToolbarLayout.setTitle(SPACE);

        alias = getIntent().getStringExtra(COMPANY_ALIAS);

        findQueryService.findByAlias(userModel.getAccessToken(), alias);
        findQueryService.obtainPosts(userModel.getAccessToken(), alias);
    }

    private void obtainCompanyHeader(CompanyPointerModel company) {
        obtainCompanyLogo(company, logoImageView, company.getLogoUrl());
        titleTextView.setText(company.getName());
    }

    private void obtainCompanyLogo(CompanyPointerModel company, ImageView holder, String logoUrl) {
        if (logoUrl == null) {
            String preview = Names.getNamePreview(company.getName());

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
                .bitmapTransform(new CenterCrop(this), new RoundedCornersTransformation(this, Dimens.drr(), 0))
                .into(holder);
    }

    @Override
    public void onFind(CompanyPointerModel company) {
        Logger.i(company.toString());
        obtainCompanyHeader(company);
    }

    @Override
    public void onObtainPosts(PostsContainerModel posts) {
        viewPager.setAdapter(new CompanyProfilePager(getSupportFragmentManager(), titles, posts));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onError(ErrorModel error) {

    }
}
