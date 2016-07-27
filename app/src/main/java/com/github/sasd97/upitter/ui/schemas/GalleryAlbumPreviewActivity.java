package com.github.sasd97.upitter.ui.schemas;

import android.content.Intent;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.constants.GalleryConstants;
import com.github.sasd97.upitter.ui.adapters.GalleryAlbumPagerAdapter;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.results.EditImageActivity;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.*;

import java.util.List;
import java.util.Locale;

public class GalleryAlbumPreviewActivity extends BaseActivity {

    private String OF_PREFIX;
    private String TITLE_SCHEMA = "%1$d %2$s %3$d";

    private int albumSize = 0;

    private List<String> imagePaths;
    private int currentGalleryPosition;
    private int currentMode;

    private GalleryConstants.AlbumMode mode;

    private ViewPager mViewPager;
    private GalleryAlbumPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_album_preview_activity);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Slidr.attach(this, SlidrUtils.config(SlidrPosition.VERTICAL, 0.1f));

        OF_PREFIX = getString(R.string.image_of_gallery_album_preview_activity);

        imagePaths = getIntent().getStringArrayListExtra(LIST_ATTACH);
        currentGalleryPosition = getIntent().getIntExtra(POSITION_ATTACH, 0);
        currentMode = getIntent().getIntExtra(MODE_ATTACH, 0);

        mode = GalleryConstants.AlbumMode.obtainMode(currentMode);
        imagePaths = ListUtils.each(imagePaths, new ListUtils.OnListModifyListener<String>() {
            @Override
            public String modify(String object) {
                return mode.obtainPath(object);
            }
        });

        albumSize = imagePaths.size();

        mSectionsPagerAdapter = new GalleryAlbumPagerAdapter(getSupportFragmentManager(), imagePaths);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(currentGalleryPosition);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                toolbar.setTitle(String.format(Locale.getDefault(), TITLE_SCHEMA,
                        (position + 1), OF_PREFIX, albumSize));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void bindViews() {

    }

    public void onApplyClick(View v) {
        Intent result = new Intent();
        result.putExtra(PUT_CROPPED_IMAGE, imagePaths.get(mViewPager.getCurrentItem()));
        setResult(RESULT_OK, result);
        finish();
    }

    public void onEditClick(View v) {
        Intent intent = new Intent(GalleryAlbumPreviewActivity.this, EditImageActivity.class);
        intent.putExtra(PATH_ATTACH, imagePaths.get(mViewPager.getCurrentItem()));
        startActivityForResult(intent, 12);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent intent = new Intent();
            intent.putExtra(PUT_CROPPED_IMAGE, data.getStringExtra(PUT_CROPPED_IMAGE));
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
