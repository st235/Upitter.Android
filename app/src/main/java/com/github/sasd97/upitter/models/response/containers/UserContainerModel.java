package com.github.sasd97.upitter.models.response.containers;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.pointers.UserPointerModel;

/**
 * Created by Alexadner Dadukin on 20.09.2016.
 */

public class UserContainerModel extends BaseResponseModel<UserPointerModel> {

    public UserPointerModel getUser() {
        return mResponse;
    }
}
