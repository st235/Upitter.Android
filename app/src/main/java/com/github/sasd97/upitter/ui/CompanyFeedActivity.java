package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.holders.CompanyHolder;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.services.query.ApplicationInfoQueryService;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.fragments.BaseFeedFragment;
import com.github.sasd97.upitter.ui.fragments.FavoritesFragment;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Names;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.Upitter.*;
import static com.github.sasd97.upitter.constants.FeedStartupLinks.NOTHING_STARTED;
import static com.github.sasd97.upitter.constants.FeedStartupLinks.SETTINGS_STARTED;
import static com.github.sasd97.upitter.constants.FeedStartupLinks.CREATE_POST_STARTED;

public class CompanyFeedActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ApplicationInfoQueryService.OnInfoListener {

    private CompanyModel company;
    private int currentState = NOTHING_STARTED;
    private ApplicationInfoQueryService infoQueryService;

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;

    private RelativeLayout createPostView;
    private ImageView logoImageView;
    private TextView titleTextView;
    private TextView aliasTextView;

    private BaseFeedFragment baseFeedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_feed);
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar);
        company = ((CompanyHolder) getHolder()).get();
        Logger.json(company.toJson());

        Intent intent = new Intent(this, CompanyProfileActivity.class);
        startActivity(intent);
        finish();

        baseFeedFragment = BaseFeedFragment.getFragment();

        infoQueryService = ApplicationInfoQueryService.getService(this);
        infoQueryService.obtainInfo(company.getAccessToken());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        obtainNavigationHeader(navigationView.getHeaderView(0));
        obtainCompanyHeader();

        createPostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }

                currentState = CREATE_POST_STARTED;
                startActivity(new Intent(CompanyFeedActivity.this, PostCreationActivity.class));
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, baseFeedFragment)
                .commit();
    }

    private void obtainNavigationHeader(View header) {
        createPostView = findById(header, R.id.create_post_tape_company_nav_header);
        logoImageView = findById(header, R.id.logo_company_tape);
        titleTextView = findById(header, R.id.title_company_tape);
        aliasTextView = findById(header, R.id.alias_company_tape);
    }

    private void obtainCompanyHeader() {
        obtainCompanyLogo(logoImageView, company.getAvatarUrl());
        titleTextView.setText(company.getName());
        aliasTextView.setText(Names.obtainAlias(company.getAlias()));
    }

    private void obtainCompanyLogo(ImageView holder, String logoUrl) {
        if (logoUrl == null) {
            String preview = Names.getNamePreview(company.getName());

            TextDrawable textDrawable = TextDrawable
                                            .builder()
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
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        switch (id) {
            case R.id.nav_tape:
                navigate(baseFeedFragment);
                break;
            case R.id.nav_favorites:
                navigate(FavoritesFragment.getFragment());
                break;
            case R.id.nav_settings:
                currentState = SETTINGS_STARTED;
                startActivity(new Intent(this, CompanySettingsActivity.class));
                break;
            case R.id.nav_app:
                startActivity(new Intent(this, AppInfoActivity.class));
                break;
            case R.id.nav_logout:
                deleteSession();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    @Override
    protected void onResume() {
        super.onResume();
        switch (currentState) {
            case SETTINGS_STARTED:
                currentState = NOTHING_STARTED;
                obtainCompanyHeader();
                break;
            case CREATE_POST_STARTED:
                currentState = NOTHING_STARTED;
                refreshTape();
                break;
            default:
                break;
        }
    }

    @Override
    public void onObtainInfo(int code, int version) {
        Logger.i(code + ";" + version);
    }

    @Override
    public void onError(ErrorModel error) {

    }

    private void refreshTape() {
        baseFeedFragment.onRefresh();

        Snackbar
                .make(getRootView(), "Refreshing...", Snackbar.LENGTH_SHORT)
                .show();
    }
}
