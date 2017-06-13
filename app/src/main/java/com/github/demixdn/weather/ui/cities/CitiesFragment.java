package com.github.demixdn.weather.ui.cities;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.demixdn.weather.App;
import com.github.demixdn.weather.R;
import com.github.demixdn.weather.data.model.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CitiesFragment extends Fragment implements CitiesView {


    private CitiesPresenter presenter;
    private RecyclerView recyclerView;
    private CityWeatherAdapter adapter;

    private ProgressDialog progressDialog;

    public CitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cities, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        App.getInstance().getAppComponent().inject(this);
        initAdapter();
        getPresenter().loadWeather();
    }

    private void initUI(View root) {
        recyclerView = (RecyclerView) root.findViewById(R.id.rvCityItems);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Load weather for cities");
    }

    private void initAdapter() {
        adapter = new CityWeatherAdapter(new ArrayList<Weather>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.dialog_error_title);
        builder.setMessage(R.string.dialog_error_default_message_retry);
        builder.setPositiveButton(R.string.dialog_action_retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getPresenter().loadWeather();
                dialog.dismiss();
            }
        });
        builder.setNeutralButton(R.string.dialog_action_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void bindPresenter(CitiesPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public CitiesPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showWeather(List<Weather> weathers) {
        adapter.addItems(weathers);
    }
}
