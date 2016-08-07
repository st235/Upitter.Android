package com.github.sasd97.upitter.services;

import android.util.Log;

import com.github.sasd97.upitter.events.OnQueueListener;
import com.github.sasd97.upitter.models.CategoryModel;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.models.response.fileServer.FileResponseModel;
import com.github.sasd97.upitter.models.response.fileServer.ImageResponseModel;
import com.github.sasd97.upitter.services.query.PostQueryService;
import com.github.sasd97.upitter.utils.ListUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.github.sasd97.upitter.constants.PostCreateConstants.TITLE_MIN_LENGTH;
import static com.github.sasd97.upitter.constants.PostCreateConstants.DESCRIPTION_MIN_LENGTH;

/**
 * Created by alexander on 12.07.16.
 */

public class PostBuilderService implements OnQueueListener<List<FileResponseModel>> {

    private static final String TAG = "Post builder";

    private enum Type { SIMPLE_POST, POST_WITH_QUIZ, POST_WITH_IMAGES, COMPLEX_POST }
    public enum Missing { MISSING_LOCATION, SHORT_TITLE, SHORT_DESCRIPTION }

    public interface OnPostBuilderListener {
        void onBuild();
        void onPrepare();
        void onPrepareError();
        void onPublicationError();
        void onValidationError(List<Missing> missing);
    }

    private Type postType;

    private String uid;

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
    private List<FileResponseModel> photos;
    private ArrayList<String> rawPhotos;

    private String accessToken;

    private PostQueryService queryService;
    private OnPostBuilderListener listener;

    private PostBuilderService(OnPostBuilderListener listener, PostQueryService queryService) {
        this.listener = listener;
        this.queryService = queryService;
    }

    public static PostBuilderService getBuilder(OnPostBuilderListener listener, PostQueryService queryService) {
        return new PostBuilderService(listener, queryService);
    }

    public PostBuilderService uid(String uid) {
        this.uid = uid;
        return this;
    }

    public PostBuilderService title(String title) {
        this.title = title;
        return this;
    }

    public PostBuilderService text(String text) {
        this.text = text;
        return this;
    }

    public PostBuilderService category(CategoryModel category) {
        this.category = category.getId();
        return this;
    }

    public PostBuilderService coordinates(CoordinatesModel coordinates) {
        this.latitude = coordinates.getLatitude();
        this.longitude = coordinates.getLongitude();
        return this;
    }

    public PostBuilderService quiz(ArrayList<String> quiz) {
        this.quiz = quiz;
        return this;
    }

    public PostBuilderService rawPhotos(ArrayList<String> rawPhotos) {
        this.rawPhotos = rawPhotos;
        return this;
    }

    public ArrayList<String> getQuiz() {
        return quiz;
    }

    public void build(String accessToken) {
        if (!validateForms()) {
            listener.onValidationError(obtainMissing());
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
        Logger.d(toJson());

        queryService
                .createPost(accessToken,
                        RestService.obtainJsonRaw(toJson()));
    }

    private void preparePhotos(ArrayList<String> photos) {
        ImagesUploadQueue
                .executeQueue(uid, this,
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
        if (title.length() < TITLE_MIN_LENGTH) result = false;
        if (text.length() < DESCRIPTION_MIN_LENGTH) result = false;
        return result;
    }

    private List<Missing> obtainMissing() {
        List<Missing> missing = new ArrayList<>();
        if (latitude == null || longitude == null) missing.add(Missing.MISSING_LOCATION);
        if (title.length() < TITLE_MIN_LENGTH) missing.add(Missing.SHORT_TITLE);
        if (text.length() < DESCRIPTION_MIN_LENGTH) missing.add(Missing.SHORT_DESCRIPTION);
        return missing;
    }

    @Override
    public void onQueueCompete(List<FileResponseModel> photos) {
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
