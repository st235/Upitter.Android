package com.github.sasd97.upitter.utils;

import android.content.Context;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.FolderModel;

import java.util.ArrayList;


/**
 * Created by Alex on 07.02.2016.
 */
public class Gallery {

    private static String ALL_NAME;
    private static final String ALL_PATH = "*";

    private Gallery() {}

    public static FolderModel createAllFolder(Context context, ArrayList<FolderModel> folders) {
        int allAmount = 0;
        ALL_NAME = context.getString(R.string.spinner_all_photos_gallery_activity);
        int randomPreview = (int)(Math.random() * folders.size());

        FolderModel.Builder all = new FolderModel
                .Builder()
                .path(ALL_PATH);
        all.preview(folders.get(randomPreview).getPreview());

        for (FolderModel model: folders) {
            allAmount += model.getAmount();
        }

        all.amount(allAmount);
        all.name(ALL_NAME);
        return all.build();
    }
}
