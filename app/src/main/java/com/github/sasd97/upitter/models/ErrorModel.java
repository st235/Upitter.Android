package com.github.sasd97.upitter.models;

import android.support.annotation.NonNull;

import java.util.Locale;

/**
 * Created by alexander on 04.07.16.
 */
public class ErrorModel {

    private int mCode = -1;
    private String mMessage = "Error holder";

    private ErrorModel(Builder builder) {
        mCode = builder.code;
        mMessage = builder.message;
    }

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Error:\nCode: [%1$d]\nMessage: [%2$s]",
                mCode,
                mMessage);
    }

    public static class Builder {

        private int code;
        private String message;

        public Builder code(int code) {
            this.code = code;
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
