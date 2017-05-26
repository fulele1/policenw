package com.xaqb.policenw;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xaqb.policenw.Utils.ChangeUtil;
import com.xaqb.policenw.Utils.GsonUtil;
import com.xaqb.policenw.Utils.HttpUrlUtils;
import com.xaqb.policenw.adapter.PerAdapter;
import com.xaqb.policenw.entity.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 快递员详情
 */
public class PerActivity extends BaseActivity implements OnDataFinishedLinstern{
    private List<Person> mPeople;
    private PerActivity instance;
    private ListView mList;
    private TextView mCount;
    private PerAdapter mAdapter;

    @Override
    public void initTitleBar() {
        setTitle("快递员");
        showBackwardView(true);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_per);
        instance = this;
        mList = (ListView) findViewById(R.id.list_per);
        mList.setDivider(new ColorDrawable(Color.GRAY));
        mList.setDividerHeight(1);
        mCount = (TextView) findViewById(R.id.tv_count);
    }

    @Override
    public void initData() {
    }

    /**
     *
     * @return 返回快递员详情的接口参数
     */


    public String getData(){
        Intent intent = instance.getIntent();
        String com = intent.getStringExtra("com");
        String per = intent.getStringExtra("per");
        String phone = intent.getStringExtra("phone");
        String ide = intent.getStringExtra("ide");
        String org = intent.getStringExtra("org");

        return "&comname="+com+
                "&name="+per+
                "&mp="+phone+
                "&cert="+ide+
                "&comorg="+org;
    }

    @Override
    public void addListener() {
        setOnDataFinishedLinstern(instance);
        getOkConnection(readConfig("urls")+HttpUrlUtils.getHttpUrl().get_query_per()+getData());
    }

    @Override
    public void dataFinishedLinstern(String s) {
        mPeople = new ArrayList<>();
        if (s.startsWith("0")){
            //响应成功
            String str = ChangeUtil.procRet(s);
            str = str.substring(1,str.length());
            List<Map<String ,Object>> data = GsonUtil.GsonToListMaps(str);
            for (int i = 0;i < data.size();i++){
            Person person = new Person();
            person.setName(data.get(i).get("empname").toString());
            person.setSix(data.get(i).get("sexname").toString());
            person.setNum(data.get(i).get("empphone").toString());
            person.setIde(data.get(i).get("empcertcode").toString());
            person.setComeCode(data.get(i).get("empcode").toString());
            person.setCom(data.get(i).get("comname").toString());
            mPeople.add(person);
            }
        }else{
            //响应失败
            Toast.makeText(instance, "未查询到有效数据", Toast.LENGTH_SHORT).show();
        }

        loadingDialog.dismiss();
        addEvent();
    }


    /**
     * 添加事件
     */
    private void addEvent() {
        mCount.setText(mPeople.size()+"");
        mAdapter = new PerAdapter(instance,mPeople);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(instance,PerDetailActivity.class);
                intent.putExtra("empcode",mPeople.get(i).getComeCode());
                startActivity(intent);
            }
        });
    }
}
