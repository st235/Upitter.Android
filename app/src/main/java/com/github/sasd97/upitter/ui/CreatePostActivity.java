package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.constants.IntentKeysConstants;
import com.github.sasd97.upitter.constants.RequestCodesConstants;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Gallery;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

public class CreatePostActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post_activity);
        setToolbar(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));
    }

    @Override
    protected void bindViews() {

    }

    public void onPhotosClick(View v) {
        Intent gallery =
               new Gallery
                .Builder()
                .from(this)
                .multiSelectionMode(true)
                .selectionMaxCounter(5)
                .build();
        startActivityForResult(gallery, RequestCodesConstants.GALLERY_ACTIVITY_REQUEST);
    }
}
