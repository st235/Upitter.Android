package com.github.sasd97.upitter.ui.schemas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.utils.Names;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.*;

import java.util.ArrayList;

public class GalleryAlbumPreviewActivity extends AppCompatActivity {

    private ArrayList<String> imagePaths;
    private int currentGalleryPosition;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_album_preview_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SlidrConfig config = new SlidrConfig.Builder()
                                .position(SlidrPosition.VERTICAL)
                                .sensitivity(0.1f)
                                .build();

        Slidr.attach(this, config);

        imagePaths = getIntent().getStringArrayListExtra(LIST_ATTACH);
        currentGalleryPosition = getIntent().getIntExtra(POSITION_ATTACH, 0);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), imagePaths);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(currentGalleryPosition);

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

    public static class PlaceholderFragment extends Fragment {

        private ImageView detailView = null;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int position, String imagePath) {
            PlaceholderFragment fragment = new PlaceholderFragment();
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
            detailView = (ImageView) rootView.findViewById(R.id.detail_image);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent intent = new Intent();
            intent.putExtra(PUT_CROPPED_IMAGE, data.getStringExtra(PUT_CROPPED_IMAGE));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<String> data;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<String> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position, data.get(position));
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Hello";
        }
    }
}
