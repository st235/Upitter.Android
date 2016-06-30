package com.github.sasd97.upitter.models.request;

import com.google.gson.Gson;

/**
 * Created by alexander on 30.06.16.
 */
public class CoordinatesRequestModel extends BaseRequestModel {

    private float latitude;
    private float longitude;

    public CoordinatesRequestModel setLatitude(float latitude) {
        this.latitude = latitude;
        return this;
    }

    public CoordinatesRequestModel setLongitude(float longitude) {
        this.longitude = longitude;
        return this;
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
