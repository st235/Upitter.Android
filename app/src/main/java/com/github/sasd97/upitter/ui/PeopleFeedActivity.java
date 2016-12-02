package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.holders.PeopleHolder;
import com.github.sasd97.upitter.models.PeopleModel;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.fragments.FavoritesFragment;
import com.github.sasd97.upitter.ui.fragments.PeopleFeedFragment;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Names;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.Upitter.*;

public class PeopleFeedActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PeopleModel people;
    private PeopleFeedFragment peopleFeedFragment;

    private ImageView blurBackgroundImageView;
    private ImageView avatarImageView;
    private TextView userNameTextView;

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
        obtainNavigationHeader(navigationView.getHeaderView(0));
        obtainPeopleHeader();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, peopleFeedFragment)
                .commit();
    }

    private void obtainNavigationHeader(View header) {
        blurBackgroundImageView = findById(header, R.id.blured_background);
        avatarImageView = findById(header, R.id.user_avatar);
        userNameTextView = findById(header, R.id.user_name);
    }

    private void obtainPeopleHeader() {
        obtainPeopleLogo(avatarImageView, blurBackgroundImageView, people.getAvatarUrl());
        userNameTextView.setText(people.getName());
    }

    private void obtainPeopleLogo(ImageView holder, ImageView background, String logoUrl) {
        if (logoUrl == null) {
            String preview = Names.getNamePreview(people.getName());

            TextDrawable textDrawable = TextDrawable
                    .builder()
                    .beginConfig()
                    .width(Dimens.dpToPx(75))
                    .height(Dimens.dpToPx(75))
                    .endConfig()
                    .buildRoundRect(preview,
                            ContextCompat.getColor(this, R.color.colorShadowDark),
                            Dimens.dpToPx(4));

            holder.setImageDrawable(textDrawable);
            return;
        }

        Glide
                .with(this)
                .load(logoUrl)
                .bitmapTransform(new CenterCrop(this), new RoundedCornersTransformation(this, Dimens.drr(), 0))
                .into(holder);

        Glide
                .with(this)
                .load(logoUrl)
                .bitmapTransform(new CenterCrop(this), new BlurTransformation(this))
                .into(background);
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
        getMenuInflater().inflate(R.menu.tape, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_tape:
                setActionTitle(R.string.title_activity_tape);
                navigate(peopleFeedFragment);
                break;
            case R.id.nav_notification:
                setActionTitle(R.string.favorites_activity_tape);
                startActivity(new Intent(this, NotificationActivity.class));
                break;
            case R.id.nav_favorites:
                navigate(FavoritesFragment.getFragment());
                break;
            case R.id.nav_subscriptions:
                startActivity(new Intent(this, PeopleSubscriptionActivity.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, PeopleSettingsActivity.class));
                break;
            case R.id.nav_app:
                startActivity(new Intent(this, AppInfoWhiteActivity.class));
                break;
            case R.id.nav_logout:
                deleteSession();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setActionTitle(@StringRes int title) {
        getSupportActionBar().setTitle(title);
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
        setHolder(null);
        startActivity(intent);
        finish();
    }
}
