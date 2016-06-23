package com.github.sasd97.upitter.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by Alexadner Dadukin on 23.06.2016.
 */
public class SmsModel implements Parcelable {

    private String mAuthor;
    private String mBody;

    private SmsModel(Builder builder) {
        mAuthor = builder.author;
        mBody = builder.body;
    }

    protected SmsModel(Parcel in) {
        mAuthor = in.readString();
        mBody = in.readString();
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getBody() {
        return mBody;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "SMS from %1$s\n%2$s",
                mAuthor,
                mBody);
    }

    public static class Builder {

        private String author;
        private String body;

        public Builder author(@NonNull String author) {
            this.author = author;
            return this;
        }

        public Builder body(@NonNull String body) {
            this.body = body;
            return this;
        }

        public SmsModel build() {
            return new SmsModel(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAuthor);
        parcel.writeString(mBody);
    }

    public static final Creator<SmsModel> CREATOR = new Creator<SmsModel>() {
        @Override
        public SmsModel createFromParcel(Parcel in) {
            return new SmsModel(in);
        }

        @Override
        public SmsModel[] newArray(int size) {
            return new SmsModel[size];
        }
    };
}
