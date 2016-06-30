package com.github.sasd97.upitter.models.request;

/**
 * Created by alexander on 30.06.16.
 */
public class CoordinatesRequestModel {

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
}
