package com.github.sasd97.upitter.events;

import com.github.sasd97.upitter.models.ErrorModel;

/**
 * Created by alexander on 04.07.16.
 */
public interface OnErrorQueryListener {
    void onError(ErrorModel error);
}
