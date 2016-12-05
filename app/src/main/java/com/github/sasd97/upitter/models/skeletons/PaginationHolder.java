package com.github.sasd97.upitter.models.skeletons;

import com.github.sasd97.upitter.models.CoordinatesModel;

import java.util.List;

/**
 * Created by Alexadner Dadukin on 12/5/2016.
 */

public interface PaginationHolder {
    void redraw(List<CoordinatesModel> list);
}
