package com.github.sasd97.upitter.ui.results;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.constants.IntentKeysConstants;
import com.github.sasd97.upitter.models.CategoryModel;
import com.github.sasd97.upitter.ui.adapters.SearchableRecyclerAdapter;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Categories;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 05.07.16.
 */
public class PostCategoriesChooseActivity extends BaseActivity
        implements SearchableRecyclerAdapter.OnItemSelectedListener<CategoryModel>,
        View.OnClickListener {

    public static final String BADGE_RESULT = "SEARCH_RESULT";

    private RecyclerView recyclerView;
    private SearchableRecyclerAdapter adapter;
    private List<CategoryModel> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_choose_activity);
        setToolbar(R.id.toolbar, true);

        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT, 0.2f));

        data = Categories.getCategories();
        recyclerView = (RecyclerView) findViewById(R.id.location_recycler_view);

        adapter = new SearchableRecyclerAdapter(false, false, this, data);
        adapter.setOnItemSelectedListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void bindViews() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(boolean isChecked, CategoryModel categoryModel, int position) {
        Intent result = new Intent();
        result.putExtra(IntentKeysConstants.CATEGORIES_ATTACH, categoryModel);
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.updateDataSet(find(data, newText));
                return false;
            }
        });
        return true;
    }

    private List<CategoryModel> find(List<CategoryModel> origin, String query) {
        List<CategoryModel> result = new ArrayList<>();

        for (CategoryModel badge: origin) {
            if (badge.getTitle().toLowerCase().contains(query.toLowerCase())) result.add(badge);
        }

        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            case R.id.action_search:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

