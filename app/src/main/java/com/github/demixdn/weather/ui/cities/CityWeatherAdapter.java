package com.github.demixdn.weather.ui.cities;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.demixdn.weather.data.model.Weather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 12.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

final class CityWeatherAdapter extends RecyclerView.Adapter<CityWeatherViewHolder> implements CityRemoveAction {

    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec

    @NonNull
    private final CityRemoveListener cityRemoveListener;
    @NonNull
    private final WeatherItemClickListener clickListener;
    private List<Weather> items;
    private List<Weather> itemsPendingRemoval;

    private final Handler handler;
    private final Map<String, Runnable> pendingRunnables;


    CityWeatherAdapter(@NonNull List<Weather> items, @NonNull CityRemoveListener removeListener, @NonNull WeatherItemClickListener clickListener) {
        this.items = items;
        cityRemoveListener = removeListener;
        this.clickListener = clickListener;
        handler = new Handler();
        pendingRunnables = new HashMap<>();
        itemsPendingRemoval = new ArrayList<>();
    }

    @Override
    public CityWeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityWeatherViewHolder(parent, clickListener);
    }

    @Override
    public void onBindViewHolder(CityWeatherViewHolder holder, int position) {
        final Weather item = getItem(position);
        if (itemsPendingRemoval.contains(item)) {
            holder.undoState(new RemoveOnClickListener(item));
        } else {
            holder.normalState(item);
        }
    }

    public Weather getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void addItems(@NonNull List<Weather> items) {
        for (Weather item : items) {
            addItem(item);
        }
    }

    @SuppressWarnings("WeakerAccess")
    void addItem(@NonNull Weather item) {
        if (items.isEmpty()) {
            items = new ArrayList<>();
        }
        if (!items.contains(item)) {
            items.add(item);
            int position = getItemCount() - 1;
            notifyItemInserted(position);
        }
    }

    @Override
    public void pendingRemoval(int position) {
        final Weather item = items.get(position);
        if (!itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(items.indexOf(item));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            String key = item.getCity().toAppString();
            pendingRunnables.put(key, pendingRemovalRunnable);
        }
    }

    @Override
    public void remove(int position) {
        Weather item = items.get(position);
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
        }
        if (items.contains(item)) {
            items.remove(position);
            notifyItemRemoved(position);
            cityRemoveListener.onCityRemoved(item.getCity());
        }
    }

    @Override
    public boolean isPendingRemoval(int position) {
        Weather item = items.get(position);
        return itemsPendingRemoval.contains(item);
    }

    private class RemoveOnClickListener implements View.OnClickListener {
        private final Weather item;

        public RemoveOnClickListener(Weather item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            String key = item.getCity().toAppString();
            Runnable pendingRemovalRunnable = pendingRunnables.get(key);
            pendingRunnables.remove(key);
            if (pendingRemovalRunnable != null) {
                handler.removeCallbacks(pendingRemovalRunnable);
            }
            itemsPendingRemoval.remove(item);
            notifyItemChanged(items.indexOf(item));
        }
    }
}
