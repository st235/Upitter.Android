package com.github.sasd97.upitter.models.response.pointers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

/**
 * Created by alexander on 27.06.16.
 */

public class CategoryPointerModel {

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
    private Integer[] mSubcategoriesSelectedIds;

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getParentId() {
        return mParentId;
    }

    public void setSubcategoriesId(Integer[] subcategories, List<CategoryPointerModel> children) {
        mSubcategoriesSelectedIds = subcategories;
        final int size = children.size();
        int counter = 0;

        mSubcategoriesSelected = new Integer[subcategories.length];
        for (int i = 0; i < size; i++) {
            for (Integer subcategory : subcategories) {
                if (children.get(i).getId() == subcategory) {
                    mSubcategoriesSelected[counter] = i;
                    counter++;
                }
            }
        }
    }

    public void setSubcategoriesSelected(Integer[] subcategories, List<CategoryPointerModel> list) {
        mSubcategoriesSelected = subcategories;
        final int length = subcategories.length;

        mSubcategoriesSelectedIds = new Integer[length];
        for (int i = 0; i < length; i++) {
            final int subId = list.get(subcategories[i]).getId();
            mSubcategoriesSelectedIds[i] = subId;
        }
    }

    public Integer[] getSelectedSubcategories() {
        return mSubcategoriesSelected;
    }

    public Integer[] getSelectedSubcategoriesIds() {
        return mSubcategoriesSelectedIds;
    }

    public String getLogoUrl() {
        return mAvatarUrl;
    }

    public boolean isParent() {
        return mId % 100 == 0;
    }

    public boolean in(int id) {
        if (!isParent()) return false;
        return id > mId && id < (mId + 100);
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
