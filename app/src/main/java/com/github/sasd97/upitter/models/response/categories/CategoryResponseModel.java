package com.github.sasd97.upitter.models.response.categories;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

/**
 * Created by alexander on 27.06.16.
 */
public class CategoryResponseModel extends BaseResponseModel {

    @SerializedName("customId")
    @Expose
    private int mId;

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("parentCategory")
    @Expose
    private int mParentId;

    @SerializedName("logoUrl")
    @Expose
    private String mAvatarUrl;

    private Integer[] mSubcategoriesSelected;
    private int[] mSubcategoriesSelectedIds;

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getParentId() {
        return mParentId;
    }

    public void setSubcategoriesSelected(Integer[] subcategories, List<CategoryResponseModel> list) {
        mSubcategoriesSelected = subcategories;

        int length = subcategories.length;
        mSubcategoriesSelectedIds = new int[length];
        for (int i = 0; i < length; i++) {
            int subId = list.get(subcategories[i]).getId();
            mSubcategoriesSelectedIds[i] = subId;
        }
    }

    public Integer[] getSelectedSubcategories() {
        return mSubcategoriesSelected;
    }

    public int[] getSelectedSubcategoriesIds() {
        return mSubcategoriesSelectedIds;
    }

    public String getLogoUrl() {
        return mAvatarUrl;
    }

    public String getDebugInfo() {
        return String.format(Locale.getDefault(), "Category [%1$d] with parent %2$d\n%3$s\nAvatarUrl: %4$s",
                mId,
                mParentId,
                mTitle,
                mAvatarUrl);
    }

    @Override
    public String toString() {
        return mTitle;
    }
}
