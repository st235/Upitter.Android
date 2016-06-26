package com.github.sasd97.upitter.models.skeletons;

/**
 * Created by Alex on 18.05.2016.
 */
public class ImageSkeleton implements CheckableSkeleton {

    private String mImagePath;
    private boolean mIsChecked = false;

    private int mListPosition = -1;
    private int mMultiSelectCounter = -1;

    private ImageSkeleton(Builder builder) {
        mIsChecked = builder.checked;
        mImagePath = builder.path;
    }

    public String getPath() {
        return mImagePath;
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void setCheck(boolean checkState) {
        mIsChecked = checkState;
    }

    public void setListPosition(int position) {
        mListPosition = position;
    }

    public int getListPosition() {
        return mListPosition;
    }

    public void setMultiSelectCounter(int selectPosition) {
        mMultiSelectCounter = selectPosition;
    }

    public int getMultiSelectCounter() {
        return mMultiSelectCounter;
    }

    @Override
    public String toString() {
        return String.format("Path: %1$s\n Checked: %2$b", mImagePath, mIsChecked);
    }

    public static class Builder {

        private boolean checked;
        private String path;

        public Builder checked(boolean checked) {
            this.checked = checked;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public ImageSkeleton build() {
            return new ImageSkeleton(this);
        }
    }
}
