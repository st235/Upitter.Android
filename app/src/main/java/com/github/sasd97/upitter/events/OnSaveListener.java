package com.github.sasd97.upitter.events;

import java.util.ArrayList;

/**
 * Created by Alex on 09.02.2016.
 */

public interface OnSaveListener {
    void onSave(ArrayList<String> paths);
    void onSaveError();
}
