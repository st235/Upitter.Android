package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.fragments.BaseFeedFragment;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Names;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.Upitter.*;

public class CompanyFeedActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CompanyModel company;

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;

    private RelativeLayout createPostView;
    private ImageView logoImageView;
    private TextView titleTextView;
    private TextView categoryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_feed);
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar);
        company = (CompanyModel) getHolder().get();

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
                startActivity(new Intent(CompanyFeedActivity.this, PostCreationActivity.class));
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, BaseFeedFragment.getFragment())
                .commit();
    }

    private void obtainNavigationHeader(View header) {
        createPostView = findById(header, R.id.create_post_tape_company_nav_header);
        logoImageView = findById(header, R.id.logo_company_tape);
        titleTextView = findById(header, R.id.title_company_tape);
        categoryTextView = findById(header, R.id.category_title_category_single_view);
    }

    private void obtainCompanyHeader() {
        obtainCompanyLogo(logoImageView, company.getAvatarUrl());
        titleTextView.setText(company.getName());
        //categoryTextView.setText(company.get);
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
                .bitmapTransform(new CenterCrop(this), new RoundedCornersTransformation(this, Dimens.dpToPx(4), 0))
                .into(holder);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            case R.id.nav_settings:
                startActivity(new Intent(this, CompanySettingsActivity.class));
                break;
            case R.id.nav_logout:
                deleteSession();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void deleteSession() {
        Intent intent = new Intent(this, AuthorizationActivity.class);
        getHolder().delete();
        startActivity(intent);
        finish();
    }
}
