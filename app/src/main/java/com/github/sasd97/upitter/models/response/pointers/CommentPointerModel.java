package com.github.sasd97.upitter.models.response.pointers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Locale;

/**
 * Created by alexander on 04.09.16.
 */
public class CommentPointerModel {

    @SerializedName("customId")
    @Expose
    private String mCustomId;

    @SerializedName("text")
    @Expose
    private String mText;

    @SerializedName("author")
    @Expose
    private PlainUserPointerModel mAuthor;

    @SerializedName("createdDate")
    @Expose
    private String mCreatedDate;

    public String getId() {
        return mCustomId;
    }

    public String getText() {
        return mText;
    }

    public PlainUserPointerModel getAuthor() {
        return mAuthor;
    }

    public String getCreationDate() {
        return mCreatedDate;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Comment #%1$s\nAuthor: %2$s\n%3$s\nCreation date: %4$s",
                mCustomId,
                mAuthor == null ? "Null" : mAuthor.toString(),
                mAuthor,
                mCreatedDate);
    }
}
