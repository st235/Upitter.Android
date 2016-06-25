package com.github.sasd97.upitter.utils.mutators;

import com.github.sasd97.upitter.models.skeletons.ImageSkeleton;

import java.util.ArrayList;

/**
 * Created by Alex on 18.05.2016.
 */
public final class PhotoMutator {

    private PhotoMutator() {}

    public static ArrayList<ImageSkeleton> mutate(ArrayList<String> paths) {
        ArrayList<ImageSkeleton> result = new ArrayList<>();

        for (String path: paths)
            result.add(new ImageSkeleton.Builder().path(path).build());

        return result;
    }

    public static ArrayList<String> backMutate(ArrayList<ImageSkeleton> photos) {
        ArrayList<String> result = new ArrayList<>();
        for (ImageSkeleton photo: photos) result.add(photo.getPath());
        return result;
    }
}
