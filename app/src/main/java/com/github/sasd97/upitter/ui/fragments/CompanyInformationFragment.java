package com.github.sasd97.upitter.ui.fragments;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.response.pointers.ActivityPointerModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.services.query.ActivityQueryService;
import com.github.sasd97.upitter.ui.adapters.recyclers.ActivitiesRecycler;
import com.github.sasd97.upitter.ui.adapters.recyclers.ContactPhonesPreviewRecycler;
import com.github.sasd97.upitter.ui.adapters.recyclers.SocialIconsPreviewRecycler;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.Palette;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Alexadner Dadukin on 21.09.2016.
 */

public class CompanyInformationFragment extends BaseFragment
        implements ActivityQueryService.OnActivityListener {

    private CompanyPointerModel company;

    @BindView(R.id.company_description_textview) TextView companyDescriptionTextView;
    @BindView(R.id.contact_phones_recycler_view) RecyclerView contactPhonesRecyclerView;
    @BindView(R.id.activities_recycler_view) RecyclerView activitiesRecyclerView;
    @BindView(R.id.social_links_recycler_view) RecyclerView socialLinksRecyclerView;
    @BindView(R.id.contact_site) TextView contactSite;

    public CompanyInformationFragment() {
        super(R.layout.fragment_information);
    }

    public static CompanyInformationFragment getFragment(CompanyPointerModel company) {
        CompanyInformationFragment fragment = new CompanyInformationFragment();
        fragment.setCompany(company);
        return fragment;
    }

    @Override
    protected void setupViews() {
        ActivityQueryService
                .getService(this)
                .getTitles(ListUtils.toArray(Integer.class, company.getActivity()));

        companyDescriptionTextView.setText(company.getDescription());

        contactSite.setText(company.getSite());

        contactPhonesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        contactPhonesRecyclerView.setAdapter(new ContactPhonesPreviewRecycler(company.getContactPhones()));
        socialLinksRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        socialLinksRecyclerView.setAdapter(new SocialIconsPreviewRecycler(company.getSocialLinks()));

    }

    @Override
    public void obObtainTitles(List<ActivityPointerModel> categories) {
        activitiesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        activitiesRecyclerView.setAdapter(new ActivitiesRecycler(categories));
    }

    @Override
    public void onError(ErrorModel error) {
        Logger.e(error.toString());
    }

    public void setCompany(CompanyPointerModel company) {
        this.company = company;
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

        PackageManager packageManager = getContext().getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(customTabsIntent.intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            if (TextUtils.equals(packageName, PACKAGE_NAME))
                customTabsIntent.intent.setPackage(PACKAGE_NAME);
        }

        customTabsIntent.launchUrl(getActivity(), uri);
    }
}
