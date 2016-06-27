package com.github.sasd97.upitter.ui.results;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.categories.CategoryResponseModel;
import com.github.sasd97.upitter.services.query.CategoriesQueryService;
import com.github.sasd97.upitter.ui.adapters.CategoryRecyclerAdapter;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoriesActivity extends BaseActivity
        implements CategoriesQueryService.OnCategoryListener {

    private RecyclerView categoryRecyclerView;
    private CategoryRecyclerAdapter categoryRecyclerAdapter;
    private CategoriesQueryService queryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_activity);
        setToolbar(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));

        categoryRecyclerAdapter = new CategoryRecyclerAdapter(new ArrayList<CategoryResponseModel>());

        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryRecyclerView.setAdapter(categoryRecyclerAdapter);

        queryService = CategoriesQueryService.getService(this);
        queryService.getCategories(Locale.getDefault().getLanguage());
    }

    @Override
    protected void bindViews() {
        categoryRecyclerView = findById(R.id.recycler_view_categories_activity);
    }

    @Override
    public void onGetCategories(List<CategoryResponseModel> categories) {
        categoryRecyclerAdapter.addAll(ListUtils.filter(categories, new ListUtils.OnListInteractionListener<CategoryResponseModel>() {
            @Override
            public boolean isFit(CategoryResponseModel other) {
                return other.getId() % 100 == 0;
            }
        }));
    }

    @Override
    public void onError() {

    }
}
