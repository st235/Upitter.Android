package com.github.sasd97.upitter.models;

import android.support.annotation.NonNull;

import java.util.Locale;

/**
 * Created by alexander on 04.07.16.
 */
public class ErrorModel {

    private int mCode = -1;
    private String mUrl;
    private String mMessage = "Error holder";

    private ErrorModel(Builder builder) {
        mUrl = builder.url;
        mCode = builder.code;
        mMessage = builder.message;
    }

    public int getCode() {
        return mCode;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getMessage() {
        return mMessage;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Error:\nCode: [%1$d]\nMessage: [%2$s]\nURL: {%3$s}",
                mCode,
                mMessage == null ? "Null" : mMessage,
                mUrl == null ? "Null" : mUrl);
    }

    public static class Builder {

        private int code;
        private String url;
        private String message;

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder url(@NonNull String url) {
            this.url = url;
            return this;
        }


        public Builder message(@NonNull String message) {
            this.message = message;
            return this;
        }

        public ErrorModel build() {
            return new ErrorModel(this);
        }
    }
}
