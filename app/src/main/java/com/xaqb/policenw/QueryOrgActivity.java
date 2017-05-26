package com.xaqb.policenw;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Calendar;

public class QueryOrgActivity extends BaseActivity {


    private QueryOrgActivity instance;
    private EditText mEtName;
    private EditText mEtStart;
    private EditText mEtEnd;
    private ImageView mIvClear;
    private ImageView mIvClearStart;
    private ImageView mIvClearEnd;
    private ImageView mIvOrg;
    private Button mBtFinished;
    private String mName;
    private String mStart;
    private String mEnd;

    @Override
    public void initTitleBar() {
        setTitle("辖区统计查询");
    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_query_org);
        instance = this;
        mEtName = (EditText) findViewById(R.id.et_name_org);
        mEtStart = (EditText) findViewById(R.id.et_start_org);
        mEtEnd = (EditText) findViewById(R.id.et_end_org);
        mBtFinished = (Button) findViewById(R.id.bt_query_org);
        mIvClear = (ImageView) findViewById(R.id.iv_clear_org);
        mIvClearStart = (ImageView) findViewById(R.id.iv_start_clean_org);
        mIvClearEnd = (ImageView) findViewById(R.id.iv_end_clean_org);
        mIvOrg = (ImageView) findViewById(R.id.iv_org_org);
    }

    @Override
    public void initData() {
    }

    @Override
    public void addListener() {
        mBtFinished.setOnClickListener(instance);
        mIvClear.setOnClickListener(instance);
        mIvClearStart.setOnClickListener(instance);
        mIvClearEnd.setOnClickListener(instance);
        mIvOrg.setOnClickListener(instance);
        mEtStart.setOnClickListener(instance);
        mEtEnd.setOnClickListener(instance);
        mEtName.addTextChangedListener(new TextWatcher() {
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
        mEtStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.equals("")){
                    mIvClearStart.setVisibility(View.GONE);
                }else if (!charSequence.equals("")){
                    mIvClearStart.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mEtEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.equals("")){
                    mIvClearEnd.setVisibility(View.GONE);
                }else if (!charSequence.equals("")){
                    mIvClearEnd.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_clear_org://清空管辖机构名称
                mEtName.setText("");
                mIvClear.setVisibility(View.GONE);
                break;
            case R.id.iv_start_clean_org://清空开始时间
                mEtStart.setText("");
                mIvClearStart.setVisibility(View.GONE);
                break;
            case R.id.iv_end_clean_org://清空结束时间
                mEtEnd.setText("");
                mIvClearEnd.setVisibility(View.GONE);
                break;
            case R.id.iv_org_org://选择管辖机构
                Intent intent1 = new Intent(instance,SearchOrgActivity.class);
                startActivityForResult(intent1,0);
                break;
            case R.id.et_start_org://开始的时间
                chooseDatePicker();
                break;
            case R.id.et_end_org://结束的时间
                chooseDatePicker2();
                break;
            case R.id.bt_query_org://完成
                String mNameOld = mEtName.getText().toString().trim();
                if (mNameOld.contains("-")){
                    mName = mNameOld.substring(0,mNameOld.indexOf("-"));
                }else{
                    mName = mNameOld;
                }
                mStart = mEtStart.getText().toString().trim();
                mEnd = mEtEnd.getText().toString().trim();
                Intent intent = new Intent(instance,OrgDetailActivity.class);
                intent.putExtra("mName",mName);
                intent.putExtra("mStart",mStart);
                intent.putExtra("mEnd",mEnd);
                startActivity(intent);
                break;
        }
    }

    public void chooseDatePicker(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(instance, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = i1+1;
                String datainner = i+"-"+i1+"-"+i2;
                mEtStart.setText(datainner);
            }
        },year,month, day);
        datePickerDialog.show();
    }
    public void chooseDatePicker2(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(instance, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = i1+1;
                String datainner = i+"-"+i1+"-"+i2;
                mEtEnd.setText(datainner);
            }
        },year,month, day);
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode ==RESULT_OK){
            Bundle bundle = data.getExtras();
            if (bundle != null){
                String result = bundle.getString("coms");
                String code = bundle.getString("code");
                mEtName.setText(code+"-"+result);
            }
        } else if (requestCode == 1 && resultCode ==RESULT_OK){
            Bundle bundle = data.getExtras();
            if (bundle != null){
                String result = bundle.getString("startDate");
                mEtStart.setText(result);
            }
        }else if (requestCode == 2 && resultCode ==RESULT_OK){
            Bundle bundle = data.getExtras();
            if (bundle != null){
                String result = bundle.getString("endDate");
                mEtEnd.setText(result);
            }
        }
    }
}
