package com.github.sasd97.upitter.utils.comparators;

import java.io.File;
import java.util.Comparator;

/**
 * Created by Alex on 08.02.2016.
 */

public final class FileComparator implements Comparator<File> {

    @Override
    public int compare(File current, File next) {
       Long currentData = current.lastModified();
       Long nextData = next.lastModified();
        return nextData.compareTo(currentData);
    }
}
