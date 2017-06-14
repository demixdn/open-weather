package com.github.demixdn.weather.ui.cities;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.demixdn.weather.App;
import com.github.demixdn.weather.R;
import com.github.demixdn.weather.data.Observer;
import com.github.demixdn.weather.data.model.City;
import com.github.demixdn.weather.data.model.Weather;
import com.github.demixdn.weather.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CitiesFragment extends Fragment implements CitiesView, CityRemoveListener, Observer {


    private CitiesPresenter presenter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CityWeatherAdapter adapter;
    private WeatherItemClickListener weatherItemClickListener;

    public CitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WeatherItemClickListener) {
            weatherItemClickListener = (WeatherItemClickListener) context;
        } else {
            throw new IllegalArgumentException("Parent must implemented WeatherItemClickListener");
        }
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

    @Override
    public void onDestroyView() {
        getPresenter().unbindView();
        super.onDestroyView();
    }

    private void initUI(View root) {
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorMaterial400,
                R.color.colorMaterial300,
                R.color.colorMaterial200,
                R.color.colorMaterial100);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (presenter != null) {
                    presenter.loadWeather();
                }
            }
        });
        recyclerView = (RecyclerView) root.findViewById(R.id.rvCityItems);
    }

    private void initAdapter() {
        adapter = new CityWeatherAdapter(new ArrayList<Weather>(), this, weatherItemClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new RemoveDecoration());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        setUpItemTouchHelper();
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new CityTouchHelper(0, ItemTouchHelper.LEFT, adapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void showSwipeLayout() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    private void hideSwipeLayout() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.destroyDrawingCache();
            swipeRefreshLayout.clearAnimation();
        }
    }

    @Override
    public void showProgress() {
        showSwipeLayout();
    }

    @Override
    public void hideProgress() {
        hideSwipeLayout();
    }

    @Override
    public void showError(String errorMessage) {
        hideProgress();
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
        builder.create().show();
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

    @Override
    public void onCityRemoved(@NonNull City removedCity) {
        getPresenter().onCityRemove(removedCity);
    }

    @Override
    public void update() {
        Logger.d("Observable update CitiesFragment");
        App.getInstance().getAppComponent().inject(this);
    }
}
