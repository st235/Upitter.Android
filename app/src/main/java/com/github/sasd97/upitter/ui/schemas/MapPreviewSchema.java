package com.github.sasd97.upitter.ui.schemas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.AuthorOnMapModel;
import com.github.sasd97.upitter.services.runners.MapExecutorService;
import com.github.sasd97.upitter.services.LocationService;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Maps;
import com.github.sasd97.upitter.utils.Names;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.COORDINATES_ATTACH;

/**
 * Created by alexander on 21.07.16.
 */
public class MapPreviewSchema extends BaseActivity
        implements OnMapReadyCallback,
        LocationService.OnLocationListener,
        MapExecutorService.OnMapExecutionListener,
        View.OnClickListener {

    private static final String TAG = "Show on map";

    private GoogleMap googleMap;
    private Location currentLocation;
    private AuthorOnMapModel authorToShow;

    @BindView(R.id.fab) FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_coordinates_selection);
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar, true);

        LocationService service = LocationService.getService(this);
        service.init(this);

        authorToShow = getIntent().getParcelableExtra(COORDINATES_ATTACH);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle(authorToShow.getAuthorName());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "CLICKED");

        String url = Maps.obtainPathUrl(
                currentLocation.getLatitude(),
                currentLocation.getLongitude(),
                authorToShow.getLatitude(),
                authorToShow.getLongitude());

        MapExecutorService.execute(url, this);
    }

    private void setupViews(final GoogleMap googleMap, final LatLng position) {
        final Bitmap.Config config = Bitmap.Config.ARGB_8888;
        final int topOffset = Dimens.dpToPx(5);
        final int offset = Dimens.dpToPx(12);
        final int bottomOffset = Dimens.dpToPx(15);
        final int side = Dimens.dpToPx(100);
        final int scaleSide = Math.min(side - 2 * offset, side - bottomOffset - offset);

        final Bitmap marker = Bitmap.createBitmap(side, side, config);
        final Canvas canvas = new Canvas(marker);

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_marker_ava);
        drawable.setBounds(0, 0, side, side);
        drawable.draw(canvas);

        if (authorToShow.isAvatar()) {
            Glide
                    .with(this)
                    .load(authorToShow.getAuthorAvatarUrl())
                    .asBitmap()
                    .transform(new CenterCrop(this), new RoundedCornersTransformation(this, Dimens.drr(), 0))
                    .into(new SimpleTarget<Bitmap>(scaleSide, scaleSide) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            int extraOffset = offset + (scaleSide - resource.getWidth()) / 2;

                            canvas.drawBitmap(resource,
                                    null,
                                    new Rect(extraOffset, topOffset, side - extraOffset, side - topOffset - bottomOffset),
                                    null);
                            setMarker(googleMap, position, marker);
                        }
                    });
            return;
        }

        String preview = Names.getNamePreview(authorToShow.getAuthorName());

        TextDrawable textDrawable = TextDrawable
                .builder()
                .buildRoundRect(preview,
                        ContextCompat.getColor(this, R.color.colorShadowDark),
                        Dimens.dpToPx(4));

        Bitmap resource = convertToBitmap(textDrawable, scaleSide, scaleSide);
        int extraOffset = offset + (scaleSide - resource.getWidth()) / 2;

        canvas.drawBitmap(resource,
                null,
                new Rect(extraOffset, topOffset, side - extraOffset, side - topOffset - bottomOffset),
                null);
        setMarker(googleMap, position, marker);
    }

    private void setMarker(GoogleMap googleMap, LatLng position, Bitmap marker) {
        googleMap.addMarker(new MarkerOptions()
                .position(position)
                .flat(true)
                .icon(BitmapDescriptorFactory.fromBitmap(marker)));
    }

    private Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        LatLng position = new LatLng(authorToShow.getLatitude(), authorToShow.getLongitude());
        setupViews(googleMap, position);
        moveToPoint(googleMap, position);
    }

    private void moveToPoint(GoogleMap googleMap, LatLng points) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points, 15));
    }

    @Override
    public void onLocationFind(Location location) {
        fab.setVisibility(View.VISIBLE);
        this.currentLocation = location;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation = location;
    }

    @Override
    public void onBuildPath(String response) {
        Maps.drawPath(this.googleMap, response);
    }
}
