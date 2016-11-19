package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.models.response.containers.PostsContainerModel;
import com.github.sasd97.upitter.models.response.pointers.ActivityPointerModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.models.response.pointers.SocialIconPointerModel;
import com.github.sasd97.upitter.models.response.pointers.SubscribersPointerModel;
import com.github.sasd97.upitter.services.query.ActivityQueryService;
import com.github.sasd97.upitter.services.query.CompanyProfileQueryService;
import com.github.sasd97.upitter.ui.adapters.recyclers.ActivitiesRecycler;
import com.github.sasd97.upitter.ui.adapters.recyclers.ContactPhonesPreviewRecycler;
import com.github.sasd97.upitter.ui.adapters.recyclers.SocialIconsPreviewRecycler;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.Names;
import com.github.sasd97.upitter.utils.Palette;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.orhanobut.logger.Logger;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.COMPANY_ALIAS;

public class CompanyInformationActivity extends BaseActivity
        implements CompanyProfileQueryService.OnCompanySearchListener,
        ActivityQueryService.OnActivityListener {

    private final String SPACE = " ";

    private String alias;
    private UserModel userModel;
    private ActivityQueryService activityQueryService;
    private CompanyProfileQueryService findQueryService;

    private CompanyPointerModel company;

    @BindView(R.id.collpasingToolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.logo_company_profile) ImageView logoImageView;
    @BindView(R.id.title_company_profile) TextView titleTextView;
    @BindView(R.id.company_description_textview) TextView companyDescriptionTextView;
    @BindView(R.id.contact_phones_recycler_view) RecyclerView contactPhonesRecyclerView;
    @BindView(R.id.activities_recycler_view) RecyclerView activitiesRecyclerView;
    @BindView(R.id.social_links_recycler_view) RecyclerView socialLinksRecyclerView;
    @BindView(R.id.contact_site) TextView contactSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_information);
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar, true);
        collapsingToolbarLayout.setTitle(SPACE);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));

        userModel = getHolder().get();
        findQueryService = CompanyProfileQueryService.getService(this);
        activityQueryService = ActivityQueryService.getService(this);

        alias = getIntent().getStringExtra(COMPANY_ALIAS);

        findQueryService.findByAlias(userModel.getAccessToken(), alias);
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

    @Override
    public void onFind(CompanyPointerModel company) {
        this.company = company;
        activityQueryService.getTitles(ListUtils.toArray(Integer.class, company.getActivity()));

        obtainCompanyLogo(company, logoImageView, company.getLogoUrl());
        titleTextView.setText(company.getName());
        companyDescriptionTextView.setText(company.getDescription());
        contactSite.setText(company.getSite());
        contactPhonesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        contactPhonesRecyclerView.setAdapter(new ContactPhonesPreviewRecycler(company.getContactPhones()));
        socialLinksRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        socialLinksRecyclerView.setAdapter(new SocialIconsPreviewRecycler(company.getSocialLinks()));
    }

    @Override
    public void obObtainTitles(List<ActivityPointerModel> categories) {
        activitiesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        activitiesRecyclerView.setAdapter(new ActivitiesRecycler(categories));
    }

    @Override
    public void onObtainPosts(PostsContainerModel posts) {

    }

    @Override
    public void onSubscribersObtained(SubscribersPointerModel subscribers) {

    }

    @Override
    public void onError(ErrorModel error) {
        Logger.e(error.toString());
    }

    @OnClick(R.id.contact_site)
    public void onSiteClick(View v) {
        String site = company.getSite();
        if (!site.contains("http://")) site = "http://".concat(site);
        Uri uri = Uri.parse(site);
        String PACKAGE_NAME = "com.android.chrome";


        CustomTabsIntent customTabsIntent = new CustomTabsIntent
                .Builder()
                .setToolbarColor(Palette.getPrimaryColor())
                .build();
        customTabsIntent.intent.setData(uri);

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(customTabsIntent.intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            if (TextUtils.equals(packageName, PACKAGE_NAME))
                customTabsIntent.intent.setPackage(PACKAGE_NAME);
        }

        customTabsIntent.launchUrl(this, uri);
    }
}
