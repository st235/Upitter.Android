package com.github.sasd97.upitter.ui.schemas;

import android.content.Intent;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.adapters.pagers.GalleryAlbumPager;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.results.ImageEditingResult;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.*;

import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;

public class AlbumPreviewGallerySchema extends BaseActivity {

    private static final String TAG = "Gallery Album Preview";

    @BindString(R.string.image_of_gallery_album_preview_activity) String OF_PREFIX;
    private String TITLE_SCHEMA = "%1$d %2$s %3$d";

    private int albumSize = 0;

    private List<String> imagePaths;
    private int currentGalleryPosition;
    private int mode;

    private GalleryAlbumPager mSectionsPagerAdapter;

    @BindView(R.id.container) ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_preview_gallery);
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar, true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.VERTICAL, 0.1f));

        imagePaths = getIntent().getStringArrayListExtra(LIST_ATTACH);
        currentGalleryPosition = getIntent().getIntExtra(POSITION_ATTACH, 0);
        mode = getIntent().getIntExtra(MODE_ATTACH, 0);

        albumSize = imagePaths.size();

        mSectionsPagerAdapter = new GalleryAlbumPager(getSupportFragmentManager(), imagePaths, mode);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(currentGalleryPosition);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                getToolbar().setTitle(String.format(Locale.getDefault(), TITLE_SCHEMA,
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

    public void onApplyClick(View v) {
        Log.d(TAG, imagePaths.get(mViewPager.getCurrentItem()));

        Intent result = new Intent();
        result.putExtra(PUT_CROPPED_IMAGE, imagePaths.get(mViewPager.getCurrentItem()));
        setResult(RESULT_OK, result);
        finish();
    }

    public void onEditClick(View v) {
        Intent intent = new Intent(AlbumPreviewGallerySchema.this, ImageEditingResult.class);
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
