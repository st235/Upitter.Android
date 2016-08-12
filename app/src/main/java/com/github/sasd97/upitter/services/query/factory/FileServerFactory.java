package com.github.sasd97.upitter.services.query.factory;

import com.github.sasd97.upitter.components.ImageUploadRequestBody;
import com.github.sasd97.upitter.models.response.fileServer.FileResponseModel;
import com.github.sasd97.upitter.models.response.fileServer.MediaResponseModel;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */
public interface FileServerFactory {

    String FILE_MULTIPART_SCHEMA = "image\"; filename=\"1\"";

    @Multipart
    @POST("upload/image")
    Call<MediaResponseModel> uploadImage(@Part("id") RequestBody id,
                                         @Part("type") RequestBody type,
                                         @Part("purpose") RequestBody purpose,
                                         @PartMap() HashMap<String, RequestBody> body);

    @Multipart
    @POST("upload/avatar")
    Call<MediaResponseModel> uploadAvatar(@Part("id") RequestBody id,
                                          @PartMap() HashMap<String, RequestBody> body);

    @Multipart
    @POST("upload/post_image")
    Call<FileResponseModel> uploadPostImage(@Part("id") RequestBody id,
                                            @Part(FILE_MULTIPART_SCHEMA) ImageUploadRequestBody image);
}
