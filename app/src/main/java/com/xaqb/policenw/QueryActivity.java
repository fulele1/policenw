package com.xaqb.policenw;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xaqb.policenw.Utils.ChangeUtil;
import com.xaqb.policenw.Utils.GsonUtil;
import com.xaqb.policenw.Utils.HttpUrlUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;


/**
 * Created by lenovo on 2017/3/1
 * 扫码获取到运单编号后自动跳转到查询订单的界面
 */
public class QueryActivity extends BaseActivity {
    private QueryActivity instance;
    private Button btQuery;
    private TextView mTvExpressType;//收寄类型
    private TextView mTvCode;//运单号
    private TextView mTvName;//物品名称
    private TextView mTvMancertCode;//证件号码
    private TextView mTvTime;//运单时间
    private TextView mTvAddress;//寄件地址
    private TextView mTvPhone;//联系电话
    private TextView mTvDestPhone;//收件人电话
    private TextView mTvCompany;//企业名称
    private TextView mTvPerson;//从业人员名称



    @Override
    public void initTitleBar() {
        setTitle("快递单查询");
        showBackwardView(true);

    }

    @Override
    public void initViews() {
        setContentView(R.layout.query_activity);
        instance = this;
        assignViews();
    }

    private void assignViews() {
        btQuery = (Button) findViewById(R.id.bt_query_query);
        mTvCode = (TextView) findViewById(R.id.txt_code_query);
        mTvExpressType = (TextView) findViewById(R.id.txt_type_query);
        mTvName = (TextView) findViewById(R.id.txt_name_query);
        mTvMancertCode = (TextView) findViewById(R.id.txt_mancert_code_query);
        mTvTime = (TextView) findViewById(R.id.txt_time_query);
        mTvAddress = (TextView) findViewById(R.id.txt_address_query);
        mTvPhone = (TextView) findViewById(R.id.txt_phone_query);
        mTvDestPhone = (TextView) findViewById(R.id.txt_des_phone_query);
        mTvCompany = (TextView) findViewById(R.id.txt_company_query);
        mTvPerson = (TextView) findViewById(R.id.txt_per_query);
    }

    @Override
    public void initData() {
        Intent intent = instance.getIntent();
        String code = intent.getStringExtra("code");
        okHttp(code);

    }

    @Override
    public void addListener() {
        btQuery.setOnClickListener(instance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_query_query:
                if (mTvCode.getText()!=null){
                    Intent intent = new Intent(instance, QueryWebviewActivity.class);
                    String s = mTvCode.getText().toString();
                    if (s.contains("(")){
                        intent.putExtra("url", "https://m.kuaidi100.com/index_all.html?postid=" + s.substring(0,s.indexOf("(")));
                    }else{
                        intent.putExtra("url", "https://m.kuaidi100.com/index_all.html?postid=" + mTvCode.getText());
                    }
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void okHttp(final String string){

        loadingDialog.show("正在连接，请稍等");

        //进行网络请求查询订单
        OkHttpUtils
                .get()
                .url(readConfig("urls")+HttpUrlUtils.getHttpUrl().quer_yCode() + "&code=" + string)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        loadingDialog.dismiss();
                        showToast("网络访问异常");
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        if (s.startsWith("0")) {
                            //suc
                            loadingDialog.dismiss();
                            String str = ChangeUtil.procRet(s);//{"policeid":"xaqianbai","policename":"西安千百","socode":"610100000000","soname":"西安市公安局"}
                            str = str.substring(1, str.length());
                            Map<String, Object> data = GsonUtil.JsonToMap(str);
                            if (!data.get("expresstype").toString().equals("")) {
                                mTvExpressType.setText(data.get("expresstype").toString());//投递类型
                                mTvCode.setText(string);
                                mTvName.setText(data.get("name").toString());//物品名称
                                mTvMancertCode.setText(data.get("mancertcode").toString());//证件号码
                                mTvTime.setText(data.get("mantime").toString());//时间
                                mTvAddress.setText(data.get("address").toString());//收寄地址
                                mTvPhone.setText(data.get("manphone").toString());//联系电话
                                mTvCompany.setText(data.get("comname").toString());//公司名称
                                mTvPerson.setText(data.get("empname").toString());//从业人员名称
                                if (data.get("expresstype").toString().equals("收寄")){
                                    mTvDestPhone.setText(data.get("destphone").toString());//收件人电话（只在收寄时有该字段）
                                }
                            } else {
                                mTvCode.setText(string + "查无此单");
                               // btQuery.setVisibility(View.GONE);
                            }
                        } else {
                            //failure
                            loadingDialog.dismiss();
                            mTvCode.setText(string + "(查无此单)");
                           //btQuery.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
