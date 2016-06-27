package com.github.sasd97.upitter.models.response.categories;

import com.github.sasd97.upitter.models.response.BaseResponseModel;

import java.util.List;

/**
 * Created by alexander on 27.06.16.
 */
public class CatergoriesResponseModel extends BaseResponseModel<List<CategoryResponseModel>> {

    public List<CategoryResponseModel> getCategories() {
        return mResponse;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (CategoryResponseModel category: mResponse)
            builder.append(category.toString()).append("\n");
        return builder.toString();
    }
}
