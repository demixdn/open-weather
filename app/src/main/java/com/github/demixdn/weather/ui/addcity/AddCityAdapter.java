package com.github.demixdn.weather.ui.addcity;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 08.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

class AddCityAdapter extends BaseAdapter implements Filterable {

    private List<String> originalData;
    private List<String> filteredData;
    private CityFilter filter;

    AddCityAdapter(@NonNull List<String> cities) {
        this.originalData = cities;
        this.filteredData = new ArrayList<>(cities);
        this.filter = new CityFilter();
    }


    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public String getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View result = convertView;
        if (result == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null);
            holder = new ViewHolder((TextView) result);
            result.setTag(holder);
        } else {
            holder = (ViewHolder) result.getTag();
        }
        holder.setText(getItem(position));
        return result;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public void destroyData() {
        originalData = null;
        filteredData = null;
        System.gc();
    }

    private static class ViewHolder {
        private TextView text;

        ViewHolder(TextView text) {
            this.text = text;
        }

        public void setText(@NonNull String text) {
            this.text.setText(text);
        }
    }

    private class CityFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                ArrayList<String> mSuggestions = new ArrayList<>(originalData);

                mSuggestions.clear();

                for (String value : originalData) {
                    if (value.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        mSuggestions.add(value);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mSuggestions;
                filterResults.count = mSuggestions.size();
                return filterResults;
            } else {
                return null;
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null && results.count > 0) {
                filteredData = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
