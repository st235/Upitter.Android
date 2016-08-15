package com.github.sasd97.upitter.services.query.factory;

import com.github.sasd97.upitter.components.ImageUploadRequestBody;
import com.github.sasd97.upitter.models.response.containers.FileContainerModel;
import com.github.sasd97.upitter.models.response.containers.MediaContainerModel;
import com.github.sasd97.upitter.models.response.pointers.FilePointerModel;
import com.github.sasd97.upitter.models.response.pointers.MediaPointerModel;

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
    Call<MediaContainerModel> uploadImage(@Part("id") RequestBody id,
                                          @Part("type") RequestBody type,
                                          @Part("purpose") RequestBody purpose,
                                          @PartMap() HashMap<String, RequestBody> body);

    @Multipart
    @POST("upload/avatar")
    Call<MediaContainerModel> uploadAvatar(@Part("id") RequestBody id,
                                         @PartMap() HashMap<String, RequestBody> body);

    @Multipart
    @POST("upload/post_image")
    Call<FileContainerModel> uploadPostImage(@Part("id") RequestBody id,
                                             @Part(FILE_MULTIPART_SCHEMA) ImageUploadRequestBody image);
}
