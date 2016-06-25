package com.github.sasd97.upitter.models;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.utils.Names;

import java.util.Locale;

/**
 * Created by Alex on 07.02.2016.
 */

public class FolderModel {

    private String mPath;
    private String mName;
    private String mPreview;
    private Integer mAmount;

    private FolderModel(Builder builder) {
        mPath = builder.path;
        mAmount = builder.amount;
        mName = builder.name;
        mPreview = builder.preview;
    }

    public String getPath() {
        return mPath;
    }

    public String getName() {
        return mName;
    }

    public Integer getAmount() {
        return mAmount;
    }

    public String getPreview() {
        return Names
                .getInstance()
                .getFilePath(mPreview).toString();
    }

    public boolean isEmpty() {
        return mAmount == 0;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Folder %1$s in %2$s with %3$d items (%4$s)",
                mName,
                mPath,
                mAmount,
                mPreview);
    }

    public static class Builder {

        private String path;
        private String name;
        private String preview;
        private Integer amount;

        public Builder path(@NonNull String path) {
            this.path = path;
            this.name = Names
                    .getInstance()
                    .getFolderName(path)
                    .toString();
            return this;
        }

        public Builder name(@NonNull String name) {
            this.name = name;
            return this;
        }

        public Builder preview(@NonNull String preview) {
            this.preview = preview;
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public FolderModel build() {
            return new FolderModel(this);
        }
    }
}
