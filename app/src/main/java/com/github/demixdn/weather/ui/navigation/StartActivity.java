package com.github.demixdn.weather.ui.navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.demixdn.weather.App;
import com.github.demixdn.weather.R;
import com.github.demixdn.weather.data.Observer;
import com.github.demixdn.weather.data.model.Weather;
import com.github.demixdn.weather.ui.cities.EmptyCitiesFragment;
import com.github.demixdn.weather.ui.cities.WeatherItemClickListener;
import com.github.demixdn.weather.utils.AppTypeface;
import com.github.demixdn.weather.utils.ImageHelper;
import com.github.demixdn.weather.utils.Logger;
import com.google.firebase.auth.FirebaseUser;


public class StartActivity extends AppCompatActivity
        implements StartView, NavigationView.OnNavigationItemSelectedListener,
        EmptyCitiesFragment.OnCityAddClickListener, Observer, WeatherItemClickListener {


    private static final int REQUEST_CODE_SIGN = 1001;
    private static final int INDEX_CITY = 0;
    private static final int INDEX_PROFILE = 1;
    private static final int INDEX_ABOUT = 2;
    private AppTypeface appTypeface;
    private NavigationView navigationView;
    private FloatingActionButton fabAddCity;
    private TextView tvUserName;
    private TextView tvUserEmail;
    private ImageView ivUser;
    private ProgressDialog progressDialog;
    private FragmentManager fragmentManager;

    private Navigator navigator;

    private StarterPresenter presenter;


    public void setAppTypeface(@NonNull AppTypeface appTypeface) {
        this.appTypeface = appTypeface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initUI();
        initComponents();
        App.getInstance().getAppComponent().inject(this);
        setTypeface();
    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabAddCity = (FloatingActionButton) findViewById(R.id.fabAddCity);
        fabAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCityAddClick();
            }
        });

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(INDEX_CITY);
        tvUserName = (TextView) header.findViewById(R.id.tvUserName);
        tvUserEmail = (TextView) header.findViewById(R.id.tvUserEmail);
        ivUser = (ImageView) header.findViewById(R.id.ivUserPhoto);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.cities_progress));
        progressDialog.setCancelable(false);
    }

    private void initComponents() {
        fragmentManager = getSupportFragmentManager();
        navigator = new Navigator();
    }

    private void setTypeface() {
        tvUserName.setTypeface(appTypeface.get(AppTypeface.Roboto.REGULAR));
        tvUserEmail.setTypeface(appTypeface.get(AppTypeface.Roboto.REGULAR));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        progressDialog.show();
        getPresenter().viewReady();
    }

    @Override
    protected void onDestroy() {
        getPresenter().unbindView();
        super.onDestroy();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_cities) {
            showCities();
        } else if (id == R.id.nav_profile) {
            showProfile();
        } else if (id == R.id.nav_about) {
            showAppInfo();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fabShow() {
        if (!fabAddCity.isShown()) {
            fabAddCity.show();
        }
    }

    private void fabHide() {
        if (fabAddCity.isShown()) {
            fabAddCity.hide();
        }
    }

    @Override
    public void showCities() {
        setTitle(R.string.cities_weather);
        progressDialog.dismiss();
        navigationView.getMenu().getItem(INDEX_CITY).setChecked(true);
        fabShow();
        navigator.showCities(fragmentManager);
    }

    private void showAppInfo() {
        setTitle(R.string.about);
        progressDialog.dismiss();
        navigationView.getMenu().getItem(INDEX_ABOUT).setChecked(true);
        fabHide();
        navigator.showInfo(fragmentManager);
    }

    private void showProfile() {
        setTitle(R.string.profile);
        progressDialog.dismiss();
        navigationView.getMenu().getItem(INDEX_PROFILE).setChecked(true);
        fabHide();
        navigator.showProfile(fragmentManager);
    }

    @Override
    public void showLoginScreen() {
        progressDialog.dismiss();
        navigator.showSignIn(this, REQUEST_CODE_SIGN);
    }

    @Override
    public void showUser(@NonNull FirebaseUser user) {
        tvUserName.setText(user.getDisplayName());
        tvUserEmail.setText(user.getEmail());
        int size = getResources().getDimensionPixelSize(R.dimen.header_user_icon_size);
        Uri userPhotoUrl = user.getPhotoUrl();
        ImageHelper.loadUserPhoto(this, size, userPhotoUrl, ivUser);
    }

    @Override
    public void showEmptyState() {
        progressDialog.dismiss();
        navigationView.getMenu().getItem(INDEX_CITY).setChecked(true);
        fabHide();
        navigator.showEmptyState(fragmentManager);
    }

    @Override
    public void onCityAddClick() {
        navigator.showAddCity(this);
    }

    @Override
    public void bindPresenter(StarterPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public StarterPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SIGN) {
            if (resultCode == RESULT_OK) {
                progressDialog.dismiss();
                App.getInstance().getAppComponent().reload();
            } else {
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void update() {
        Logger.d("Observable update StartActivity");
        App.getInstance().getAppComponent().inject(this);
        setTypeface();
        getPresenter().subscribe();
        getPresenter().viewReady();
    }

    @Override
    public void onWeatherItemClicked(@NonNull Weather item) {
        navigator.showDetail(this, item);
    }
}
