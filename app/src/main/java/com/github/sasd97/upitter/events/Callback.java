package com.github.sasd97.upitter.events;

import com.github.sasd97.upitter.services.RestService;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Alexadner Dadukin on 24.07.2016.
 */
public abstract class Callback<T> implements retrofit2.Callback<T> {

    private OnErrorQueryListener listener;

    public Callback(OnErrorQueryListener listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        RestService.logRequest(call);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        RestService.logRequest(call);
        t.printStackTrace();
        listener.onError(RestService.getEmptyError());
    }
}
