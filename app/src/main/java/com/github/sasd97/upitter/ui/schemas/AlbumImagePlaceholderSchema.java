package com.github.sasd97.upitter.ui.schemas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.utils.Names;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.PATH_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.POSITION_ATTACH;

/**
 * Created by Alexadner Dadukin on 26.06.2016.
 */

public class AlbumImagePlaceholderSchema extends BaseFragment {

    @BindView(R.id.detail_image) PhotoView detailView;

    public AlbumImagePlaceholderSchema() {
        super(R.layout.fragment_album_image_placeholder);
    }

    public static AlbumImagePlaceholderSchema getFragment(int position, String imagePath) {
        AlbumImagePlaceholderSchema fragment = new AlbumImagePlaceholderSchema();
        Bundle args = new Bundle();
        args.putInt(POSITION_ATTACH, position);
        args.putString(PATH_ATTACH, imagePath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void setupViews() {
        String path = getArguments().getString(PATH_ATTACH);

        Glide.with(getActivity())
                .load(Names.getFileName(path))
                .fitCenter()
                .into(detailView);
    }

}