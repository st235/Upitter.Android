package com.github.sasd97.upitter.utils;

import android.util.Log;

import com.github.sasd97.upitter.events.OnQueueListener;
import com.github.sasd97.upitter.models.CategoryModel;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.models.response.fileServer.ImageResponseModel;
import com.github.sasd97.upitter.services.ImagesUploadQueue;
import com.github.sasd97.upitter.services.RestService;
import com.github.sasd97.upitter.services.query.PostQueryService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by alexander on 12.07.16.
 */

public class PostBuilder implements OnQueueListener<List<ImageResponseModel>> {

    private static final String TAG = "Post builder";

    enum Type { SIMPLE_POST, POST_WITH_QUIZ, POST_WITH_IMAGES, COMPLEX_POST }

    public interface OnPostBuilderListener {
        void onPrepare();
        void onBuild(); //  FIXME: add onPrepare method to lock ui before post
        void onPublicationError();
        void onPrepareError();
    }

    private Type postType;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("latitude")
    @Expose
    private Double latitude;

    @SerializedName("longitude")
    @Expose
    private Double longitude;

    @SerializedName("variants")
    @Expose
    private ArrayList<String> quiz;

    @SerializedName("images")
    @Expose
    private List<ImageResponseModel> photos;
    private ArrayList<String> rawPhotos;

    private String accessToken;

    private PostQueryService queryService;
    private OnPostBuilderListener listener;

    private PostBuilder(OnPostBuilderListener listener, PostQueryService queryService) {
        this.listener = listener;
        this.queryService = queryService;
    }

    public static PostBuilder getBuilder(OnPostBuilderListener listener, PostQueryService queryService) {
        return new PostBuilder(listener, queryService);
    }

    public PostBuilder title(String title) {
        this.title = title;
        return this;
    }

    public PostBuilder text(String text) {
        this.text = text;
        return this;
    }

    public PostBuilder category(CategoryModel category) {
        this.category = category.getId();
        return this;
    }

    public PostBuilder coordinates(CoordinatesModel coordinates) {
        this.latitude = coordinates.getLatitude();
        this.longitude = coordinates.getLongitude();
        return this;
    }

    public PostBuilder quiz(ArrayList<String> quiz) {
        this.quiz = quiz;
        return this;
    }

    public PostBuilder rawPhotos(ArrayList<String> rawPhotos) {
        this.rawPhotos = rawPhotos;
        return this;
    }

    public ArrayList<String> getQuiz() {
        return quiz;
    }

    public void build(String accessToken) {
        if (!validateForms()) {
            listener.onPrepareError();
            return;
        }
        listener.onPrepare();
        this.accessToken = accessToken;

        Log.d(TAG, identityType().toString());

        switch (identityType()) {
            case SIMPLE_POST:
                complete();
                break;
            case POST_WITH_QUIZ:
                complete();
                break;
            case POST_WITH_IMAGES:
                preparePhotos(rawPhotos);
                break;
            case COMPLEX_POST:
                preparePhotos(rawPhotos);
                break;
        }
    }

    private void complete() {
        Log.d(TAG, toJson());
        final String language = Locale.getDefault().getLanguage();
        queryService
                .createPost(accessToken,
                        language,
                        RestService.obtainJsonRaw(toJson()));
    }

    private void preparePhotos(ArrayList<String> photos) {
        ImagesUploadQueue
                .executeQueue(this,
                        ListUtils.toArray(String.class, photos));
    }

    private Type identityType() {
        if (postType != null) return postType;
        if (ListUtils.isUndefined(quiz) && ListUtils.isUndefined(rawPhotos)) {
            postType = Type.SIMPLE_POST;
            return postType;
        }
        if (ListUtils.isUndefined(quiz)) {
            postType = Type.POST_WITH_IMAGES;
            return postType;
        }
        if (ListUtils.isUndefined(rawPhotos)) {
            postType = Type.POST_WITH_QUIZ;
            return postType;
        }
        postType = Type.COMPLEX_POST;
        return postType;
    }

    private boolean validateForms() {
        boolean result = true;
        if (latitude == null || longitude == null) result = false;
        if (category == null) result = false;
        return result;
    }

    @Override
    public void onQueueCompete(List<ImageResponseModel> photos) {
        this.photos = photos;
        complete();
    }

    @Override
    public void onQueueError() {
        listener.onPrepareError();
    }


    private String toJson() {
        Gson builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return builder.toJson(this);
    }
}
