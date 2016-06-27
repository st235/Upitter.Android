package com.github.sasd97.upitter.services.query.factory;

import com.github.sasd97.upitter.models.response.authorization.AuthorizationRequestCodeResponseModel;
import com.github.sasd97.upitter.models.response.authorization.AuthorizationResponseModel;
import com.github.sasd97.upitter.models.response.SimpleResponseModel;
import com.github.sasd97.upitter.models.response.categories.CatergoriesResponseModel;
import com.github.sasd97.upitter.models.response.report.ReportResponseModel;

import org.json.JSONObject;

import retrofit2.Call;
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
    Call<AuthorizationResponseModel> authorizeWithTwitter(@Field("token") String token, @Field("secret") String secret);

    @FormUrlEncoded
    @POST("support/android/{id}")
    Call<ReportResponseModel> sendCrashReport(@Path("id") String id, @Field("log") JSONObject trace);

    @POST("/authorization/phone/set/{number}/{countryCode}")
    Call<SimpleResponseModel> obtainRequestCode(@Path("number") String number, @Path("countryCode") String countryCode);

    @FormUrlEncoded
    @POST("/authorization/phone/verify/{number}/{countryCode}")
    Call<AuthorizationRequestCodeResponseModel> sendRequestCode(@Path("number") String number, @Path("countryCode") String countryCode, @Field("code") String requestCode);

    @GET("/categories")
    Call<CatergoriesResponseModel> getCategories(@Query("ln") String language);
}
