package com.github.demixdn.weather.ui.profile;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.demixdn.weather.App;
import com.github.demixdn.weather.R;
import com.github.demixdn.weather.utils.AppTypeface;
import com.github.demixdn.weather.utils.ImageHelper;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements ProfileView, View.OnClickListener {

    private TextView tvUserName;
    private TextView tvUserEmail;
    private ImageView ivUser;
    private Button btLogout;

    private ProfilePresenter presenter;
    private AppTypeface appTypeface;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public void setAppTypeface(@NonNull AppTypeface appTypeface) {
        this.appTypeface = appTypeface;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onDestroyView() {
        getPresenter().unbindView();
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        App.getInstance().getAppComponent().inject(this);
        setTypeface();
        getPresenter().viewReady();
    }

    private void initUI(View root) {
        tvUserName = (TextView) root.findViewById(R.id.tvProfileDisplayName);
        tvUserEmail = (TextView) root.findViewById(R.id.tvProfileEmail);
        ivUser = (ImageView) root.findViewById(R.id.ivProfileImage);
        btLogout = (Button) root.findViewById(R.id.btProfileLogout);
        btLogout.setOnClickListener(this);
    }

    private void setTypeface() {
        tvUserName.setTypeface(appTypeface.get(AppTypeface.Roboto.REGULAR));
        tvUserEmail.setTypeface(appTypeface.get(AppTypeface.Roboto.REGULAR));
        btLogout.setTypeface(appTypeface.get(AppTypeface.Roboto.MEDIUM));
    }

    @Override
    public void bindPresenter(ProfilePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public ProfilePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showUser(@NonNull FirebaseUser user) {
        tvUserName.setText(user.getDisplayName());
        tvUserEmail.setText(user.getEmail());
        int size = getResources().getDimensionPixelSize(R.dimen.header_user_icon_size);
        Uri userPhotoUrl = user.getPhotoUrl();
        ImageHelper.loadUserPhoto(getActivity(), size, userPhotoUrl, ivUser);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btProfileLogout) {
            getPresenter().logout();
        } else {
            throw new IllegalArgumentException("Method onClick not defined for view #" + i);
        }
    }
}
