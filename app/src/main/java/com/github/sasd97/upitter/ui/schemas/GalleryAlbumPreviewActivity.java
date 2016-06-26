package com.github.sasd97.upitter.ui.schemas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.adapters.GalleryAlbumPagerAdapter;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.*;

import java.util.ArrayList;
import java.util.Locale;

public class GalleryAlbumPreviewActivity extends AppCompatActivity {

    private String OF_PREFIX;
    private String TITLE_SCHEMA = "%1$d %2$s %3$d";

    private int albumSize = 0;

    private ArrayList<String> imagePaths;
    private int currentGalleryPosition;

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

//        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
//        bottomBar.useDarkTheme();
//        bottomBar.setItemsFromMenu(R.menu.bottom_menu_detailded, new OnMenuTabClickListener() {
//            @Override
//            public void onMenuTabSelected(@IdRes int menuItemId) {
//                switch (menuItemId) {
////                    case R.id.bottom_bar_preview:
////                        onBackPressed();
////                        break;
//                    case R.id.bottom_bar_edit:
//                        Intent intent = new Intent(GalleryAlbumPreviewActivity.this, EditImageActivity.class);
//                        intent.putExtra(PATH_ATTACH, imagePaths.get(mViewPager.getCurrentItem()));
//                        startActivityForResult(intent, EDIT_SCREEN_START_ID);
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            @Override
//            public void onMenuTabReSelected(@IdRes int menuItemId) {
//                switch (menuItemId) {
//                    case R.id.bottom_bar_preview:
//                        onBackPressed();
//                        break;
//                    case R.id.bottom_bar_apply:
//                        Intent result = new Intent();
//                        result.putExtra(PUT_CROPPED_IMAGE, imagePaths.get(mViewPager.getCurrentItem()));
//                        setResult(RESULT_OK, result);
//                        finish();
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_detailed, menu);
//        return true;
//    }

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
