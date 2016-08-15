package com.github.sasd97.upitter.models.response;

/**
 * Created by alexander on 23.06.16.
 */

public class SimpleResponseModel extends BaseResponseModel<Object> {

    @Override
    public String toString() {
        return getResponseInfo();
    }
}
