package com.github.sasd97.upitter.services.query;

import com.github.sasd97.upitter.models.response.categories.CategoryResponseModel;
import com.github.sasd97.upitter.models.response.categories.CatergoriesResponseModel;
import com.github.sasd97.upitter.services.RestService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexander on 27.06.16.
 */
public class BusinessRegistrationQueryService {

    public interface OnBusinessRegistrationListener {
        void onGetCategories(List<CategoryResponseModel> categories);
        void onError();
    }

    private OnBusinessRegistrationListener listener;

    private BusinessRegistrationQueryService(OnBusinessRegistrationListener listener) {
        this.listener = listener;
    }

    public static BusinessRegistrationQueryService getService(OnBusinessRegistrationListener listener) {
        return new BusinessRegistrationQueryService(listener);
    }

    public void getCategories() {
        Call<CatergoriesResponseModel> getCategories = RestService.baseFactory().getCategories();
        getCategories.enqueue(new Callback<CatergoriesResponseModel>() {
            @Override
            public void onResponse(Call<CatergoriesResponseModel> call, Response<CatergoriesResponseModel> response) {
                if (response.body().isError()) {
                    listener.onError();
                    return;
                }

                listener.onGetCategories(response.body().getCategories());
            }

            @Override
            public void onFailure(Call<CatergoriesResponseModel> call, Throwable t) {
                t.printStackTrace();
                listener.onError();
            }
        });
    }
}
