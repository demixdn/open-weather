package com.github.demixdn.weather.ui.cities;

/**
 * Created on 14.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

interface CityRemoveAction {
    void pendingRemoval(int position);

    void remove(int position);

    boolean isPendingRemoval(int position);
}
