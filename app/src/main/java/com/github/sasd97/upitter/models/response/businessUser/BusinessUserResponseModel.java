package com.github.sasd97.upitter.models.response.businessUser;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexander on 28.06.16.
 */
public class BusinessUserResponseModel extends BaseResponseModel {

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("activity")
    @Expose
    private String mName;

    @SerializedName("phone")
    @Expose
    private String mName;

    @SerializedName("site")
    @Expose
    private String mName;

    @SerializedName("coordina")
    @Expose
    private String mName;
}
