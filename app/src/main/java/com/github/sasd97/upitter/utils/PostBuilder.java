package com.github.sasd97.upitter.utils;

import android.util.Log;

import com.github.sasd97.upitter.events.OnQueueListener;
import com.github.sasd97.upitter.models.CategoryModel;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.services.ImagesUploadQueue;
import com.github.sasd97.upitter.services.query.PostQueryService;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by alexander on 12.07.16.
 */

public class PostBuilder implements OnQueueListener {

    enum Type { SIMPLE_POST, POST_WITH_QUIZ, POST_WITH_IMAGES, COMPLEX_POST }

    public interface OnPostBuilderListener {
        void onBuild();
        void onPublicationError();
        void onPrepareError();
    }

    private Type postType;

    private String title;
    private String text;
    private CategoryModel category;
    private CoordinatesModel coordinates;
    private ArrayList<String> quiz;
    private ArrayList<String> rawPhotos;
    private ArrayList<String> photos;

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
        this.category = category;
        return this;
    }

    public PostBuilder coordinates(CoordinatesModel coordinates) {
        this.coordinates = coordinates;
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
        this.accessToken = accessToken;

        Log.d("POST_TYPE", identityType().toString());

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
        final String language = Locale.getDefault().getLanguage();

        switch (identityType()) {
            case SIMPLE_POST:
                        queryService
                        .createPost(accessToken,
                                    language,
                                    title,
                                    text,
                                    category.getId(),
                                    coordinates.getLatitude(),
                                    coordinates.getLongitude());
                break;
            case POST_WITH_QUIZ:
                queryService
                        .createPostWithQuiz(accessToken,
                                language,
                                title,
                                text,
                                category.getId(),
                                quiz,
                                coordinates.getLatitude(),
                                coordinates.getLongitude());
                break;
            case POST_WITH_IMAGES:
                queryService
                        .createPostWithImages(accessToken,
                                language,
                                title,
                                text,
                                category.getId(),
                                photos,
                                coordinates.getLatitude(),
                                coordinates.getLongitude());
                break;
            case COMPLEX_POST:
                queryService
                        .createPostComplex(accessToken,
                                language,
                                title,
                                text,
                                category.getId(),
                                quiz,
                                photos,
                                coordinates.getLatitude(),
                                coordinates.getLongitude());
                break;
        }
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
        if (coordinates == null) result = false;
        if (category == null) result = false;
        return result;
    }

    @Override
    public <T> void onQueueCompete(T list) {
        photos = (ArrayList<String>) list;
        complete();
    }

    @Override
    public void onQueueError() {
        listener.onPrepareError();
    }
}
