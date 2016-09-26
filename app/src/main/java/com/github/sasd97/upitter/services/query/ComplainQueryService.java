package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.SimpleResponseModel;
import com.github.sasd97.upitter.models.response.containers.ComplainContainerModel;
import com.github.sasd97.upitter.models.response.pointers.ComplaintPointerModel;
import com.github.sasd97.upitter.services.RestService;
import com.orhanobut.logger.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

/**
 * Created by alexander on 26.09.16.
 */

public class ComplainQueryService {

    public interface ComplainListener extends OnErrorQueryListener {
        void onObtainComplainList(List<ComplaintPointerModel> list);
        void onComplainSuccess();
        void onComplainError();
    }

    private ComplainListener listener;

    private ComplainQueryService(@NonNull ComplainListener listener) {
        this.listener = listener;
    }

    public static ComplainQueryService getService(@NonNull ComplainListener listener) {
        return new ComplainQueryService(listener);
    }

    public void obtainComplainList(@NonNull String accessToken) {
        Call<ComplainContainerModel> obtainComplainList =
                RestService
                .baseFactory()
                .obtainComplainList(accessToken, language());

        obtainComplainList.enqueue(new Callback<ComplainContainerModel>(listener) {
            @Override
            public void onResponse(Call<ComplainContainerModel> call, Response<ComplainContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onObtainComplainList(response.body().getComplains());
            }
        });
    }

    public void createComplain(@NonNull String accessToken,
                               @NonNull String targetId,
                               @NonNull String reasonId) {
        Logger.e("%s, %s", targetId, reasonId);

        Call<SimpleResponseModel> obtainComplainList =
                RestService
                        .baseFactory()
                        .createComplain(accessToken, language(), reasonId, targetId);

        obtainComplainList.enqueue(new Callback<SimpleResponseModel>(listener) {
            @Override
            public void onResponse(Call<SimpleResponseModel> call, Response<SimpleResponseModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                if (response.isSuccessful()) listener.onComplainSuccess();
                else listener.onComplainError();
            }
        });
    }
}
