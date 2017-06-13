package com.github.demixdn.weather.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.bumptech.glide.Glide;
import com.github.demixdn.weather.App;
import com.github.demixdn.weather.R;
import com.github.demixdn.weather.data.DataCallback;
import com.github.demixdn.weather.data.auth.AuthManager;
import com.github.demixdn.weather.data.model.City;
import com.github.demixdn.weather.data.repository.CitiesRepository;
import com.github.demixdn.weather.ui.cities.EmptyCitiesFragment;
import com.github.demixdn.weather.ui.transformation.CropCircleTransformation;
import com.github.demixdn.weather.utils.AppTypeface;
import com.github.demixdn.weather.utils.Logger;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class StartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EmptyCitiesFragment.OnCityAddClickListener {


    private static final int INDEX_CITY = 0;
    private static final int INDEX_PROFILE = 1;
    private static final int INDEX_ABOUT = 2;
    private AppTypeface appTypeface;
    private AuthManager authManager;
    private CitiesRepository citiesRepository;
    private NavigationView navigationView;
    private FloatingActionButton fabAddCity;
    private TextView tvUserName;
    private TextView tvUserEmail;
    private ImageView ivUser;
    private ProgressDialog progressDialog;
    private FragmentManager fragmentManager;

    private Navigator navigator;
    private DataCallback<List<City>> citiesCallback;


    public void setAppTypeface(@NonNull AppTypeface appTypeface) {
        this.appTypeface = appTypeface;
    }

    public void setFacebookLoginDelegate(@NonNull AuthManager facebookLogin) {
        this.authManager = facebookLogin;
    }

    public void setCitiesRepository(CitiesRepository citiesRepository) {
        this.citiesRepository = citiesRepository;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        App.getInstance().getAppComponent().inject(this);
        initUI();
        fragmentManager = getSupportFragmentManager();
        navigator = new Navigator();
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

        tvUserName.setTypeface(appTypeface.get(AppTypeface.Roboto.REGULAR));
        tvUserEmail.setTypeface(appTypeface.get(AppTypeface.Roboto.REGULAR));

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Get Cities in progress");
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!authManager.isLogged()) {
            navigator.showSignIn(this);
        } else {
            showUserInfo();
            showCitiesForUser();
        }
    }

    private void showUserInfo() {
        FirebaseUser user = authManager.getActiveUser();
        if (user != null) {
            showUserInfoOnHeader(user);
        }
    }

    private void showCitiesForUser() {
        progressDialog.show();
        citiesCallback = new DataCallback<List<City>>() {
            @Override
            public void onSuccess(@NonNull List<City> cities) {
                Logger.d(cities.toString());
                progressDialog.dismiss();
                if (cities.isEmpty()) {
                    showEmptyState();
                } else {
                    showCities();
                }
            }

            @Override
            public void onException(@NonNull Exception ex) {
                progressDialog.dismiss();
                Logger.e(ex);
                showEmptyState();
            }
        };
        citiesRepository.getUserCities(citiesCallback);
    }

    private void showUserInfoOnHeader(@NonNull FirebaseUser user) {
        tvUserName.setText(user.getDisplayName());
        tvUserEmail.setText(user.getEmail());
        int size = getResources().getDimensionPixelSize(R.dimen.header_user_icon_size);
        Glide.with(this)
                .load(user.getPhotoUrl())
                .override(size, size)
                .bitmapTransform(new CropCircleTransformation(this))
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .crossFade()
                .into(ivUser);

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

    private void showCities() {
        navigationView.getMenu().getItem(INDEX_CITY).setChecked(true);
        fabShow();
        navigator.showCities(fragmentManager);
        citiesCallback = null;
    }

    private void showAppInfo() {
        navigationView.getMenu().getItem(INDEX_ABOUT).setChecked(true);
        fabHide();
        navigator.showInfo(fragmentManager);
    }

    private void showProfile() {
        navigationView.getMenu().getItem(INDEX_PROFILE).setChecked(true);
        fabHide();
        navigator.showProfile(fragmentManager);
    }


    private void showEmptyState() {
        navigationView.getMenu().getItem(INDEX_CITY).setChecked(true);
        fabHide();
        navigator.showEmptyState(fragmentManager);
    }

    @Override
    public void onCityAddClick() {
        navigator.showAddCity(this);
    }
}
