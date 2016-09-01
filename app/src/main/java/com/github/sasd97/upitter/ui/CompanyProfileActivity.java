package com.github.sasd97.upitter.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.services.query.CompanyFindQueryService;
import com.github.sasd97.upitter.ui.adapters.pagers.CompanyProfilePager;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.orhanobut.logger.Logger;

import butterknife.BindArray;
import butterknife.BindView;

import static com.github.sasd97.upitter.Upitter.getHolder;

public class CompanyProfileActivity extends BaseActivity
        implements CompanyFindQueryService.OnCompanySearchListener {

    private final String SPACE = " ";

    private UserModel userModel;
    private CompanyFindQueryService findQueryService;

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
        findQueryService = CompanyFindQueryService.getService(this);
        collapsingToolbarLayout.setTitle(SPACE);

        viewPager.setAdapter(new CompanyProfilePager(getSupportFragmentManager(), titles));
        tabLayout.setupWithViewPager(viewPager);
        findQueryService.findByAlias(userModel.getAccessToken(), "id13");
    }

    @Override
    public void onFind(CompanyPointerModel company) {
        Logger.i(company.toString());
    }

    @Override
    public void onError(ErrorModel error) {

    }
}
