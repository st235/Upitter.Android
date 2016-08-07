package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.company.CompanyResponseModel;
import com.github.sasd97.upitter.services.RestService;

import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

/**
 * Created by Alexadner Dadukin on 06.08.2016.
 */
public class CompanyQueryService {

    public interface OnCompanyListener extends OnErrorQueryListener {
        void onAvatarChanged(String path);
    }

    private OnCompanyListener listener;

    private CompanyQueryService(OnCompanyListener listener) {
        this.listener = listener;
    }

    public static CompanyQueryService getService(OnCompanyListener listener) {
        return new CompanyQueryService(listener);
    }

    public void changeAvatar(@NonNull String accessToken,
                             @NonNull final String logoUrl) {
        Call<CompanyResponseModel> changeAvatar = RestService
                .baseFactory()
                .editCompanyLogo(accessToken,
                        language(),
                        logoUrl);

        changeAvatar.enqueue(new Callback<CompanyResponseModel>(listener) {
            @Override
            public void onResponse(Call<CompanyResponseModel> call, Response<CompanyResponseModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onAvatarChanged(logoUrl);
            }
        });
    }
}
