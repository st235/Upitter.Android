package com.github.sasd97.upitter.ui.results;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.response.pointers.CategoryPointerModel;
import com.github.sasd97.upitter.services.query.CategoriesQueryService;
import com.github.sasd97.upitter.ui.adapters.recyclers.CategoriesListRecycler;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.*;

public class CategoriesSelectionResult extends BaseActivity
        implements CategoriesQueryService.OnCategoryListener,
        CategoriesListRecycler.OnItemClickListener {

    @BindView(R.id.fab_categories_activity) FloatingActionButton fab;
    @BindView(R.id.recycler_view_categories_activity) RecyclerView categoryRecyclerView;

    private List<Integer> selectedCategories;
    private List<CategoryPointerModel> categories;
    private CategoriesListRecycler categoriesListRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_selection);
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar, true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));

        if (getIntent().hasExtra(CATEGORIES_ATTACH))
            selectedCategories = getIntent().getIntegerArrayListExtra(CATEGORIES_ATTACH);

        categoriesListRecycler = new CategoriesListRecycler(this, new ArrayList<CategoryPointerModel>());
        categoriesListRecycler.setOnItemClickListener(this);

        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryRecyclerView.setAdapter(categoriesListRecycler);
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

        CategoriesQueryService queryService = CategoriesQueryService.getService(this);
        queryService.getCategories(Locale.getDefault().getLanguage());
    }

    @Override
    public void onGetCategories(List<CategoryPointerModel> categories) {
        this.categories = categories;

        categoriesListRecycler.addAll(ListUtils.filter(categories, new ListUtils.OnListInteractionListener<CategoryPointerModel>() {
            @Override
            public boolean isFit(CategoryPointerModel other) {
                return other.getId() % 100 == 0;
            }
        }));

        if (selectedCategories == null || selectedCategories.size() == 0) return;
        categoriesListRecycler.each(new ListUtils.OnIteratorListener<CategoryPointerModel>() {
            @Override
            public void iterate(CategoryPointerModel object, List<CategoryPointerModel> all) {
                preSelectCategories(object, selectedCategories);
            }
        });
    }

    private void preSelectCategories(final CategoryPointerModel parentModel, List<Integer> selected) {
        if (!parentModel.isParent()) return;

        List<CategoryPointerModel> children = getChildren(parentModel);

        List<Integer> childrenSelected = ListUtils.filter(selected, new ListUtils.OnListInteractionListener<Integer>() {
            @Override
            public boolean isFit(Integer other) {
                return parentModel.in(other);
            }
        });

        if (childrenSelected.size() == 0) return;
        Integer[] array = ListUtils.toArray(Integer.class, childrenSelected);
        parentModel.setSubcategoriesId(array, children);
    }

    public void onConfirmSelection(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putIntegerArrayListExtra(CATEGORIES_ATTACH, categoriesListRecycler.getSelected());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onClick(final CategoryPointerModel category, final int position) {
        final List<CategoryPointerModel> subCategories = getChildren(category);

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
                        categoriesListRecycler.notifyItemChanged(position);
                    }
                })
                .positiveText(R.string.category_select_categories_activity)
                .show();
    }

    @Override
    public void onError(ErrorModel errorModel) {

    }

    private List<CategoryPointerModel> getChildren(final CategoryPointerModel category) {
        return ListUtils.filter(categories, new ListUtils.OnListInteractionListener<CategoryPointerModel>() {
            @Override
            public boolean isFit(CategoryPointerModel other) {
                return other.getParentId() == category.getId();
            }
        });
    }
}
