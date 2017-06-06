package com.github.demixdn.weather.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.demixdn.weather.App;
import com.github.demixdn.weather.R;
import com.github.demixdn.weather.data.FacebookLogin;
import com.github.demixdn.weather.data.LoginCallback;
import com.github.demixdn.weather.utils.AppTypeface;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, LoginCallback {

    private AppTypeface appTypeface;
    private FacebookLogin facebookLogin;
    private ProgressDialog progressDialog;

    public static void navigate(@NonNull Context context) {
        Intent starter = new Intent(context, SignInActivity.class);
        context.startActivity(starter);
    }

    public void setAppTypeface(@NonNull AppTypeface appTypeface) {
        this.appTypeface = appTypeface;
    }

    public void setFacebookLoginDelegate(@NonNull FacebookLogin facebookLogin) {
        this.facebookLogin = facebookLogin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        App.getInstance().getAppComponent().inject(this);
        initUI();
        facebookLogin.bind(this);
    }

    private void initUI() {
        AppCompatButton btFacebookLogin = (AppCompatButton) findViewById(R.id.btFbLogin);
        TextView tvSignInTitle = (TextView) findViewById(R.id.tvSignTitles);
        btFacebookLogin.setOnClickListener(this);
        btFacebookLogin.setTypeface(appTypeface.get(AppTypeface.Roboto.MEDIUM));
        tvSignInTitle.setTypeface(appTypeface.get(AppTypeface.Roboto.REGULAR));
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Auth in progress");
    }

    @Override
    protected void onDestroy() {
        facebookLogin.unbind();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btFbLogin) {
            loginAction();
        } else {
            throw new IllegalArgumentException("Method onClick not defined for view #" + i);
        }
    }

    private void loginAction() {
        facebookLogin.login(this);
        progressDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookLogin.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAuthSuccess(@NonNull FirebaseUser user) {
        progressDialog.dismiss();
        Toast.makeText(this, "Auth success " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onAuthError(Exception ex) {
        progressDialog.dismiss();
        Toast.makeText(this, "Auth error", Toast.LENGTH_SHORT).show();
    }
}