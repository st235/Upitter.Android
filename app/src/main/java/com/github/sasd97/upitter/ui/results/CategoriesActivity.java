package com.github.sasd97.upitter.ui.results;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.ErrorModel;
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

import static com.github.sasd97.upitter.constants.IntentKeysConstants.*;

public class CategoriesActivity extends BaseActivity
        implements CategoriesQueryService.OnCategoryListener,
        CategoryRecyclerAdapter.OnItemClickListener {

    private FloatingActionButton fab;

    private RecyclerView categoryRecyclerView;
    private CategoryRecyclerAdapter categoryRecyclerAdapter;
    private CategoriesQueryService queryService;

    private List<CategoryResponseModel> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_activity);
        setToolbar(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));

        categoryRecyclerAdapter = new CategoryRecyclerAdapter(this, new ArrayList<CategoryResponseModel>());
        categoryRecyclerAdapter.setOnItemClickListener(this);

        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryRecyclerView.setAdapter(categoryRecyclerAdapter);
        categoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    if (fab.isShown()) {
                        fab.hide();
                    }
                } else if (dy < 0) {
                    if (!fab.isShown()) {
                        fab.show();
                    }
                }
            }
        });

        queryService = CategoriesQueryService.getService(this);
        queryService.getCategories(Locale.getDefault().getLanguage());
    }

    @Override
    protected void bindViews() {
        categoryRecyclerView = findById(R.id.recycler_view_categories_activity);
        fab = findById(R.id.fab_categories_activity);
    }

    @Override
    public void onGetCategories(List<CategoryResponseModel> categories) {
        this.categories = categories;
        categoryRecyclerAdapter.addAll(ListUtils.filter(categories, new ListUtils.OnListInteractionListener<CategoryResponseModel>() {
            @Override
            public boolean isFit(CategoryResponseModel other) {
                return other.getId() % 100 == 0;
            }
        }));
    }

    public void onConfirmSelection(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putIntegerArrayListExtra(CATEGORIES_ATTACH, categoryRecyclerAdapter.getSelected());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onClick(final CategoryResponseModel category, final int position) {
        final List<CategoryResponseModel> subCategories = ListUtils.filter(categories, new ListUtils.OnListInteractionListener<CategoryResponseModel>() {
            @Override
            public boolean isFit(CategoryResponseModel other) {
                return other.getParentId() == category.getId();
            }
        });

        new MaterialDialog.Builder(this)
                .title(category.getTitle())
                .cancelable(true)
                .items(subCategories)
                .itemsCallbackMultiChoice(category.getSelectedSubcategories(), new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        category.setSubcategoriesSelected(which, subCategories);
                        return true;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        categoryRecyclerAdapter.notifyItemChanged(position);
                    }
                })
                .positiveText(R.string.category_select_categories_activity)
                .show();
    }

    @Override
    public void onError(ErrorModel errorModel) {

    }
}
