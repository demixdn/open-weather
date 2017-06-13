package com.github.demixdn.weather.ui;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.github.demixdn.weather.data.auth.AuthManager;
import com.github.demixdn.weather.data.auth.LoginCallback;
import com.github.demixdn.weather.utils.AppTypeface;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, LoginCallback {

    private AppTypeface appTypeface;
    private AuthManager authManager;
    private ProgressDialog progressDialog;

    public static void navigate(@NonNull Activity context, int requestCode) {
        Intent starter = new Intent(context, SignInActivity.class);
        context.startActivityForResult(starter, requestCode);
    }

    public void setAppTypeface(@NonNull AppTypeface appTypeface) {
        this.appTypeface = appTypeface;
    }

    public void setAuthDelegate(@NonNull AuthManager authManager) {
        this.authManager = authManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        App.getInstance().getAppComponent().inject(this);
        initUI();
        authManager.bind(this);
    }

    private void initUI() {
        AppCompatButton btFacebookLogin = (AppCompatButton) findViewById(R.id.btFbLogin);
        TextView tvSignInTitle = (TextView) findViewById(R.id.tvSignTitles);
        btFacebookLogin.setOnClickListener(this);
        btFacebookLogin.setTypeface(appTypeface.get(AppTypeface.Roboto.MEDIUM));
        tvSignInTitle.setTypeface(appTypeface.get(AppTypeface.Roboto.REGULAR));
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.auth_progress));
    }

    @Override
    protected void onDestroy() {
        authManager.unbind();
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
        authManager.login(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void onAuthSuccess(@NonNull FirebaseUser user) {
        progressDialog.dismiss();
        Toast.makeText(this, getString(R.string.login_success, user.getDisplayName()), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void onAuthError(Exception ex) {
        progressDialog.dismiss();
        Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show();
    }
}
