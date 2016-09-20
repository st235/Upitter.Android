package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.containers.UserContainerModel;
import com.github.sasd97.upitter.models.response.pointers.UserPointerModel;
import com.github.sasd97.upitter.services.RestService;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

/**
 * Created by Alexadner Dadukin on 20.09.2016.
 */

public class UserSettingsQueryService {

    public interface OnSettingsListener extends OnErrorQueryListener {
        void onChangedAvatar(String path);
        void onChangedUser(UserPointerModel user);
    }

    private OnSettingsListener listener;

    private UserSettingsQueryService(@NonNull OnSettingsListener listener) {
        this.listener = listener;
    }

    public static UserSettingsQueryService getService(@NonNull OnSettingsListener listener) {
        return new UserSettingsQueryService(listener);
    }

    public void changeAvatar(@NonNull String accessToken,
                             @NonNull String avatarUrl) {
        Call<UserContainerModel> changeAvatar = RestService
                .baseFactory()
                .editUserLogo(accessToken, language(), avatarUrl);

        changeAvatar.enqueue(new Callback<UserContainerModel>(listener) {
            @Override
            public void onResponse(Call<UserContainerModel> call, Response<UserContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onChangedAvatar(response.body().getUser().getAvatarUrl());
            }
        });
    }

    public void edit(@NonNull String accessToken,
                     @NonNull String name,
                     @NonNull String surname,
                     @NonNull String nickname) {
        Call<UserContainerModel> edit = RestService
                .baseFactory()
                .editUser(accessToken, language(), name, surname, nickname);

        edit.enqueue(new Callback<UserContainerModel>(listener) {
            @Override
            public void onResponse(Call<UserContainerModel> call, Response<UserContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onChangedUser(response.body().getUser());
            }
        });
    }
}
