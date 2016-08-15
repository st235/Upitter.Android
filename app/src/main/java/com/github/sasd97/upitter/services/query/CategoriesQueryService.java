package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.pointers.CategoryPointerModel;
import com.github.sasd97.upitter.models.response.containers.CategoriesContainerModel;
import com.github.sasd97.upitter.services.RestService;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by alexander on 27.06.16.
 */

public class CategoriesQueryService {

    public interface OnCategoryListener extends OnErrorQueryListener {
        void onGetCategories(List<CategoryPointerModel> categories);
    }

    private OnCategoryListener listener;

    private CategoriesQueryService(OnCategoryListener listener) {
        this.listener = listener;
    }

    public static CategoriesQueryService getService(OnCategoryListener listener) {
        return new CategoriesQueryService(listener);
    }

    public void getCategories(@NonNull String accessToken) {
        Call<CategoriesContainerModel> getCategories = RestService
                .baseFactory()
                .getCategories(Locale.getDefault().getLanguage(), accessToken);

        getCategories.enqueue(new Callback<CategoriesContainerModel>(listener) {
            @Override
            public void onResponse(Call<CategoriesContainerModel> call, Response<CategoriesContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onGetCategories(response.body().getCategories());
            }
        });
    }
}
