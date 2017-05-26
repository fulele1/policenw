package com.xaqb.policenw;


import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.xaqb.policenw.Utils.ChartUtil;
import com.xaqb.policenw.Utils.GsonUtil;
import com.xaqb.policenw.Utils.HttpUrlUtils;
import com.xaqb.policenw.Utils.LogUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrgDetailActivity extends BaseActivity implements OnDataFinishedLinstern{

    private OrgDetailActivity instance;
    private String mName;
    private String mStart;
    private String mEnd;
    private TextView mTvName;
    private TextView mTvBrand;
    private TextView mTvCom;
    private TextView mTvPer;
    private TextView mTvPostCount;
    private TextView mTvReCount;
    private LineChart mChart;
    private List<Double> at;
    private List<Double> dv;
    private List<String> keys;
    private Double atCount = 0.0;
    private Double dvCount = 0.0;

    @Override
    public void initTitleBar() {
        setTitle("辖区详情");
    }


    @Override
    public void initViews() {
        setContentView(R.layout.activity_org_detail);
        instance = this;
        mTvName = (TextView) findViewById(R.id.tv_name_org);
        mTvBrand = (TextView) findViewById(R.id.tv_brand_org);
        mTvCom = (TextView) findViewById(R.id.tv_com_org);
        mTvPer = (TextView) findViewById(R.id.tv_per_org);
        mTvPostCount = (TextView) findViewById(R.id.tv_post_count_org);
        mTvReCount = (TextView) findViewById(R.id.tv_receive_count_org);
        mChart = (LineChart) findViewById(R.id.chart_org_de);
        Intent intent = getIntent();
        mName  = intent.getStringExtra("mName");
        mStart = intent.getStringExtra("mStart");
        mEnd = intent.getStringExtra("mEnd");
    }

    @Override
    public void initData() {
    }

    @Override
    public void addListener() {
        setOnDataFinishedLinstern(instance);
        /*{socode=610100000000,//机构编号
            soname=西安市公安局,//机构名称
            bnum=0,//品牌数量     comnum=3, //企业数量   atnum=1, //收寄数量   dvnum=2,//投递数量 empnum=6, //从业人员数量
        data={  2017-05-06={atnum=0.0, dvnum=0.0},
                2017-05-07={atnum=0.0, dvnum=0.0},
                2017-05-08={atnum=0.0, dvnum=0.0},
                2017-05-09={atnum=0.0, dvnum=0.0},
                2017-05-10={atnum=0.0, dvnum=0.0},
                2017-05-11={atnum=1.0, dvnum=2.0},
                2017-05-12={atnum=0.0, dvnum=0.0}}}*/
        getOkConnection(readConfig("urls")+HttpUrlUtils.getHttpUrl().get_query_org_detail()+"&tdate="+mStart+"&edate="+mEnd+"&comorg="+mName);
    }
    @Override
    public void dataFinishedLinstern(String s) {
        LogUtils.i(s);
        if (s.startsWith("0")){
            //响应成功
            String str = s.split(String.valueOf((char) 1))[1];
            Map<String,Object> data = GsonUtil.GsonToMaps(str);
            mTvName.setText(data.get("soname").toString());
            mTvBrand.setText(data.get("bnum").toString());
            mTvCom.setText(data.get("comnum").toString());
            mTvPer.setText(data.get("empnum").toString());
            mTvPostCount.setText(data.get("dvnum").toString());
            mTvReCount.setText(data.get("atnum").toString());
            getData(data);
        }else{
            //响应失败
            Toast.makeText(instance, "未查询到有效数据", Toast.LENGTH_SHORT).show();
        }
        loadingDialog.dismiss();
    }

    /**
     * 解析count
     * @param data
     */

    public void getData(Map<String ,Object> data){
        Map<String ,Object> data2 = GsonUtil.GsonToMaps(data.get("data").toString());
        at = new ArrayList<>();
        dv = new ArrayList<>();
        keys = new ArrayList<>();

        Set<String> keySet = data2.keySet();
        Iterator iterator= keySet.iterator();
        while (iterator.hasNext()) {
            keys.add((String) iterator.next());
        }

        for(int i = 0;i<keys.size();i++){
            Map<String ,Object> day = GsonUtil.GsonToMaps(data2.get(keys.get(i)).toString());
            at.add((Double)day.get("atnum"));
            dv.add((Double)day.get("dvnum"));
        }

        for (int i = 0;i<at.size();i++){
            atCount +=at.get(i);
        }

        for (int i = 0;i<dv.size();i++){
            dvCount +=dv.get(i);
        }

        LogUtils.i(atCount+"");
        LogUtils.i(dvCount+"");

        // 获取完数据之后 制作7个数据点（沿x坐标轴）
        LineData mLineData = ChartUtil.makeLineData(7,dv,at,keys,"投递", Color.BLUE,"收寄",Color.RED);
        ChartUtil.setChartStyle(mChart, mLineData, Color.WHITE);
    }

}
