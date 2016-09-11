package com.github.sasd97.upitter.models.response.containers;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.pointers.ActivityPointerModel;

import java.util.List;

/**
 * Created by Alexadner Dadukin on 11.09.2016.
 */

public class ActivitiesContainerModel extends BaseResponseModel<List<ActivityPointerModel>> {

    public List<ActivityPointerModel> getTitles() {
        return mResponse;
    }
}
