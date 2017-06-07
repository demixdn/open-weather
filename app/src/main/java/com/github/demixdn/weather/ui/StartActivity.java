package com.github.demixdn.weather.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import com.github.demixdn.weather.data.FacebookLogin;
import com.github.demixdn.weather.ui.transformation.CropCircleTransformation;
import com.github.demixdn.weather.utils.AppTypeface;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private AppTypeface appTypeface;
    private FacebookLogin facebookLogin;
    private NavigationView navigationView;
    private FloatingActionButton fabAddCity;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    public void setAppTypeface(@NonNull AppTypeface appTypeface) {
        this.appTypeface = appTypeface;
    }

    public void setFacebookLoginDelegate(@NonNull FacebookLogin facebookLogin) {
        this.facebookLogin = facebookLogin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        App.getInstance().getAppComponent().inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabAddCity = (FloatingActionButton) findViewById(R.id.fabAddCity);
        fabAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!facebookLogin.isLogged()) {
            SignInActivity.navigate(this);
        } else {
            View header = navigationView.getHeaderView(0);
            TextView tvUserName = (TextView) header.findViewById(R.id.tvUserName);
            TextView tvUserEmail = (TextView) header.findViewById(R.id.tvUserEmail);
            FirebaseUser user = facebookLogin.getActiveUser();
            if (user != null) {
                tvUserName.setText(user.getDisplayName());
                tvUserEmail.setText(user.getEmail());
                ImageView ivUser = (ImageView) header.findViewById(R.id.ivUserPhoto);
                int size = getResources().getDimensionPixelSize(R.dimen.header_user_icon_size);
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .override(size, size)
                        .bitmapTransform(new CropCircleTransformation(this))
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .into(ivUser);
            }
        }
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cities) {
            // Handle the camera action
        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
