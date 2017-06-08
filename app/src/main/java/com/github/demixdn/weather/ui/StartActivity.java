package com.github.demixdn.weather.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import com.github.demixdn.weather.ui.addcity.AddCityActivity;
import com.github.demixdn.weather.ui.transformation.CropCircleTransformation;
import com.github.demixdn.weather.utils.AppTypeface;
import com.github.demixdn.weather.utils.Logger;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class StartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EmptyCitiesFragment.OnCityAddClickListener {


    private AppTypeface appTypeface;
    private AuthManager authManager;
    private CitiesRepository citiesRepository;
    private NavigationView navigationView;
    private FloatingActionButton fabAddCity;
    private TextView tvUserName;
    private TextView tvUserEmail;
    private ImageView ivUser;
    private ProgressDialog progressDialog;


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

        View header = navigationView.getHeaderView(0);
        tvUserName = (TextView) header.findViewById(R.id.tvUserName);
        tvUserEmail = (TextView) header.findViewById(R.id.tvUserEmail);
        ivUser = (ImageView) header.findViewById(R.id.ivUserPhoto);

        tvUserName.setTypeface(appTypeface.get(AppTypeface.Roboto.REGULAR));
        tvUserEmail.setTypeface(appTypeface.get(AppTypeface.Roboto.REGULAR));

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Get Cities in progress");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!authManager.isLogged()) {
            SignInActivity.navigate(this);
        } else {
            FirebaseUser user = authManager.getActiveUser();
            if (user != null) {
                showUserInfoOnHeader(user);
            }
            progressDialog.show();
            citiesRepository.getUserCities(new DataCallback<List<City>>() {
                @Override
                public void onSuccess(@NonNull List<City> cities) {
                    Logger.d(cities.toString());
                    progressDialog.dismiss();
                }

                @Override
                public void onException(@NonNull Exception ex) {
                    progressDialog.dismiss();
                    Logger.e(ex);
                }
            });
        }
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

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCityAddClick() {
        AddCityActivity.navigate(this);
    }
}
