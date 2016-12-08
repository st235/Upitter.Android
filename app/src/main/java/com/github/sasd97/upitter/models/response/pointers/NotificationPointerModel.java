package com.github.sasd97.upitter.models.response.pointers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alexadner Dadukin on 11/29/2016.
 */

public class NotificationPointerModel {

    @SerializedName("customId")
    @Expose
    private String customId;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("author")
    @Expose
    private PlainUserPointerModel author;

    @SerializedName("createdDate")
    @Expose
    private String createdDate;

    @SerializedName("postId")
    @Expose
    private String postId;

    public String getCustomId() {
        return customId;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public PlainUserPointerModel getAuthor() {
        return author;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getPostId() {
        return postId;
    }

    @Override
    public String toString() {
        return "NotificationPointerModel{" +
                "customId='" + customId + '\'' +
                ", type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", authorId='" + author.toString() + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}
