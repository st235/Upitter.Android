package com.github.sasd97.upitter.ui.fragments;

import android.support.annotation.LayoutRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.response.containers.PostsContainerModel;
import com.github.sasd97.upitter.models.response.pointers.ActivityPointerModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.models.response.pointers.SubscribersPointerModel;
import com.github.sasd97.upitter.services.query.ActivityQueryService;
import com.github.sasd97.upitter.ui.adapters.recyclers.ActivitiesRecycler;
import com.github.sasd97.upitter.ui.adapters.recyclers.ContactPhonesPreviewRecycler;
import com.github.sasd97.upitter.ui.adapters.recyclers.SocialIconsPreviewRecycler;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.Names;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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
}
