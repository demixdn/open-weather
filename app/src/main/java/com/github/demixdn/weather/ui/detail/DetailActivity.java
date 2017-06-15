package com.github.demixdn.weather.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.demixdn.weather.R;
import com.github.demixdn.weather.data.model.Weather;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String EXTRA_WEATHER_ITEM = "extra_weather_item";
    private static final float MAX_ZOOM_PREFERENCE = 10f;
    private static final float MIN_ZOOM_PREFERENCE = 7f;
    private static final float ZOOM_VALUE = 9f;
    private Weather weatherItem;

    public static void navigate(@NonNull Context context, @NonNull Weather weather) {
        Intent starter = new Intent(context, DetailActivity.class);
        starter.putExtra(EXTRA_WEATHER_ITEM, weather);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (getWeatherItem()) return;
        setToolbarTitle();
        showWeatherFragment();
        showMapFragment();
    }

    private void showMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void showWeatherFragment() {
        WeatherDetailFragment detailFragment = WeatherDetailFragment.newInstance(weatherItem);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.weather_container, detailFragment)
                .commitAllowingStateLoss();
    }

    private void setToolbarTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        setTitle(getString(R.string.city_weather, weatherItem.getCity().toAppString()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
    }

    private boolean getWeatherItem() {
        weatherItem = (Weather) getIntent().getSerializableExtra(EXTRA_WEATHER_ITEM);
        if (weatherItem == null) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng kievLatLng = new LatLng(weatherItem.getCity().lat, weatherItem.getCity().lon);
        googleMap.addMarker(new MarkerOptions().position(kievLatLng).title("Marker in " + weatherItem.getCity().toAppString()));
        googleMap.setMaxZoomPreference(MAX_ZOOM_PREFERENCE);
        googleMap.setMinZoomPreference(MIN_ZOOM_PREFERENCE);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kievLatLng, ZOOM_VALUE));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
