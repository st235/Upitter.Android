package com.github.sasd97.upitter.services;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.Upitter;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.SimpleErrorResponseModel;
import com.github.sasd97.upitter.services.query.factory.BaseFactory;
import com.github.sasd97.upitter.services.query.factory.FileServerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.github.sasd97.upitter.constants.MethodConstants.BASE_API_URL;
import static com.github.sasd97.upitter.constants.MethodConstants.BASE_SUB_API_URL;
import static com.github.sasd97.upitter.constants.MethodConstants.BASE_SUB_ASTRAL_API_URL;
import static com.github.sasd97.upitter.constants.MethodConstants.FILE_SERVER_API_URL;
import static com.github.sasd97.upitter.constants.MethodConstants.FILE_SUB_ASTRAL_SERVER_API_URL;
import static com.github.sasd97.upitter.constants.MethodConstants.FILE_SUB_SERVER_API_URL;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */

public final class RestService {

    private static final String IMAGE_MEDIATYPE = "image/*";
    private static final String TEXT_MEDIATYPE = "text/plain";
    private static final String JSON_MEDIATYPE = "application/json";

    private static final String FILE_MULTIPART_SCHEMA = "image\"; filename=\"%1$s";

    private static Retrofit baseAPI;
    private static Retrofit fileServerAPI;

    private static BaseFactory baseFactory;
    private static FileServerFactory fileServerFactory;

    private RestService() {}

    public static void init() {
        baseAPI = new Retrofit.Builder()
                .baseUrl(BASE_SUB_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        fileServerAPI = new Retrofit.Builder()
                .baseUrl(FILE_SUB_SERVER_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        baseFactory = baseAPI.create(BaseFactory.class);
        fileServerFactory = fileServerAPI.create(FileServerFactory.class);
    }

    public static BaseFactory baseFactory() {
        return baseFactory;
    }

    public static FileServerFactory fileServerFactory() {
        return fileServerFactory;
    }

    public static HashMap<String, RequestBody> obtainImageMultipart(File image) {
        HashMap<String, RequestBody> map = new HashMap<>();
        RequestBody photoBody = RequestBody.create(MediaType.parse(IMAGE_MEDIATYPE), image);
        map.put(String.format(Locale.ENGLISH, FILE_MULTIPART_SCHEMA, image.getName()), photoBody);
        return map;
    }

    public static RequestBody obtainTextMultipart(String text) {
        return RequestBody.create(MediaType.parse(TEXT_MEDIATYPE), text);
    }

    public static RequestBody obtainJsonRaw(@NonNull String jsonRaw) {
        return RequestBody.create(MediaType.parse(JSON_MEDIATYPE), jsonRaw);
    }

    public static SimpleErrorResponseModel parseError(Response<?> response) {
        Converter<ResponseBody, SimpleErrorResponseModel> converter =
                baseAPI.responseBodyConverter(SimpleErrorResponseModel.class, new Annotation[0]);
        SimpleErrorResponseModel error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new SimpleErrorResponseModel();
        }

        return error;
    }

    public static boolean handleError(@NonNull Call<?> call,
                                      @NonNull Response<? extends BaseResponseModel> response,
                                      @NonNull OnErrorQueryListener listener) {
        if (response.isSuccessful()) {
            if (!response.body().isError()) return true;
            listener.onError(response.body().getError(call.request().url().toString()));
            return false;
        }

        SimpleErrorResponseModel error = parseError(response);
        listener.onError(error.getError(call.request().url().toString()));
        return false;
    }

    public static ErrorModel getEmptyError() {
        return new ErrorModel.Builder()
                .code(500)
                .message("Unhandled error")
                .build();
    }

    public static void logRequest() {
        //  TODO: make log
    }
}
