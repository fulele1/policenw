package com.xaqb.policenw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xaqb.policenw.adapter.CityAdapter;
import com.xaqb.policenw.entity.City;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择城市
 */
public class ChooseActivity extends BaseActivity {
    private ChooseActivity instance;
    private ListView mLv;
    private List<City> mCity;
    private SharedPreferences preferences;
    @Override
    public void initTitleBar() {
        setTitle("请选择城市");
    }
    @Override
    public void initViews() {
        preferences = this.getSharedPreferences("times.xml",MODE_PRIVATE);
        Boolean times = preferences.getBoolean("first",true);
        if (times){
            setContentView(R.layout.activity_choose);
            instance = this;
            mLv = (ListView) findViewById(R.id.list_choose);
        }else{
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }

    @Override
    public void initData() {
        String [] citys = {"西安","重庆","四川凉山州","东营","泰安","奈曼旗","渭南","蚌埠"};
        String [] urls = {
                "http://xawz.xaqianbai.net:8090",
                "http://ejd.cqlandun.com",
                "http://lsz.xaqianbai.net",
                "http://dongying.xaqianbai.net:9094",
                "http://taian.xaqianbai.net:9098",
                "http://nmq.xaqianbai.net:9001",
                "http://wn.xaqianbai.net:8070",
                "http://bb.xaqianbai.net"};
        mCity = new ArrayList<>();
        for (int i = 0;i<citys.length;i++){
            City city = new City();
            city.setName(citys[i]);
            city.setUrl(urls[i]);
            mCity.add(city);
        }
    }

    @Override
    public void addListener() {
        CityAdapter cityAdapter = new CityAdapter(instance,mCity);
        mLv.setAdapter(cityAdapter);
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("first",false);
                editor.commit();
                Intent intent = new Intent(instance,LoginActivity.class);
                writeConfig("city",mCity.get(i).getName());
                writeConfig("urls",mCity.get(i).getUrl());
                instance.startActivity(intent);
                instance.finish();
            }
        });
    }
}
