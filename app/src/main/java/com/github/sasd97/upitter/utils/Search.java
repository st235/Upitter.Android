package com.github.sasd97.upitter.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.github.sasd97.upitter.events.OnSearchListener;
import com.github.sasd97.upitter.models.FolderModel;
import com.github.sasd97.upitter.utils.comparators.FileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Alexander Dadukin on 31.01.2016.
 */

public class Search extends AsyncTask<Void, Void, ArrayList<String>> {

    private ContentResolver contentResolver;
    private OnSearchListener searchListener;

    private static Search searchUtils;

    private Search(ContentResolver contentResolver, OnSearchListener searchListener) {
        this.searchListener = searchListener;
        this.contentResolver = contentResolver;
    }

    public static <T extends AppCompatActivity & OnSearchListener> void search(T context) {
        search(context, context);
    }

    public static void search(Context context, OnSearchListener listener) {
        searchUtils = new Search(context.getContentResolver(), listener);
        searchUtils.execute();
    }

    protected ArrayList<String> findFolder() {
        Cursor cursor = null;
        SortedSet<String> dirSet = new TreeSet<>();
        ArrayList<String> dirPaths = null;

        Uri mediaUri = searchListener.getMediaType();
        String[] projection = searchListener.getProjection();

        if (mediaUri != null) cursor = contentResolver.query(mediaUri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String temporaryDir = cursor.getString(0);
                temporaryDir = temporaryDir.substring(0, temporaryDir.lastIndexOf('/'));
                dirSet.add(temporaryDir);
            } while (cursor.moveToNext());
            dirPaths = new ArrayList<>(dirSet);
        }

        if(cursor != null) cursor.close();
        return dirPaths;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        ArrayList<String> filePaths = new ArrayList<>();
        ArrayList<File> files = new ArrayList<>();
        ArrayList<FolderModel> models = new ArrayList<>();

        ArrayList<String> cats = findFolder();

        if (cats == null) return null;

        for(String path: cats) {
            final File searched = new File(path);
            final FolderModel.Builder folderBuilder = new FolderModel
                    .Builder()
                    .path(path);

            File[] searchedList = searched.listFiles();
            int folderAmount = 0;

            if (searchedList == null) continue;

            for(File file: searchedList) {
                if(file.isDirectory()) searchedList = file.listFiles();
                if(searchListener.onCompare(file)) {
                    folderAmount++;
                    if(folderAmount == 1) folderBuilder.preview(file.getAbsolutePath());
                    files.add(file);
                }
            }

            folderBuilder.amount(folderAmount);
            if (folderAmount != 0) models.add(folderBuilder.build());
        }

        Collections.sort(files, new FileComparator());

        for (File file: files) filePaths.add(file.getAbsolutePath());

        searchListener.onFoldersParsed(models);
        return filePaths;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);

        if(strings == null) searchListener.onSearchError();
        else searchListener.onSearched(strings);
    }
}
