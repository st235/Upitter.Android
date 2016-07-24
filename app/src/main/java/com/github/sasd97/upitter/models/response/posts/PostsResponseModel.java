package com.github.sasd97.upitter.models.response.posts;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

/**
 * Created by Alexadner Dadukin on 09.07.2016.
 */

public class PostsResponseModel extends BaseResponseModel<PostsResponseModel> {

    @SerializedName("posts")
    @Expose
    private List<PostResponseModel> mPosts;


    public List<PostResponseModel> getPosts() {
        return mPosts;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (PostResponseModel post: mPosts) builder.append(post.toString()).append("\n\n");
        return String.format(Locale.getDefault(), "Posts: %1$s\n", builder.toString());
    }
}
