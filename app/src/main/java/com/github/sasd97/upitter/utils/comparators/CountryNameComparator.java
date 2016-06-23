package com.github.sasd97.upitter.utils.comparators;

import com.github.sasd97.upitter.models.CountryModel;

import java.util.Comparator;

/**
 * Created by Alexander Dadukin on 11.06.2016.
 */
public final class CountryNameComparator implements Comparator<CountryModel> {

    @Override
    public int compare(CountryModel lhs, CountryModel rhs) {
        return lhs.getName().compareToIgnoreCase(rhs.getName());
    }
}
