package com.github.sasd97.upitter.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.utils.Names;

import uk.co.senab.photoview.PhotoView;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.PATH_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.POSITION_ATTACH;

/**
 * Created by Alexadner Dadukin on 26.06.2016.
 */
public class GalleryAlbumPlaceholderFragment extends Fragment {

    private PhotoView detailView = null;

    public GalleryAlbumPlaceholderFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gallery_album_preview_fragment, container, false);
        detailView = (PhotoView) rootView.findViewById(R.id.detail_image);

        Glide.with(getActivity())
                .load(Names
                        .getInstance()
                        .getFilePath(getArguments().getString(PATH_ATTACH))
                        .toString())
                .fitCenter()
                .into(detailView);

        return rootView;
    }
}