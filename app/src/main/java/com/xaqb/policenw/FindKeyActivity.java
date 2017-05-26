package com.xaqb.policenw;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xaqb.policenw.Utils.ChangeUtil;
import com.xaqb.policenw.Utils.HttpUrlUtils;
import com.xaqb.policenw.Utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
* @author le_f on 2016/11/22.
* */
public class FindKeyActivity extends BaseActivity {

    String oldmd5;
    String pswmd5;
    String confirmPswmd5;
    private Button btComplete;
    private FindKeyActivity instance;
    private EditText etOld, etPsw, etConfirmPsw;
    private String old, psw, confirmPsw;

    @Override
    public void initTitleBar() {
        setTitle(R.string.reset_psw);
        showBackwardView(true);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_find_key);
        instance = this;
        assignViews();
    }

    private void assignViews() {
        etOld = (EditText) findViewById(R.id.et_oldpwd_find);
        etPsw = (EditText) findViewById(R.id.et_password);
        etConfirmPsw = (EditText) findViewById(R.id.et_confirm_psw);
        btComplete = (Button) findViewById(R.id.bt_complete);
    }

    @Override
    public void initData() {


    }

    @Override
    public void addListener() {
        btComplete.setOnClickListener(instance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_complete:
                resetPsw();
                break;
        }
    }

    private void resetPsw() {
        old = etOld.getText().toString().trim();
        oldmd5 = ChangeUtil.md5(old);
        psw = etPsw.getText().toString().trim();
        pswmd5 = ChangeUtil.md5(psw);
        confirmPsw = etConfirmPsw.getText().toString().trim();
        confirmPswmd5 = ChangeUtil.md5(confirmPsw);
//        if (phone == null || phone.equals("")) {
//            showToast("请输入手机号码");
//        } else if (vCode == null || vCode.equals("")) {
//            showToast("请输入验证码");
//        }
        if(old.equals("")){
            showToast("请输入旧密码");
        }
        else if (psw.equals("")) {
            showToast("请输入新密码");
        } else if (confirmPsw.equals("")) {
            showToast("请输入确认密码");
        } else if (!psw.equals(confirmPsw)) {
            showToast("两次输入的密码不一致");
        } else {
//            RequestParams requestParams = new RequestParams();
//            requestParams.addBodyParameter("tel", phone);
//            requestParams.addBodyParameter("new_password", psw);
//            requestParams.addBodyParameter("confirm_password", confirmPsw);
//            requestParams.addBodyParameter("code", vCode);
//            requestParams.addBodyParameter("codekey", codeKey);
//            ajax(requestParams, HttpUrlUtils.getHttpUrl().reset_password());
//            LogUtils.i(HttpUrlUtils.getHttpUrl().reset_password());

            loadingDialog.show("正在修改");

            OkHttpUtils
                    .get()
                    .url(readConfig("urls")+HttpUrlUtils.getHttpUrl().back_password() + "&old=" + oldmd5 + "&new=" + pswmd5)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            LogUtils.i(old);
                            LogUtils.i(psw);
                            LogUtils.i(HttpUrlUtils.getHttpUrl().back_password() + "&old=" + oldmd5 + "&new=" + pswmd5);
                            LogUtils.i(s);

                            if (s.startsWith("0")) {
                                //suc
//                                String str = ChangeUtil.procRet(s);
//                                str = str.substring(1,str.length());
//                                LogUtils.i(str);
//                                Map<String,Object> data =  GsonUtil.JsonToMap(str);

                                showToast("找回密码成功");

                                instance.startActivity(new Intent(instance,LoginActivity.class));
                                instance.finish();
                            }
                            if (s.startsWith("100")) {
                                showToast("登陆超时，请重新登陆");
                                finish();
                            } else {
                                showToast("原始密码错误，请从新输入");
                            }

                            loadingDialog.dismiss();
                        }
                    });
        }
    }


}
