package com.github.sasd97.upitter.utils;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 20.05.2016.
 */

public final class ListUtils {

    public interface OnListInteractionListener<T> {
        boolean isFit(T other);
    }

    public interface OnListMutateListener<T, M> {
        M mutate(T object);
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

    public static <T> T select(@NonNull List<T> list, @NonNull OnListInteractionListener<T> listener) {
        if (list == null) return null;

        for (T item: list)
            if (listener.isFit(item))
                return item;
        return null;
    }
    
    public static <T> List<T> filter(@NonNull List<T> list, @NonNull OnListInteractionListener<T> listener) {
        if (list == null) return null;

        List<T> result = new ArrayList<>();
        for (T item: list)
            if (listener.isFit(item))
                result.add(item);
        return result;
    }

    public static <T, M> List<M> mutate(@NonNull List<T> list, @NonNull OnListMutateListener<T, M> listener) {
        List<M> result = new ArrayList<>();
        for (T item: list)
            result.add(listener.mutate(item));
        return result;
    }
}
