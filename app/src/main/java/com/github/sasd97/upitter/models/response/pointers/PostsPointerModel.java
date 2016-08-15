package com.github.sasd97.upitter.models.response.pointers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

/**
 * Created by Alexadner Dadukin on 09.07.2016.
 */

public class PostsPointerModel {

    @SerializedName("posts")
    @Expose
    private List<PostPointerModel> mPosts;


    public List<PostPointerModel> getPosts() {
        return mPosts;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (PostPointerModel post: mPosts) builder.append(post.toString()).append("\n\n");
        return String.format(Locale.getDefault(), "Posts: %1$s\n", builder.toString());
    }
}
