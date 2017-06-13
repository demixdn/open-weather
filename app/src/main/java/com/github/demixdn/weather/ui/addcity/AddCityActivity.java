package com.github.demixdn.weather.ui.addcity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.github.demixdn.weather.App;
import com.github.demixdn.weather.R;
import com.github.demixdn.weather.utils.Logger;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCityActivity extends AppCompatActivity implements AddCityView {


    private AutoCompleteTextView autoCompleteCityField;
    private AddCityPresenter presenter;
    private AddCityAdapter addCityAdapter;
    private ApproveCityDelegate citiesDelegate;
    private LinearLayout approvedContainer;

    public static void navigate(Context context) {
        Intent starter = new Intent(context, AddCityActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_city);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initUI();
        App.getInstance().getAppComponent().inject(this);
        presenter.onViewCreated(savedInstanceState);
        citiesDelegate = new ApproveCityDelegate(approvedContainer, getPresenter());
    }

    private void initUI() {
        autoCompleteCityField = (AutoCompleteTextView) findViewById(R.id.atvAddCityFiled);
        autoCompleteCityField.clearFocus();
        approvedContainer = (LinearLayout) findViewById(R.id.llApprovedCities);
        approvedContainer.requestFocus();
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

    @Override
    public void onStop() {
        clearData();
        super.onStop();
    }

    private void clearData() {
        if (addCityAdapter != null) {
            addCityAdapter.destroyData();
        }
        System.gc();
        getPresenter().unbindView();
    }


    @Override
    public void bindPresenter(AddCityPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public AddCityPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showCities(@NonNull List<String> cities) {
        addCityAdapter = new AddCityAdapter(cities);
        autoCompleteCityField.setAdapter(addCityAdapter);
        autoCompleteCityField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityName = autoCompleteCityField.getText().toString();
                Logger.i(this, "OnItemClicked " + cityName);
                citiesDelegate.addCity(cityName);
            }
        });
    }

    @Override
    public void onAddCityApproved(@NonNull String cityName, boolean result) {
        autoCompleteCityField.setText(null);
        citiesDelegate.onAddCityApproved(cityName, result);
    }

    @Override
    public void onAddRemoveApproved(@NonNull String cityName, boolean result) {
        citiesDelegate.onAddRemoveApproved(cityName, result);
    }
}
