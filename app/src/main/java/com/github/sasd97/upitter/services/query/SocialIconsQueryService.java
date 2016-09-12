package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.containers.SocialIconContainerModel;
import com.github.sasd97.upitter.models.response.pointers.SocialIconPointerModel;
import com.github.sasd97.upitter.services.RestService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

/**
 * Created by Alexadner Dadukin on 15.08.2016.
 */

public class SocialIconsQueryService {

    public interface OnSocialIconsListener extends OnErrorQueryListener {
        void onObtainSocialIcons(List<SocialIconPointerModel> icons);
    }

    private OnSocialIconsListener listener;

    private SocialIconsQueryService(OnSocialIconsListener listener) {
        this.listener = listener;
    }

    public static SocialIconsQueryService getService(@NonNull OnSocialIconsListener listener) {
        return new SocialIconsQueryService(listener);
    }

    public void obtainIconsList(@NonNull String accessToken) {
        Call<SocialIconContainerModel> obtainIcons = RestService
                .baseFactory()
                .obtainSocialIcons(accessToken, language());

        obtainIcons.enqueue(new Callback<SocialIconContainerModel>(listener) {
            @Override
            public void onResponse(Call<SocialIconContainerModel> call, Response<SocialIconContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onObtainSocialIcons(response.body().getIconsList());
            }
        });
    }
}
