package com.github.sasd97.upitter.models;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.skeletons.HeaderSkeleton;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Alex on 09.06.2016.
 */

public class CountryModel implements HeaderSkeleton {

    public enum RegionType {
        AFRICA(R.color.africa_region),
        OCEANIA(R.color.oceania_region),
        ASIA(R.color.asia_region),
        EUROPE(R.color.europe_region),
        AMERICAS(R.color.americas_region);

        private int colorResId;

        private RegionType(int colorCode) {
            this.colorResId = colorCode;
        }

        public int getColorResId() {
            return colorResId;
        }

        public static RegionType getType(@NonNull String region) {
            if (region.equalsIgnoreCase("Africa")) return AFRICA;
            if (region.equalsIgnoreCase("Oceania")) return OCEANIA;
            if (region.equalsIgnoreCase("Asia")) return ASIA;
            if (region.equalsIgnoreCase("Europe")) return EUROPE;
            return AMERICAS;
        }
    }

    private String mName;
    private String mNativeName;
    private String mRegion;
    private String mDialCode;
    private String mCode;
    private String mHeader;
    private boolean mIsHeader = false;
    private int mSectionManager;
    private int mSectionFirstPosition;

    private CountryModel(Builder builder) {
        mName = builder.name;
        mNativeName = builder.nativeName;
        mRegion = builder.region;
        mDialCode = builder.dialCode;
        mCode = builder.code;
        mHeader = mName.substring(0, 1);
    }

    public String getName() {
        return mName;
    }

    public String getNativeName() {
        return mNativeName;
    }

    public String getDialCode() {
        return mDialCode;
    }

    public String getCode() {
        return mCode;
    }

    public String getRegion() {
        return mRegion;
    }

    @Override
    public String getHeader() {
        return mHeader;
    }

    @Override
    public boolean isHeader() {
        return mIsHeader;
    }

    @Override
    public void setIsHeader(boolean isHeader) {
        mIsHeader = isHeader;
    }

    @Override
    public int getSectionManager() {
        return mSectionManager;
    }

    @Override
    public void setSectionManager(int manager) {
        mSectionManager = manager;
    }

    @Override
    public int getSectionFirstPosition() {
        return mSectionFirstPosition;
    }

    @Override
    public void setSectionFirstPosition(int position) {
        mSectionFirstPosition = position;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "[Country: Name %1$s (%2$s), Native: %3$s, Region: %4$s, Code: %5$s]",
                mName,
                mCode,
                mNativeName,
                mRegion,
                mDialCode);
    }

    public static class Builder {

        private String name;
        private String nativeName;
        private String region;
        private String dialCode;
        private String code;

        public Builder name(@NonNull String name) {
            this.name = name;
            return this;
        }

        public Builder nativeName(@NonNull String nativeName) {
            this.nativeName = nativeName;
            return this;
        }

        public Builder region(@NonNull String region) {
            this.region = region;
            return this;
        }

        public Builder dialCode(@NonNull String dialCode) {
            this.dialCode = dialCode;
            return this;
        }

        public Builder code(@NonNull String code) {
            this.code = code;
            return this;
        }

        public CountryModel build() {
            return new CountryModel(this);
        }
    }
}
