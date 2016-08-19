package com.github.sasd97.upitter.services.query.factory;

import com.github.sasd97.upitter.models.response.containers.AuthorizationCompanyContainerModel;
import com.github.sasd97.upitter.models.response.containers.AuthorizationRequestCodeContainerModel;
import com.github.sasd97.upitter.models.response.containers.AuthorizationContainerModel;
import com.github.sasd97.upitter.models.response.SimpleResponseModel;
import com.github.sasd97.upitter.models.response.containers.CategoriesContainerModel;
import com.github.sasd97.upitter.models.response.containers.CompanyContainerModel;
import com.github.sasd97.upitter.models.response.containers.PostContainerModel;
import com.github.sasd97.upitter.models.response.containers.PostsContainerModel;
import com.github.sasd97.upitter.models.response.containers.SocialIconContainerModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.models.response.pointers.PostPointerModel;
import com.github.sasd97.upitter.models.response.pointers.PostsPointerModel;
import com.github.sasd97.upitter.models.response.containers.ReportContainerModel;

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
    Call<AuthorizationContainerModel> authorizeWithGooglePlus(@Field("tokenId") String googleTokenId);

    @FormUrlEncoded
    @POST("authorization/facebook/verify")
    Call<AuthorizationContainerModel> authorizeWithFacebook(@Field("accessToken") String accessToken);

    @FormUrlEncoded
    @POST("authorization/twitter/verify")
    Call<AuthorizationContainerModel> authorizeWithTwitter(@Field("token") String token,
                                                           @Field("secret") String secret);

    @POST("authorization/phone/set/{number}/{countryCode}")
    Call<SimpleResponseModel> obtainRequestCode(@Path("number") String number,
                                                @Path("countryCode") String countryCode);

    @FormUrlEncoded
    @POST("authorization/phone/verify/{number}/{countryCode}")
    Call<AuthorizationRequestCodeContainerModel> sendRequestCode(@Path("number") String number,
                                                                 @Path("countryCode") String countryCode,
                                                                 @Field("code") String requestCode);


    @FormUrlEncoded
    @POST("debug/authorization/phone/verify_development/{number}/{countryCode}")
    Call<AuthorizationRequestCodeContainerModel> debugRequestCode(@Path("number") String number,
                                                                  @Path("countryCode") String countryCode,
                                                                  @Field("code") String requestCode);

    @POST("authorization/phone/add_info/{number}/{countryCode}")
    Call<AuthorizationCompanyContainerModel> registerCompany(@Path("number") String number,
                                                             @Path("countryCode") String countryCode,
                                                             @Body RequestBody businessUser);

    @FormUrlEncoded
    @POST("support/android/{id}")
    Call<ReportContainerModel> sendCrashReport(@Path("id") String id,
                                               @Query("ln") String language,
                                               @Query("accessToken") String accessToken,
                                               @Field("log") JSONObject trace);

    @GET("categories")
    Call<CategoriesContainerModel> getCategories(@Query("ln") String language,
                                                 @Query("accessToken") String accessToken);

    @POST("post/create")
    Call<PostContainerModel> createPost(@Query("ln") String language,
                                        @Query("accessToken") String accessToken,
                                        @Body RequestBody post);

    @GET("post/obtain")
    Call<PostsContainerModel> obtainPosts(@Query("ln") String language,
                                          @Query("accessToken") String accessToken,
                                          @Query("latitude") double latitude,
                                          @Query("longitude") double longitude,
                                          @Query("radius") int radius,
                                          @Query("activity[]") List<Integer> categories);

    @GET("post/obtainNew")
    Call<PostsContainerModel> obtainNewPosts(@Query("ln") String language,
                                           @Query("accessToken") String accessToken,
                                           @Query("latitude") double latitude,
                                           @Query("longitude") double longitude,
                                           @Query("radius") int radius,
                                           @Query("postId") String postId,
                                           @Query("activity[]") List<Integer> categories);

    @GET("post/obtainOld")
    Call<PostsContainerModel> obtainOldPosts(@Query("ln") String language,
                                           @Query("accessToken") String accessToken,
                                           @Query("latitude") double latitude,
                                           @Query("longitude") double longitude,
                                           @Query("radius") int radius,
                                           @Query("postId") String postId,
                                           @Query("activity[]") List<Integer> categories);

    @GET("post/like/{postId}")
    Call<PostContainerModel> likePost(@Path("postId") String postId,
                                    @Query("ln") String language,
                                    @Query("accessToken") String accessToken);

    @GET("post/favorite/{postId}")
    Call<PostContainerModel> addPostToFavorite(@Path("postId") String postId,
                                             @Query("ln") String language,
                                             @Query("accessToken") String accessToken);

    @GET("/post/vote/{postId}/{voteVariant}")
    Call<PostContainerModel> voteInPost(@Path("postId") String postId,
                                      @Path("voteVariant") int voteVariant,
                                      @Query("ln") String language,
                                      @Query("accessToken") String accessToken);

    @FormUrlEncoded
    @POST("company/edit")
    Call<CompanyContainerModel> editCompanyLogo(@Field("accessToken") String accessToken,
                                                @Field("ln") String language,
                                                @Field("logoUrl") String logoUrl);

    @GET("post/watch/{postId}")
    Call<PostContainerModel> watchPost(@Path("postId") String postId,
                                     @Query("accessToken") String accessToken,
                                     @Query("ln") String language);

    @GET("post/find/{postId}")
    Call<PostContainerModel> findPostById(@Path("postId") String postId,
                                        @Query("accessToken") String accessToken,
                                        @Query("ln") String language);

    @GET("general/socialInfo/get")
    Call<SocialIconContainerModel> obtainSocialIcons(@Query("accessToken") String accessToken,
                                                @Query("ln") String language);

    @POST("company/edit")
    Call<CompanyContainerModel> editCompany(@Query("ln") String language,
                                        @Query("accessToken") String accessToken,
                                        @Body RequestBody company);

    @GET("post/favorites")
    Call<PostsContainerModel> obtainFavorites(@Query("ln") String language,
                                              @Query("accessToken") String accessToken);
}
