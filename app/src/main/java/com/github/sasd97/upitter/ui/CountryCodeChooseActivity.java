package com.github.sasd97.upitter.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.CountryModel;
import com.github.sasd97.upitter.ui.adapters.CountryCodeChooseRecyclerAdapter;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Countries;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;
import com.tonicartos.superslim.LayoutManager;

import java.util.ArrayList;

public class CountryCodeChooseActivity extends BaseActivity implements CountryCodeChooseRecyclerAdapter.OnItemClickListener {

    private Toolbar toolbar;
    private RecyclerView countryRecyclerView;
    private CountryCodeChooseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_code_choose_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));

        countryRecyclerView.setLayoutManager(new LayoutManager(this));

        Countries.obtainCountries(new Countries.OnLoadListener() {
            @Override
            public void onLoad(ArrayList<CountryModel> list) {
                countryRecyclerView.setAdapter(new CountryCodeChooseRecyclerAdapter(CountryCodeChooseActivity.this, list));
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    protected void bindViews() {
        toolbar = findById(R.id.toolbar);
        countryRecyclerView = findById(R.id.recycler_view_country_code_choose_activity);
    }

    @Override
    public void onClick(CountryModel country) {
        Log.d("COUNTRY", country.toString());
    }
}
