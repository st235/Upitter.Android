package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.holders.PeopleHolder;
import com.github.sasd97.upitter.models.PeopleModel;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.fragments.FavoritesFragment;
import com.github.sasd97.upitter.ui.fragments.PeopleFeedFragment;
import com.orhanobut.logger.Logger;

import butterknife.BindView;

import static com.github.sasd97.upitter.Upitter.*;

public class PeopleFeedActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PeopleModel people;
    private PeopleFeedFragment peopleFeedFragment;

    @BindView(R.id.drawer_layout) DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_feed);
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar, true);
        people = ((PeopleHolder) getHolder()).get();
        peopleFeedFragment = PeopleFeedFragment.getFragment();
        Logger.i(people.toString());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, peopleFeedFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.people_tape, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_tape:
                navigate(peopleFeedFragment);
                break;
            case R.id.nav_favorites:
                navigate(FavoritesFragment.getFragment());
                break;
            case R.id.nav_app:
                startActivity(new Intent(this, AppInfoActivity.class));
                break;
            case R.id.nav_logout:
                deleteSession();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navigate(BaseFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void deleteSession() {
        Intent intent = new Intent(this, AuthorizationActivity.class);
        getHolder().delete();
        startActivity(intent);
        finish();
    }
}
