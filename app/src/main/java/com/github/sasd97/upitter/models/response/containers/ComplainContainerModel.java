package com.github.sasd97.upitter.models.response.containers;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.pointers.ComplaintPointerModel;

import java.util.List;

/**
 * Created by alexander on 26.09.16.
 */

public class ComplainContainerModel extends BaseResponseModel<List<ComplaintPointerModel>> {

    public List<ComplaintPointerModel> getComplains() {
        return mResponse;
    }
}
