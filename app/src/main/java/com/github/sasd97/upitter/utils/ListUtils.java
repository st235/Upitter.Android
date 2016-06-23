package com.github.sasd97.upitter.utils;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by Alex on 20.05.2016.
 */

public final class ListUtils {

    public interface OnSelectListener<T>{
        boolean isSelectable(T other);
    }

    private ListUtils() {}

    public static <T> T[] obtainArray(Class<T> type, @NonNull List<T> list) {
        T[] result = (T[]) Array.newInstance(type, list.size());
        result = list.toArray(result);
        return result;
    }

    public static <T> T[] toArray(@NonNull Class<T> type, @NonNull JSONArray jsonArray) throws JSONException {
        int jsonLength = jsonArray.length();
        T[] result = (T[]) Array.newInstance(type, jsonLength);

        if (jsonLength == 1) {
            result[0] = (T) jsonArray.get(0);
            return result;
        }

        for (int i = 0; i < jsonLength; i++)
            result[i] = (T) jsonArray.get(i);

        return result;
    }

    public static <T> T select(@NonNull List<T> list, @NonNull OnSelectListener<T> listener) {
        for (T item: list)
            if (listener.isSelectable(item))
                return item;
        return null;
    }
}
