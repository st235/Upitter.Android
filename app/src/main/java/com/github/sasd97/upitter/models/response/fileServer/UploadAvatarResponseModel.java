package com.github.sasd97.upitter.models.response.fileServer;

import com.github.sasd97.upitter.models.response.BaseResponseModel;

/**
 * Created by alexander on 04.07.16.
 */

public class UploadAvatarResponseModel extends BaseResponseModel<ImageResponseModel> {

    public ImageResponseModel getImageModel() {
        return mResponse;
    }

    @Override
    public String toString() {
        return getResponseInfo();
    }
}
