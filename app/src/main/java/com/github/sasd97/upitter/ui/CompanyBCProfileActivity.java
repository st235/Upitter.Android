package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.AppBarStateChangeListener;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.models.response.containers.PostsContainerModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.models.response.pointers.SubscribersPointerModel;
import com.github.sasd97.upitter.services.query.CompanyProfileQueryService;
import com.github.sasd97.upitter.ui.adapters.pagers.CompanyProfileBCPager;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Names;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.COMPANY_ALIAS;

public class CompanyBCProfileActivity extends BaseActivity
        implements CompanyProfileQueryService.OnCompanySearchListener {

    private final String SPACE = " ";

    private String alias;
    private UserModel userModel;
    private CompanyPointerModel companyModel;
    private CompanyProfileQueryService findQueryService;

    private PostsContainerModel posts;
    private SubscribersPointerModel subscribers;

    @BindView(R.id.logo_company_profile) ImageView logoImageView;
    @BindView(R.id.title_company_profile) TextView titleTextView;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.scrollable) View scrollableLayout;
    @BindView(R.id.collpasingToolbar) CollapsingToolbarLayout collapsingToolbarLayout;

    @BindArray(R.array.company_profile_tabs_titile) String[] titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_bc_profile);
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar, true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));
        userModel = getHolder().get();

        ((AppBarLayout) findById(R.id.appbar)).addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                switch (state) {
                    case COLLAPSED:
                        collapsingToolbarLayout.setTitle(companyModel == null ? SPACE : companyModel.getName());
                        break;
                    case IDLE:
                        collapsingToolbarLayout.setTitle(SPACE);
                        break;
                }
            }

            @Override
            public void onPercentage(double percentage) {
                scrollableLayout.setAlpha((float) percentage);
            }
        });

        findQueryService = CompanyProfileQueryService.getService(this);
        collapsingToolbarLayout.setTitle(SPACE);

        alias = getIntent().getStringExtra(COMPANY_ALIAS);

        findQueryService.findByAlias(userModel.getAccessToken(), alias);
        findQueryService.obtainPosts(userModel.getAccessToken(), alias);
        findQueryService.obtainSubscribers(userModel.getAccessToken(), alias);
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
                            Dimens.drr());

            holder.setImageDrawable(textDrawable);
            return;
        }

        Glide
            .with(this)
            .load(logoUrl)
            .bitmapTransform(new CenterCrop(this), new RoundedCornersTransformation(this, Dimens.drr(), 0))
            .into(holder);
    }

    private void setupPager() {
        viewPager.setAdapter(new CompanyProfileBCPager(getSupportFragmentManager(), titles, posts, subscribers));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onFind(CompanyPointerModel company) {
        companyModel = company;
        obtainCompanyHeader(company);
    }

    @Override
    public void onObtainPosts(PostsContainerModel posts) {
        this.posts = posts;
        if (this.subscribers != null) setupPager();
    }

    @Override
    public void onSubscribersObtained(SubscribersPointerModel subscribers) {
        this.subscribers = subscribers;
        if (this.posts != null) setupPager();
    }

    @Override
    public void onError(ErrorModel error) {
    }

    @OnClick(R.id.company_profile_fc_information)
    public void onInformationButtonClick(View v) {
        Intent intent = new Intent(this, CompanyInformationActivity.class);
        intent.putExtra(COMPANY_ALIAS, alias);
        startActivity(intent);
    }
}
