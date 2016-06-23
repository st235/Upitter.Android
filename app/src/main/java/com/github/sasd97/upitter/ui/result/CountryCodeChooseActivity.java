package com.github.sasd97.upitter.ui.result;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.CountryModel;
import com.github.sasd97.upitter.ui.adapters.CountryCodeChooseRecyclerAdapter;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Countries;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

public class CountryCodeChooseActivity extends BaseActivity
        implements CountryCodeChooseRecyclerAdapter.OnItemClickListener {

    private Toolbar toolbar;
    private LinearLayout progressLinearLayout;

    private FastScrollRecyclerView countryRecyclerView;
    private CountryCodeChooseRecyclerAdapter adapter;
    private StickyHeaderDecoration decor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_code_choose_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));

        adapter = new CountryCodeChooseRecyclerAdapter(CountryCodeChooseActivity.this, new ArrayList<CountryModel>());
        decor = new StickyHeaderDecoration(adapter);
        countryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        countryRecyclerView.setAdapter(adapter);
        countryRecyclerView.addItemDecoration(decor);
        countryRecyclerView.setItemAnimator(new SlideInRightAnimator());

        if (!Countries.isObtained()) {
            //  TODO make error handling
            return;
        }

        progressLinearLayout.setVisibility(View.GONE);
        adapter.addItems(Countries.getCountries());
    }

    @Override
    protected void bindViews() {
        toolbar = findById(R.id.toolbar);
        countryRecyclerView = findById(R.id.recycler_view_country_code_choose_activity);
        progressLinearLayout = findById(R.id.still_progress_country_code_choose_activity);
    }

    @Override
    public void onClick(CountryModel country) {
        Log.d("COUNTRY", country.toString());
    }
}
