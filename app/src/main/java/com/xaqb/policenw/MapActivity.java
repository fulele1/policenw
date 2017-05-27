package com.xaqb.policenw;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.xaqb.policenw.Utils.LogUtils;

public class MapActivity extends Activity {

    private MapView mMapView;
    private String lats;
    private String lngs;
    private String title;
    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.map_map);
        mMapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        AMap aMap=null;
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        Intent intent = getIntent();
        lats = intent.getStringExtra("Lat");
        lngs = intent.getStringExtra("Lng");
        LogUtils.i(lats+"sssss");
        LogUtils.i(lngs+"sssss");
        title = intent.getStringExtra("title");
//        lat = Double.parseDouble(lats);
//        lng = Double.parseDouble(lngs);
        LogUtils.i(lat+"");
        LogUtils.i(lng+"");
        //添加一个位置--经度，维度---marker对应一个markerOptions，用来设置marker的属性等
        if (lats.equals("0.000000")||lngs.equals("0.000000")){

            //显示蓝点
            MyLocationStyle myLocationStyle;
            myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
            myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
            aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
            aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
            aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        }else {
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng =new LatLng(lat,lng);
            markerOptions.position(latLng);
            //添加图标
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.location64));

            //添加marker
            Marker marker = aMap.addMarker(markerOptions);
            marker.setTitle(title);
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

}
