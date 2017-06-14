package com.github.demixdn.weather.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

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
        weatherItem = (Weather) getIntent().getSerializableExtra(EXTRA_WEATHER_ITEM);
        if (weatherItem == null) {
            finish();
            return;
        }
        WeatherDetailFragment detailFragment = WeatherDetailFragment.newInstance(weatherItem);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.weather_container, detailFragment)
                .commitAllowingStateLoss();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setClickable(false);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng kievLatLng = new LatLng(weatherItem.getCity().lat, weatherItem.getCity().lon);
        googleMap.addMarker(new MarkerOptions().position(kievLatLng).title("Marker in " + weatherItem.getCity().toAppString()));
        googleMap.setMaxZoomPreference(10f);
        googleMap.setMinZoomPreference(7f);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kievLatLng, 9f));

    }
}
