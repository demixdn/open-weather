package com.github.demixdn.weather.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.demixdn.weather.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnCityAddClickListener} interface
 * to handle interaction events.
 */
public class EmptyCitiesFragment extends Fragment {

    private OnCityAddClickListener cityAddClickListener;

    public EmptyCitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_empty_cities, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCityAddClickListener) {
            cityAddClickListener = (OnCityAddClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnCityAddClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cityAddClickListener = null;
    }

    public interface OnCityAddClickListener {
        void onCityAddClick();
    }
}
