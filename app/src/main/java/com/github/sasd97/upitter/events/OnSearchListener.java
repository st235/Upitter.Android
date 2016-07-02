package com.github.sasd97.upitter.events;

import android.net.Uri;
import java.io.File;
import java.util.ArrayList;

import com.github.sasd97.upitter.models.FolderModel;

/**
 * Created by Alexander Dadukin on 31.01.2016.
 */

public interface OnSearchListener {
    void onSearched(ArrayList<String> list);
    void onSearchError();

    void onFoldersParsed(ArrayList<FolderModel> folders);
    boolean onCompare(File file);

    Uri getMediaType();
    String[] getProjection();
}
