package com.xaqb.policenw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xaqb.policenw.R;
import com.xaqb.policenw.entity.City;

import java.util.List;

/**
 * Created by lenovo on 2017/4/10.
 */

public class CityAdapter extends BaseAdapter{

    private Context mContext;
    private List<City> mCities;
    public CityAdapter(Context context,List<City> cities) {
        mContext = context;
        mCities = cities;
    }

    @Override
    public int getCount() {
        return mCities.size();
    }

    @Override
    public Object getItem(int i) {
        return mCities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CityHolder cityHolder;
        if (view==null){
            cityHolder = new CityHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.list_city,null);
            cityHolder.textView = (TextView) view.findViewById(R.id.tv_city);
            view.setTag(cityHolder);
        }else{
            cityHolder = (CityHolder) view.getTag();
        }

        cityHolder.textView.setText(mCities.get(i).getName());
        return view;
    }

    class CityHolder {
        TextView textView;
    }
}
