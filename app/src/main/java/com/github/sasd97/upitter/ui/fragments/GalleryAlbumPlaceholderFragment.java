package com.github.sasd97.upitter.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.utils.Names;

import uk.co.senab.photoview.PhotoView;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.PATH_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.POSITION_ATTACH;

/**
 * Created by Alexadner Dadukin on 26.06.2016.
 */

public class GalleryAlbumPlaceholderFragment extends BaseFragment {

    private PhotoView detailView = null;

    public GalleryAlbumPlaceholderFragment() {
        super(R.layout.gallery_album_preview_fragment);
    }

    public static GalleryAlbumPlaceholderFragment getFragment(int position, String imagePath) {
        GalleryAlbumPlaceholderFragment fragment = new GalleryAlbumPlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION_ATTACH, position);
        args.putString(PATH_ATTACH, imagePath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void bindViews() {
        detailView = findById(R.id.detail_image);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(getActivity())
                .load(Names
                        .getInstance()
                        .getFilePath(getArguments().getString(PATH_ATTACH))
                        .toString())
                .fitCenter()
                .into(detailView);
    }
}