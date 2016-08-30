package com.github.sasd97.upitter.ui.results;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.holders.LocationHolder;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.services.GeocoderService;
import com.github.sasd97.upitter.services.LocationService;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Palette;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import butterknife.BindString;
import butterknife.BindView;

public class LocationSelectionResult extends BaseActivity
        implements OnMapReadyCallback,
        GeocoderService.OnAddressListener,
        LocationService.OnLocationListener {

    private static final String TAG = "Location Selection";

    private static MapFragment mapFragment;
    private String RADIUS_SCHEMA = "%1$d %2$s";

    private LocationService locationService;
    private Location location;
    private GoogleMap map;
    private Circle circle;

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.seekbar_region) DiscreteSeekBar seekBar;
    @BindView(R.id.radius_info_region) TextView radiusTextView;
    @BindView(R.id.place_region) MaterialEditText regionChooser;

    @BindString(R.string.radius_postfix_location_activity) String RADIUS_POSTFIX;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);
        setToolbar(R.id.toolbar, false);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationHolder.reset(location);
                moveToPoint(map, location);
            }
        });

        radiusTextView = (TextView) findViewById(R.id.radius_info_region);

        CoordinatesModel coordinatesModel = new CoordinatesModel
                .Builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
        GeocoderService.find(this, coordinatesModel, this);

        regionChooser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    LocationHolder.setfLocation(location);
                    moveToPoint(map, location);
                    return;
                }

                if (s.toString().length() > 4) {
                    LatLng coordinates = GeocoderService.getCoordinates(LocationSelectionResult.this, s.toString());
                    if (coordinates == null) return;
                    LocationHolder.setfLocation(coordinates);
                    moveToPoint(map, coordinates);
                }
            }
        });

        seekBar.setProgress(LocationHolder.getRadius());
        radiusTextView.setText(obtainRegionText(LocationHolder.getRadius()));

        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {

            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                circle.setRadius(value);
                LocationHolder.setRadius(value);
                radiusTextView.setText(obtainRegionText(value));
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
    }

    @Override
    protected void setupViews() {
        locationService = LocationService.getService(this);
        locationService.init(this);
    }

    public String obtainRegionText(int radius) {
        return String.format(RADIUS_SCHEMA, radius, RADIUS_POSTFIX);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        googleMap.setMyLocationEnabled(true);
        moveToPoint(googleMap, LocationHolder.getLocation());

        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                LocationHolder.setfLocation(latLng);
                moveToPoint(googleMap, latLng);
            }
        });
    }

    private void moveToPoint(GoogleMap googleMap, Location points) {
        moveToPoint(googleMap, new double[]{ points.getLatitude(), points.getLongitude() });
    }

    private void moveToPoint(GoogleMap googleMap, LatLng points) {
        moveToPoint(googleMap, new double[]{ points.latitude, points.longitude });
    }

    private void moveToPoint(GoogleMap googleMap, double[] lPoint) {
        LatLng points = new LatLng(lPoint[0], lPoint[1]);

        if (circle == null)
            circle = googleMap.addCircle(new CircleOptions()
                    .center(points)
                    .radius(LocationHolder.getRadius())
                    .strokeWidth(0)
                    .fillColor(Palette.getPrimaryTransparentColor()));
        else circle.setCenter(points);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points, 15));
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        Log.d(TAG, location.toString());
    }

    @Override
    public void onLocationFind(Location location) {
        this.location = location;
        Log.d(TAG, location.toString());
    }

    @Override
    public void onAddressFail() {

    }

    @Override
    public void onAddressReady(CoordinatesModel coordinatesModel) {
        regionChooser.setHint(coordinatesModel.getAddress().getAddressLine(0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.location_selection_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}
