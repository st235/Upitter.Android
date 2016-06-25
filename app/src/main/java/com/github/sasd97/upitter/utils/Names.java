package com.github.sasd97.upitter.utils;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by Alex on 28.01.2016.
 */

public final class Names {
    
    private StringBuilder builder = null;

    private static final String COMA = ", ";
    private static final String POINT = ". ";
    private static final String SPACE = " ";
    private static final String SLASH = "/";
    private static final String START = "*";

    private static final String SINGE_POINT = ".";

    private final String FILE_PATH = "file://";

    private final int FIRST_POSITION = 0;

    private Names() {
        builder = new StringBuilder();
    }

    public static Names getInstance() {
        return new Names();
    }

    public static boolean inFolder(@NonNull String folderName, @NonNull String fileName) {
        if (folderName.equals(START)) return true;
        if (!fileName.contains(folderName) || folderName.length() == fileName.length()) return false;
        String relativelyName = fileName.substring(folderName.length() + 1);
        if (relativelyName.contains(SLASH)) return false;
        return true;
    }

    public static String getUniqueFileName(@NonNull String extension) {
        return String.format("shop_cover_%1$d.%2$s", (new Date()).getTime(), extension);
    }

    public static String getFileExtension(@NonNull String file) {
        return file.substring(file.lastIndexOf(SINGE_POINT) + 1);
    }

    public Names getShortName(@NonNull String firstName, @NonNull String secondName) {
        builder
            .append(firstName.charAt(FIRST_POSITION))
            .append(POINT)
            .append(secondName);
        return this;
    }

    public Names getName(@NonNull String firstName, @NonNull String secondName) {
        builder
            .append(firstName)
            .append(SPACE)
            .append(secondName);
        return this;
    }

    public Names getLocationName(@NonNull String... places) {
        for(String place: places) {
            builder.append(place).append(COMA);
        }

        builder = deleteLastComa(builder);
        return this;
    }

    public Names getFilePath(@NonNull String path) {
        final String REVERSED_FILE_PATH = new StringBuilder(FILE_PATH).reverse().toString();
        builder.append(path).reverse().append(REVERSED_FILE_PATH).reverse();
        return this;
    }

    public Names getFolderName(@NonNull String folderPath) {
        final int lastDividerPosition = (folderPath.lastIndexOf('/') + 1);
        builder.append(folderPath.substring(lastDividerPosition));
        return this;
    }

    private StringBuilder deleteLastComa(@NonNull StringBuilder builder) {
        final int length = builder.length();
        return builder.delete(length-2, length);
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
