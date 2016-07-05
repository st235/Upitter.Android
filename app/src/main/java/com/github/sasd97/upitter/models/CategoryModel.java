package com.github.sasd97.upitter.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.github.sasd97.upitter.models.skeletons.SearchableSkeleton;

/**
 * Created by alexander on 05.07.16.
 */
public class CategoryModel extends SearchableSkeleton {

    private final static String DRAWABLE_PREFIX = "ic_category_";
    private final static String DRAWABLE_ID = "drawable";

    private String mId;
    private String mTitle;
    private int mDrawableResource;

    private CategoryModel(Builder builder) {
        mId = builder.id;
        mTitle = builder.title;
        mDrawableResource = builder.drawableResource;
    }

    private CategoryModel(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mDrawableResource = in.readInt();
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getDrawable() {
        return mDrawableResource;
    }

    @Override
    public String getPreview() {
        return mTitle.substring(0, 1);
    }

    @Override
    public String getSubTitle() {
        return null;
    }

    @Override
    public IMAGE_TYPE getImageType() {
        return IMAGE_TYPE.INT;
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public boolean isImage() {
        return false;
    }

    @Override
    public String getStringImage() {
        return null;
    }

    @Override
    public Integer getIntImage() {
        return mDrawableResource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mTitle);
        parcel.writeInt(mDrawableResource);
    }


    public static class Builder {

        private String id;
        private String title;
        private int drawableResource;

        public Builder id(@NonNull String id) {
            this.id = id;
            return this;
        }

        public Builder title(@NonNull String title) {
            this.title = title;
            return this;
        }

        public Builder drawableResource(@NonNull Context context) {
            String resourceName = new StringBuilder(DRAWABLE_PREFIX).append(id).toString();
            this.drawableResource = context.getResources().getIdentifier(resourceName, DRAWABLE_ID, context.getPackageName());
            return this;
        }

        public CategoryModel build() {
            return new CategoryModel(this);
        }
    }

    public final static Parcelable.Creator<CategoryModel> CREATOR = new Parcelable.Creator<CategoryModel>() {

        @Override
        public CategoryModel createFromParcel(Parcel parcel) {
            return new CategoryModel(parcel);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };
}
