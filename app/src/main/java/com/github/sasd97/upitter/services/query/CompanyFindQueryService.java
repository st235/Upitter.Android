package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.containers.CompanyContainerModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.services.RestService;

import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

/**
 * Created by alexander on 01.09.16.
 */

public class CompanyFindQueryService {

    public interface OnCompanySearchListener extends OnErrorQueryListener {
        void onFind(CompanyPointerModel company);
    }

    private OnCompanySearchListener listener;

    private CompanyFindQueryService(@NonNull OnCompanySearchListener listener) {
        this.listener = listener;
    }

    public static CompanyFindQueryService getService(@NonNull OnCompanySearchListener listener) {
        return new CompanyFindQueryService(listener);
    }

    public void findByAlias(@NonNull String accessToken,
                            @NonNull String alias) {
        Call<CompanyContainerModel> findByAlias = RestService
                .baseFactory()
                .findCompanyByAlias(alias, language(), accessToken);

        findByAlias.enqueue(new Callback<CompanyContainerModel>(listener) {
            @Override
            public void onResponse(Call<CompanyContainerModel> call, Response<CompanyContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onFind(response.body().getCompany());
            }
        });
    }
}
