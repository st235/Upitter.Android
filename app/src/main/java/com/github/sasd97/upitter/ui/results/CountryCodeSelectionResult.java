package com.github.sasd97.upitter.ui.results;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.CountryModel;
import com.github.sasd97.upitter.ui.adapters.recyclers.CountryCodeListRecycler;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Countries;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

public class CountryCodeSelectionResult extends BaseActivity
        implements CountryCodeListRecycler.OnItemClickListener {

    @BindView(R.id.still_progress_country_code_choose_activity) LinearLayout progressLinearLayout;
    @BindView(R.id.recycler_view_country_code_choose_activity) FastScrollRecyclerView countryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_code_selection);
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar, true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));

        CountryCodeListRecycler adapter = new CountryCodeListRecycler(CountryCodeSelectionResult.this, new ArrayList<CountryModel>());
        StickyHeaderDecoration decor = new StickyHeaderDecoration(adapter);
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
    public void onClick(CountryModel country) {
        Log.d("COUNTRY", country.toString());
    }
}
