package com.github.sasd97.upitter.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.CategoryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 05.07.16.
 */
public final class Categories {

    private static List<CategoryModel> categories;

    public static void init(@NonNull Context context) {
        categories = new ArrayList<>();

        String[] ids = context.getResources().getStringArray(R.array.categories_id);
        String[] titles = context.getResources().getStringArray(R.array.categories_title);

        final int length = ids.length;
        if (ids.length != titles.length) throw new IllegalArgumentException("Length of ids and titles should be equals");

        for (int i = 0; i < length; i++)
            categories.add(new CategoryModel.Builder()
                                .id(ids[i])
                                .title(titles[i])
                                .drawableResource(context)
                                .build());
    }

    public static List<CategoryModel> getCategories() {
        return categories;
    }

    public static CategoryModel getDefaultCategory() {
        return categories.get(0);
    }
}
