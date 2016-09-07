package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.containers.CompanyContainerModel;
import com.github.sasd97.upitter.models.response.containers.PostsContainerModel;
import com.github.sasd97.upitter.models.response.containers.SubscribersContainerModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.models.response.pointers.PostPointerModel;
import com.github.sasd97.upitter.services.RestService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

/**
 * Created by alexander on 01.09.16.
 */

public class CompanyProfileQueryService {

    public interface OnCompanySearchListener extends OnErrorQueryListener {
        void onFind(CompanyPointerModel company);
        void onObtainPosts(PostsContainerModel posts);
        void onSubscribersObtained();
    }

    private OnCompanySearchListener listener;

    private CompanyProfileQueryService(@NonNull OnCompanySearchListener listener) {
        this.listener = listener;
    }

    public static CompanyProfileQueryService getService(@NonNull OnCompanySearchListener listener) {
        return new CompanyProfileQueryService(listener);
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

    public void obtainPosts(@NonNull String accessToken,
                            @NonNull String alias) {

        Call<PostsContainerModel> obtainPosts = RestService
                .baseFactory()
                .obtainPostsByAlias(language(), accessToken, alias);

        obtainPosts.enqueue(new Callback<PostsContainerModel>(listener) {
            @Override
            public void onResponse(Call<PostsContainerModel> call, Response<PostsContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onObtainPosts(response.body());
            }
        });
    }

    public void obtainSubscribers(@NonNull String accessToken,
                                  @NonNull String companyId) {
        Call<SubscribersContainerModel> obtainSubscribers = RestService
                .baseFactory()
                .obtainSubscribers(language(), accessToken, companyId);

        obtainSubscribers.enqueue(new Callback<SubscribersContainerModel>(listener) {
            @Override
            public void onResponse(Call<SubscribersContainerModel> call, Response<SubscribersContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onSubscribersObtained();
            }
        });
    }
}
