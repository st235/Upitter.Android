package com.github.sasd97.upitter.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.response.posts.PostsResponseModel;
import com.github.sasd97.upitter.services.query.PostQueryService;
import com.github.sasd97.upitter.ui.base.BaseFragment;

import java.util.List;
import java.util.Locale;

import static com.github.sasd97.upitter.Upitter.*;

/**
 * Created by alexander on 08.07.16.
 */

public class TapeFragment extends BaseFragment
        implements PostQueryService.OnPostListener {

    private PostQueryService queryService;

    public TapeFragment() {
        super(R.layout.tape_fragment);
    }

    public static TapeFragment getFragment() {
        return new TapeFragment();
    }

    @Override
    protected void bindViews() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        queryService = PostQueryService.getService(this);
        queryService.obtainPosts(getHolder().get().getAccessToken(), Locale.getDefault().getLanguage(),
                20, null);
    }


    @Override
    public void onPostObtained(List<PostsResponseModel> posts) {
        for (PostsResponseModel post: posts) Log.d("TAPE_FRAGMENT", post.toString());
    }

    @Override
    public void onError(ErrorModel error) {
        Log.d("TAPE_FRAGMENT", error.toString());
    }
}
