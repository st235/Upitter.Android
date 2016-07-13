package com.github.sasd97.upitter.services.query.factory;

import com.github.sasd97.upitter.models.response.authorization.AuthorizationCompanyResponseModel;
import com.github.sasd97.upitter.models.response.authorization.AuthorizationRequestCodeResponseModel;
import com.github.sasd97.upitter.models.response.authorization.AuthorizationResponseModel;
import com.github.sasd97.upitter.models.response.SimpleResponseModel;
import com.github.sasd97.upitter.models.response.categories.CatergoriesResponseModel;
import com.github.sasd97.upitter.models.response.posts.PostResponseModel;
import com.github.sasd97.upitter.models.response.posts.PostsResponseModel;
import com.github.sasd97.upitter.models.response.report.ReportResponseModel;

import org.json.JSONObject;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */
public interface BaseFactory {

    @FormUrlEncoded
    @POST("authorization/google/verify")
    Call<AuthorizationResponseModel> authorizeWithGooglePlus(@Field("tokenId") String googleTokenId);

    @FormUrlEncoded
    @POST("authorization/facebook/verify")
    Call<AuthorizationResponseModel> authorizeWithFacebook(@Field("accessToken") String accessToken);

    @FormUrlEncoded
    @POST("authorization/twitter/verify")
    Call<AuthorizationResponseModel> authorizeWithTwitter(@Field("token") String token,
                                                          @Field("secret") String secret);

    @POST("authorization/phone/set/{number}/{countryCode}")
    Call<SimpleResponseModel> obtainRequestCode(@Path("number") String number,
                                                @Path("countryCode") String countryCode);

    @FormUrlEncoded
    @POST("authorization/phone/verify/{number}/{countryCode}")
    Call<AuthorizationRequestCodeResponseModel> sendRequestCode(@Path("number") String number,
                                                                @Path("countryCode") String countryCode,
                                                                @Field("code") String requestCode);

    @POST("authorization/phone/add_info/{number}/{countryCode}")
    Call<AuthorizationCompanyResponseModel> registerCompany(@Path("number") String number,
                                                            @Path("countryCode") String countryCode,
                                                            @Body RequestBody businessUser);

    @FormUrlEncoded
    @POST("support/android/{id}")
    Call<ReportResponseModel> sendCrashReport(@Path("id") String id,
                                              @Query("ln") String language,
                                              @Query("accessToken") String accessToken,
                                              @Field("log") JSONObject trace);

    @GET("categories")
    Call<CatergoriesResponseModel> getCategories(@Query("ln") String language,
                                                 @Query("accessToken") String accessToken);

    @FormUrlEncoded
    @POST("post/create")
    Call<PostResponseModel> createPost(@Query("ln") String language,
                                              @Query("accessToken") String accessToken,
                                              @Field("title") String title,
                                              @Field("text") String text,
                                              @Field("latitude") double latitude,
                                              @Field("longitude") double longitude,
                                              @Field("category") String postCategory);

    @FormUrlEncoded
    @POST("post/create")
    Call<PostResponseModel> createPostWithQuiz(@Query("ln") String language,
                                               @Query("accessToken") String accessToken,
                                               @Field("title") String title,
                                               @Field("text") String text,
                                               @Field("latitude") double latitude,
                                               @Field("longitude") double longitude,
                                               @Field("category") String postCategory,
                                               @Field("variants") List<String> variants);

    @FormUrlEncoded
    @POST("post/create")
    Call<PostResponseModel> createPostWithImages(@Query("ln") String language,
                                                 @Query("accessToken") String accessToken,
                                                 @Field("title") String title,
                                                 @Field("text") String text,
                                                 @Field("latitude") double latitude,
                                                 @Field("longitude") double longitude,
                                                 @Field("category") String postCategory,
                                                 @Field("images") List<String> images);

    @FormUrlEncoded
    @POST("post/create")
    Call<PostResponseModel> createPostComplex(@Query("ln") String language,
                                              @Query("accessToken") String accessToken,
                                              @Field("title") String title,
                                              @Field("text") String text,
                                              @Field("latitude") double latitude,
                                              @Field("longitude") double longitude,
                                              @Field("category") String postCategory,
                                              @Field("images") List<String> variants,
                                              @Field("variants") List<String> avatars);

    @GET("post/obtain")
    Call<PostsResponseModel> obtainPosts(@Query("ln") String language,
                                         @Query("accessToken") String accessToken,
                                         @Query("latitude") double latitude,
                                         @Query("longitude") double longitude,
                                         @Query("radius") int radius,
                                         @Query("limit") int limit,
                                         @Query("offset") Integer offset);

    @GET("post/like/{postId}")
    Call<PostResponseModel> likePost(@Path("postId") String postId,
                                     @Query("ln") String language,
                                     @Query("accessToken") String accessToken);

    @GET("post/favorite/{postId}")
    Call<PostResponseModel> addPostToFavorite(@Path("postId") String postId,
                                              @Query("ln") String language,
                                              @Query("accessToken") String accessToken);

    @GET("/post/vote/{postId}/{voteVariant}")
    Call<PostResponseModel> voteInPost(@Path("postId") String postId,
                                       @Path("voteVariant") int voteVariant,
                                       @Query("ln") String language,
                                       @Query("accessToken") String accessToken);
}
