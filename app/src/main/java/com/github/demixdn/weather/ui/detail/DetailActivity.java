package com.github.demixdn.weather.ui.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.demixdn.weather.R;
import com.google.android.gms.maps.MapFragment;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.weather_container, new WeatherDetailFragment()).commitAllowingStateLoss();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

    }

}
