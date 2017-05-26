package com.xaqb.policenw;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.xaqb.policenw.Utils.ChangeUtil;
import com.xaqb.policenw.Utils.GsonUtil;
import com.xaqb.policenw.Utils.HttpUrlUtils;
import com.xaqb.policenw.Utils.LogUtils;
import com.xaqb.policenw.Utils.SPUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

/**
 * Created by lenovo on 2017/3/6.
 */
public class LoginActivity extends BaseActivity {
    private LoginActivity instance;
    private Button btLogin;
    private String username, psw, pswmd5;
    private EditText etUsername, etPsw;
    private CheckBox cbRememberPsw;
    private Intent intent;
    private TextView mTxtVersion;
    private TextView mTxtCity;
    SharedPreferences oData;

    @Override
    public void initTitleBar() {
      setTitleBarVisible(View.GONE);
    }

    @Override
    public void initViews() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.login_activity);
        instance = this;
        assignViews();
    }


    private void assignViews() {
        btLogin = (Button) findViewById(R.id.bt_login);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPsw = (EditText) findViewById(R.id.et_password);
        cbRememberPsw = (CheckBox) findViewById(R.id.cb_remember_psw);
        mTxtVersion = (TextView) findViewById(R.id.txt_version_login);
        mTxtCity = (TextView) findViewById(R.id.tv_city_login);
        mTxtVersion.setText("V"+getVersionName());
        mTxtCity.setText(readConfig("city")+"市公安局");
    }

    @Override
    public void initData() {
        username = (String) SPUtils.get(instance, "userName", "");
        psw = (String) SPUtils.get(instance, "userPsw", "");
        boolean rememberPsw = (boolean) SPUtils.get(instance, "rememberPsw", false);
        if (rememberPsw) cbRememberPsw.setChecked(true);
        if (username != null && !username.isEmpty()) {
            etUsername.setText(username);
        }
        if (psw != null && !psw.isEmpty()) {
            etPsw.setText(psw);
        }
    }

    @Override
    public void addListener() {
        btLogin.setOnClickListener(instance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                login();
                break;
        }
    }

    private void login() {
        username = etUsername.getText().toString().trim();
        psw = etPsw.getText().toString().trim();
        pswmd5 = ChangeUtil.md5(psw);
        if (username == null || username.equals("")) {
            showToast("请输入账号");
        } else if (psw == null || psw.equals("")) {
            showToast("请输入密码");
        } else {
            loadingDialog.show("正在登陆");
            OkHttpUtils
                    .get()
                    .url(readConfig("urls")+HttpUrlUtils.getHttpUrl().user_login() + "&user=" + username + "&pwd=" + pswmd5)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(okhttp3.Call call, Exception e, int i) {
                            loadingDialog.dismiss();
                            showToast("网络访问异常");
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            //0{"policeid":"xaqianbai","policename":"西安千百","socode":"610100000000","soname":"西安市公安局"}000
                            if (s.startsWith("0")) {
                                //suc
                                String str = ChangeUtil.procRet(s);
                                str = str.substring(1, str.length());//{"policeid":"xaqianbai","policename":"西安千百","socode":"610100000000","soname":"西安市公安局"}
                                LogUtils.i(str);
                                Map<String, Object> data = GsonUtil.JsonToMap(str);
                                SharedPreferences oData = getSharedPreferences("user", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor oEdit = oData.edit();//获得编辑器
                                oEdit.putString("name", data.get("policename").toString());
                                oEdit.putString("city", data.get("soname").toString());
                                oEdit.putString("org", data.get("soname").toString());
                                oEdit.putString("coms", data.get("companynum").toString());
                                oEdit.putString("pers", data.get("employeenum").toString());
                                oEdit.putString("express", data.get("todayexpress").toString());
                                oEdit.commit();//提交内容
                                startActivity(new Intent(instance, TotalActivity.class));
                                finish();
                            } else {
                                //failure
                                showToast("输入密码错误，请重新输入");
                            }
                            loadingDialog.dismiss();
                            if (cbRememberPsw.isChecked()) {
                                SPUtils.put(instance, "userName", username);
                                SPUtils.put(instance, "userPsw", psw);
                                SPUtils.put(instance, "rememberPsw", true);
                            } else {
                                SPUtils.put(instance, "userName", "");
                                SPUtils.put(instance, "userPsw", "");
                                SPUtils.put(instance, "rememberPsw", false);
                            }
                        }
                    });
        }
    }
}
