package com.github.sasd97.upitter.utils;

import android.content.Context;
import android.content.Intent;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.FolderModel;
import com.github.sasd97.upitter.ui.results.GalleryResult;

import java.util.ArrayList;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.*;

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

    public static class Builder {

        private Context from = null;
        private Class<?> to = GalleryResult.class;
        private boolean multiSelectionMode = false;
        private int selectionMaxCounter = 1;

        public Builder from(Context from) {
            this.from = from;
            return this;
        }

        public Builder multiSelectionMode(boolean multiSelectionMode) {
            this.multiSelectionMode = multiSelectionMode;
            return this;
        }

        public Builder selectionMaxCounter(int selectionMaxCounter) {
            this.selectionMaxCounter = selectionMaxCounter;
            return this;
        }

        public Intent build() {
            Intent galleryIntent = new Intent(from, to);
            galleryIntent.putExtra(GALLERY_MULTI_SELECTION_MODE, this.multiSelectionMode);
            galleryIntent.putExtra(GALLERY_MULTI_SELECT_ITEMS_AMOUNT, this.selectionMaxCounter);
            return galleryIntent;
        }
    }
}
