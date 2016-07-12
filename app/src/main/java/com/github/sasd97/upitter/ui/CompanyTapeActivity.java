package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.fragments.TapeFragment;
import com.github.sasd97.upitter.utils.Dimens;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.Upitter.*;

public class CompanyTapeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    private CompanyModel company;

    //  Navbar region
    private RelativeLayout createPostView;
    private ImageView logoImageView;
    private TextView titleTextView;
    private TextView categoryTextView;
    //  Endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_tape_activity);
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
                startActivity(new Intent(CompanyTapeActivity.this, CreatePostActivity.class));
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, TapeFragment.getFragment())
                .commit();
    }

    @Override
    protected void bindViews() {
        drawer = findById(R.id.drawer_layout);
        navigationView = findById(R.id.nav_view);
    }

    private void obtainNavigationHeader(View header) {
        createPostView = (RelativeLayout) header.findViewById(R.id.create_post_tape_company_nav_header);
        logoImageView = (ImageView) header.findViewById(R.id.logo_company_tape);
        titleTextView = (TextView) header.findViewById(R.id.title_company_tape);
        categoryTextView = (TextView) header.findViewById(R.id.category_title_category_single_view);
    }

    private void obtainCompanyHeader() {
        obtainCompanyLogo(logoImageView, company.getAvatarUrl());
        titleTextView.setText(company.getName());
        //categoryTextView.setText(company.get);
    }

    private void obtainCompanyLogo(ImageView holder, String logoUrl) {
        if (logoUrl == null) {
            TextDrawable textDrawable = TextDrawable
                                            .builder()
                                            .buildRoundRect(company.getName().substring(0, 1),
                                                    ContextCompat.getColor(this, R.color.colorShadowDark),
                                                    Dimens.dpToPx(4));
            holder.setImageDrawable(textDrawable);
            return;
        }

        Glide
                .with(this)
                .load(logoUrl)
                .bitmapTransform(new RoundedCornersTransformation(this, Dimens.dpToPx(4), 0))
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
