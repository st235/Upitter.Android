package com.github.sasd97.upitter.models.request;

import com.github.sasd97.upitter.models.PhoneModel;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by alexander on 30.06.16.
 */
public class RegisterBusinessUserRequestModel {

    private String name;
    private String temporaryToken;
    private String site;
    private int category;
    private List<CoordinatesRequestModel> coordinates;

    private RegisterBusinessUserRequestModel() {}

    public static RegisterBusinessUserRequestModel getRequest() {
        return new RegisterBusinessUserRequestModel();
    }

    public RegisterBusinessUserRequestModel name(String name) {
        this.name = name;
        return this;
    }

    public RegisterBusinessUserRequestModel temporaryToken(String temporaryToken) {
        this.temporaryToken = temporaryToken;
        return this;
    }

    public RegisterBusinessUserRequestModel site(String site) {
        this.site = site;
        return this;
    }

    public RegisterBusinessUserRequestModel category(int category) {
        this.category = category;
        return this;
    }

    public RegisterBusinessUserRequestModel coordinates(List<CoordinatesRequestModel> coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }
}
