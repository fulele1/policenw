package com.xaqb.policenw;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


/**
 * 企业查询界面
 */
public class QueryComActivity extends BaseActivity {
    private QueryComActivity instance;
    private Button mBtQuery;
    private ImageView mIvComs;
    private ImageView mIvClear;
    private ImageView mIvClearOrg;
    private ImageView mIvOrg;
    private EditText mEdComs;
    private EditText mEdCom;
    private EditText mEdOrg;

    @Override
    public void initTitleBar() {
        setTitle("企业查询");
        showBackwardView(true);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_query_com);
        instance = this;
        asSignView();
    }

    private void asSignView() {
        mBtQuery = (Button) findViewById(R.id.bt_query_com);
        mIvComs = (ImageView) findViewById(R.id.iv_coms_com);
        mIvClear = (ImageView) findViewById(R.id.iv_clear_com);
        mIvClearOrg = (ImageView) findViewById(R.id.iv_clear_org_com);
        mIvOrg = (ImageView) findViewById(R.id.iv_org_com);
        mEdComs = (EditText) findViewById(R.id.et_coms_com);
        mEdCom = (EditText) findViewById(R.id.et_com_com);
        mEdOrg = (EditText) findViewById(R.id.et_org_com);
    }


    @Override
    public void initData() {
    }


    @Override
    public void addListener() {
        mBtQuery.setOnClickListener(instance);
        mIvComs.setOnClickListener(instance);
        mIvClear.setOnClickListener(instance);
        mEdOrg.setOnClickListener(instance);
        mIvClearOrg.setOnClickListener(instance);
        mIvOrg.setOnClickListener(instance);
        mEdComs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.equals("")){
                    mIvClear.setVisibility(View.GONE);
                }else if (!charSequence.equals("")){
                    mIvClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mEdOrg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.equals("")){
                    mIvClearOrg.setVisibility(View.GONE);
                }else if (!charSequence.equals("")){
                    mIvClearOrg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onClick(View v) {

        String comscode = mEdComs.getText().toString();
        String com = mEdCom.getText().toString();
        String org = mEdOrg.getText().toString();
        switch (v.getId()) {
            case R.id.bt_query_com://查询按钮

                if (comscode.equals("")&&com.equals("")&&org.equals("")){//空空空
                    showToast("请输入查询条件");
                }else if (comscode.equals("")&&!com.equals("")&&org.equals("")){//空不空
                    toIntent(comscode,com,org);
                } else if (!comscode.equals("")&&com.equals("")&&org.equals("")){//不空空
                    toIntent(comscode.substring(0,comscode.indexOf("-")),com,org);
                }else if (comscode.equals("")&&com.equals("")&&!org.equals("")){//空空不/
                    toIntent(comscode,com,org.substring(0,org.indexOf("-")));
                }else if (!comscode.equals("")&&!com.equals("")&&org.equals("")){//不不空
                    toIntent(comscode.substring(0,comscode.indexOf("-")),com,org);
                }else if (comscode.equals("")&&!com.equals("")&&!org.equals("")){//空不不
                    toIntent(comscode,com,org.substring(0,org.indexOf("-")));
                }else if (!comscode.equals("")&&!com.equals("")&&!org.equals("")){//不不不
                    toIntent(comscode.substring(0,comscode.indexOf("-")),com,org.substring(0,org.indexOf("-")));
                }else if (!comscode.equals("")&&com.equals("")&&!org.equals("")){//不空不/
                    toIntent(comscode.substring(0,comscode.indexOf("-")),com,org.substring(0,org.indexOf("-")));
                }


                break;
            case R.id.iv_coms_com://选择品牌
                Intent intent = new Intent(instance,SearchComsActivity.class);
                startActivityForResult(intent,2);
                break;
            case R.id.iv_clear_com://清空选择企业编辑框
                mEdComs.setText("");
                mIvClear.setVisibility(View.GONE);
                break;
            case R.id.iv_clear_org_com://清空选择管辖机构编辑框
                mEdOrg.setText("");
                mIvClearOrg.setVisibility(View.GONE);
                break;
            case R.id.iv_org_com://选择管辖机构
                Intent intent2 = new Intent(instance,SearchOrgActivity.class);
                startActivityForResult(intent2,3);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==2 && resultCode==RESULT_OK){

            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("coms");
                String code = bundle.getString("code");
                mEdComs.setText(code+"-"+result);
            }
        }else if (requestCode==3 && resultCode==RESULT_OK){

            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("coms");
                String code = bundle.getString("code");
                mEdOrg.setText(code+"-"+result);
            }
        }
    }

    public void toIntent(String comscode,String com,String org){
        Intent intent = new Intent(instance,ComActivity.class);
        intent.putExtra("comscode",comscode);
        intent.putExtra("com",com);
        intent.putExtra("org",org);
        startActivity(intent);
    }
}
