package com.github.sasd97.upitter.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.orm.SugarRecord;

import java.util.Locale;

/**
 * Created by alexander on 24.06.16.
 */
public class PhoneModel extends SugarRecord implements Parcelable {

    private String mDialCode;
    private String mPhoneBody;

    public PhoneModel() {}

    private PhoneModel(Builder builder) {
        mDialCode = builder.dialCode;
        mPhoneBody = builder.phoneBody;
    }

    private PhoneModel(Parcel in) {
        mDialCode = in.readString();
        mPhoneBody = in.readString();
    }

    public String getDialCode() {
        return mDialCode;
    }

    public String getPhoneBody() {
        return mPhoneBody;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mDialCode);
        parcel.writeString(mPhoneBody);
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Dial code %1$s, body %2$s",
                mDialCode,
                mPhoneBody);
    }

    public static class Builder {

        private String dialCode;
        private String phoneBody;

        public Builder dialCode(@NonNull String dialCode) {
            this.dialCode = dialCode;
            return this;
        }

        public Builder phoneBody(@NonNull String phoneBody) {
            this.phoneBody = phoneBody;
            return this;
        }

        public PhoneModel build() {
            return new PhoneModel(this);
        }
    }

    public final static Parcelable.Creator<PhoneModel> CREATOR = new Parcelable.Creator<PhoneModel>() {

        @Override
        public PhoneModel createFromParcel(Parcel parcel) {
            return new PhoneModel(parcel);
        }

        @Override
        public PhoneModel[] newArray(int size) {
            return new PhoneModel[size];
        }
    };
}
