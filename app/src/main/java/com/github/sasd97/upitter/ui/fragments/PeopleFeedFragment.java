package com.github.sasd97.upitter.ui.fragments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.holders.LocationHolder;
import com.github.sasd97.upitter.ui.adapters.pagers.PeopleFeedViewPager;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.results.CategoriesSelectionResult;
import com.github.sasd97.upitter.ui.results.LocationSelectionResult;

import butterknife.BindArray;
import butterknife.BindView;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.CATEGORIES_ATTACH;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.CATEGORIES_ACTIVITY_REQUEST;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.LOCATION_CHANGE_REQUEST;

/**
 * Created by alexander on 15.09.16.
 */
public class PeopleFeedFragment extends BaseFragment {

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindArray(R.array.people_feed_titles) String[] titles;
    @BindView(R.id.view_pager) ViewPager viewPager;

    public PeopleFeedFragment() {
        super(R.layout.fragment_people_feed);
    }

    public static PeopleFeedFragment getFragment() {
        return new PeopleFeedFragment();
    }

    @Override
    protected void setupViews() {
        setHasOptionsMenu(true);
        viewPager.setAdapter(new PeopleFeedViewPager(getChildFragmentManager(), titles));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_choose_category:
                Intent intent = new Intent(getContext(), CategoriesSelectionResult.class);
                startActivityForResult(intent, CATEGORIES_ACTIVITY_REQUEST);
                return true;
            case R.id.action_choose_location:
                startActivityForResult(new Intent(getContext(), LocationSelectionResult.class), LOCATION_CHANGE_REQUEST);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    private void handleCategoriesIntent(@NonNull Intent intent) {
//        categoriesSelected = intent.getIntegerArrayListExtra(CATEGORIES_ATTACH);
//        feedPostRecycler.refresh();
//        postQueryService.obtainPosts(
//                getHolder().get().getAccessToken(),
//                LocationHolder.getRadius(),
//                LocationHolder.getLocation().getLatitude(),
//                LocationHolder.getLocation().getLongitude(),
//                categoriesSelected);
//    }
//
//    private void handleLocation() {
//        feedPostRecycler.refresh();
//        postQueryService.obtainPosts(
//                getHolder().get().getAccessToken(),
//                LocationHolder.getRadius(),
//                LocationHolder.getLocation().getLatitude(),
//                LocationHolder.getLocation().getLongitude(),
//                categoriesSelected);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != BaseActivity.RESULT_OK) return;

        if (requestCode == CATEGORIES_ACTIVITY_REQUEST) {
            //handleCategoriesIntent(data);
            return;
        }

        if (requestCode == LOCATION_CHANGE_REQUEST) {
            //handleLocation();
            return;
        }
    }
}
