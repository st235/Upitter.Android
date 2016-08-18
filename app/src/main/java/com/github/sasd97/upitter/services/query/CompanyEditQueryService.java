package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.containers.CompanyContainerModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.models.skeletons.RequestSkeleton;
import com.github.sasd97.upitter.services.RestService;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

/**
 * Created by alexander on 18.08.16.
 */

public class CompanyEditQueryService {

    public interface OnEditListener extends OnErrorQueryListener {
        void onSuccess(CompanyPointerModel company);
    }

    private OnEditListener listener;

    private CompanyEditQueryService(@NonNull OnEditListener listener) {
        this.listener = listener;
    }

    public static CompanyEditQueryService getService(@NonNull OnEditListener listener) {
        return new CompanyEditQueryService(listener);
    }

    public void edit(@NonNull String accessToken,
                     @NonNull RequestSkeleton company) {
        RequestBody requestBody = RestService.obtainJsonRaw(company.toJson());
        Call<CompanyContainerModel> edit = RestService
                .baseFactory()
                .editCompany(language(), accessToken, requestBody);

        edit.enqueue(new Callback<CompanyContainerModel>(listener) {
            @Override
            public void onResponse(Call<CompanyContainerModel> call, Response<CompanyContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onSuccess(response.body().getCompany());
            }
        });
    }
}
