package com.github.sasd97.upitter.services.query.factory;

import com.github.sasd97.upitter.models.response.fileServer.UploadAvatarResponseModel;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */
public interface FileServerFactory {

    @Multipart
    @POST("upload/image")
    Call<UploadAvatarResponseModel> uploadImage(@Part("id") RequestBody id,
                                                @Part("type") RequestBody type,
                                                @Part("purpose") RequestBody purpose,
                                                @PartMap() HashMap<String, RequestBody> body);

    @Multipart
    @POST("upload/avatar")
    Call<UploadAvatarResponseModel> uploadAvatar(@Part("id") RequestBody id,
                                                @PartMap() HashMap<String, RequestBody> body);
}
