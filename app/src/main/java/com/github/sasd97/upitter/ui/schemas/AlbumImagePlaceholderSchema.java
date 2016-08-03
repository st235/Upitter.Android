package com.github.sasd97.upitter.ui.schemas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.constants.GalleryConstants;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.utils.Names;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.MODE_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.PATH_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.POSITION_ATTACH;

/**
 * Created by Alexadner Dadukin on 26.06.2016.
 */

public class AlbumImagePlaceholderSchema extends BaseFragment {

    private static final String TAG = "Album image placeholder";

    @BindView(R.id.detail_image) PhotoView detailView;

    private GalleryConstants.AlbumMode albumMode;

    public AlbumImagePlaceholderSchema() {
        super(R.layout.fragment_album_image_placeholder);
    }

    public static AlbumImagePlaceholderSchema getFragment(int position, String imagePath, int mode) {
        AlbumImagePlaceholderSchema fragment = new AlbumImagePlaceholderSchema();
        Bundle args = new Bundle();
        args.putInt(POSITION_ATTACH, position);
        args.putString(PATH_ATTACH, imagePath);
        args.putInt(MODE_ATTACH, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void setupViews() {
        albumMode = GalleryConstants.AlbumMode.obtainMode(getArguments().getInt(MODE_ATTACH));
        String path = albumMode.obtainPath(getArguments().getString(PATH_ATTACH));

        Glide.with(getActivity())
                .load(path)
                .fitCenter()
                .into(detailView);
    }

}