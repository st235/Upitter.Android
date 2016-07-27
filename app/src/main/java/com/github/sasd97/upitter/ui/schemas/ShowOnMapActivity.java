package com.github.sasd97.upitter.ui.schemas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.AuthorOnMapModel;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.ViewUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.COORDINATES_ATTACH;

/**
 * Created by alexander on 21.07.16.
 */
public class ShowOnMapActivity extends BaseActivity
        implements OnMapReadyCallback {

    private static final String TAG = "Show on map";

    private GoogleMap googleMap;
    private AuthorOnMapModel coordinatesToShow;

    private RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_choose_activity);
        setToolbar(R.id.toolbar, true);

        coordinatesToShow = getIntent().getParcelableExtra(COORDINATES_ATTACH);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle(coordinatesToShow.getAuthorName());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void bindViews() {
        rootLayout = findById(R.id.root_layout);
    }

    private void setupViews(GoogleMap googleMap, LatLng position) {
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(Dimens.dpToPx(100), Dimens.dpToPx(100)));
        imageView.setImageResource(R.drawable.ic_marker_ava);

//        googleMap.addMarker(new MarkerOptions()
//                .position(position)
//                .flat(true)
//                .title("Hello world")
//                .icon(BitmapDescriptorFactory.));
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

        LatLng position = new LatLng(coordinatesToShow.getLatitude(), coordinatesToShow.getLongitude());
        setupViews(googleMap, position);
        moveToPoint(googleMap, position);
    }

    private void moveToPoint(GoogleMap googleMap, LatLng points) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points, 15));
    }
}
