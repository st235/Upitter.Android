package com.github.sasd97.upitter.models.response.containers;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.pointers.CategoryPointerModel;

import java.util.List;

/**
 * Created by alexander on 27.06.16.
 */

public class CategoriesContainerModel extends BaseResponseModel<List<CategoryPointerModel>> {

    public List<CategoryPointerModel> getCategories() {
        return mResponse;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (CategoryPointerModel category: mResponse)
            builder.append(category.toString()).append("\n");
        return builder.toString();
    }
}
