package com.github.sasd97.upitter.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.services.query.CompanyEditQueryService;
import com.github.sasd97.upitter.ui.adapters.pagers.CompanySettingsPager;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.orhanobut.logger.Logger;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;

import static com.github.sasd97.upitter.Upitter.getHolder;

public class CompanySettingsActivity extends BaseActivity
        implements CompanyEditQueryService.OnEditListener {

    private final static String TAG = "Company Settings";

    @BindArray(R.array.settings_pager) String[] titles;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    private CompanyModel companyModel;
    private CompanyEditQueryService queryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_settings);
        setToolbar(R.id.toolbar, true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT, 0.3f));
    }

    @Override
    protected void setupViews() {
        companyModel = (CompanyModel) getHolder().get();
        queryService = CompanyEditQueryService.getService(this);

        viewPager.setAdapter(new CompanySettingsPager(getSupportFragmentManager(), titles, companyModel));
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.fab)
    public void onSaveClick(View v) {
        Logger.json(companyModel.toJson());
        queryService.edit(companyModel.getAccessToken(), companyModel);
    }

    @Override
    public void onSuccess(CompanyPointerModel company) {
        Logger.i(companyModel.toString());
        Logger.json(companyModel.toJson());
        getHolder().save(companyModel);
        finish();
    }

    @Override
    public void onError(ErrorModel error) {
        Logger.i(error.toString());
    }
}
