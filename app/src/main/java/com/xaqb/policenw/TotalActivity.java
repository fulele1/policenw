package com.xaqb.policenw;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xaqb.policenw.Other.CheckNetwork;
import com.xaqb.policenw.Other.MyApplication;
import com.xaqb.policenw.Other.SendService;
import com.xaqb.policenw.Utils.HttpUrlUtils;
import com.xaqb.policenw.Utils.LogUtils;
import com.xaqb.policenw.Utils.ProcUnit;
import com.xaqb.policenw.zxing.activity.CaptureActivity;

import java.io.File;

/**
 * 主界面
 */

public class TotalActivity extends BaseActivity {

    protected String FsUrl = "";
    protected String FsUser = "";
    protected String FsRet = "";
    protected AlertDialog FoWait = null;
    protected String FsFile = "";
    protected ProgressBar FoBar = null;
    protected String FsVersion = "";
    protected boolean FbUpdate = false;
    protected boolean FbForceUpdate = false;
    static boolean FbForceRight = false;
    private TotalActivity instance;
    private TextView mTxtPer;
    private TextView mTxtCome;
    private TextView mTxtModify;
    private TextView mTxtOrg;
    private TextView mTxtUpdate;
    private TextView mTxtUser;
    private TextView mTxtUserOrg;
    private TextView mTxtComCount;
    private TextView mTxtPerCount;
    private TextView mTxtTransport;
    private TextView mTxtCity;
    private ImageView ivZxing;
    private ImageView ivChaCha;
    private EditText etOrderNum;
    @Override
    public void initTitleBar() {
        setTitleBarVisible(View.GONE);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_total);
        instance = this;
        assignViews();
        SharedPreferences oData = getSharedPreferences("user", Activity.MODE_PRIVATE);
        mTxtUser.setText(oData.getString("name","无名"));
        mTxtCity.setText(oData.getString("city",""));
        mTxtUserOrg.setText(oData.getString("org",""));
        mTxtComCount.setText(oData.getString("coms",""));
        mTxtPerCount.setText(oData.getString("pers",""));
        mTxtTransport.setText(oData.getString("express",""));

    }

    private void assignViews() {
        ivZxing = (ImageView) findViewById(R.id.iv_zxing);
        ivChaCha = (ImageView) findViewById(R.id.iv_chacha_total);
        etOrderNum = (EditText) findViewById(R.id.et_order_total);
        etOrderNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        mTxtPer = (TextView) findViewById(R.id.txt_per_total);
        mTxtCome = (TextView) findViewById(R.id.txt_com_total);
        mTxtModify = (TextView) findViewById(R.id.txt_modify_total);
        mTxtOrg = (TextView) findViewById(R.id.txt_org_total);
        mTxtUpdate = (TextView) findViewById(R.id.txt_update_total);
        mTxtUser = (TextView) findViewById(R.id.tv_user_total);
        mTxtCity = (TextView) findViewById(R.id.tv_city_total);
        mTxtUserOrg = (TextView) findViewById(R.id.tv_user_org_total);
        mTxtComCount = (TextView) findViewById(R.id.tv_com_count_total);
        mTxtPerCount = (TextView) findViewById(R.id.tv_per_count_total);
        mTxtTransport = (TextView) findViewById(R.id.tv_transport_count_total);
    }

    @Override
    public void initData() {

        FsUrl=readConfig("url");
        if(FsUrl.length()==0)
        {
        FsUrl= HttpUrlUtils.getHttpUrl().get_updata()+"/versionjy.txt";
			 writeConfig("url",FsUrl);
        }
        FsUrl = readConfig("url");
        FsUser = readConfig("user");
        FsFile = "/sdcard/update/policenw.apk";
        FbUpdate = false;
        getVersion();
        checkRight();
    }

    /**
     * 检查用户验证状态
     */
    private void checkRight() {
        if (!CheckNetwork.isNetworkAvailable(MyApplication.instance)) {
            showMess("网络未连接", false);
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    Log.w("error", FsUrl);
                    String sRet = ProcUnit.httpCheckRight(FsUrl, FsUser, readConfig("right"));
                    Log.i("check_right_result", "" + sRet);
                    if (sRet.equals("0ok")) {
                        FbForceRight = false;
                    } else if (sRet.startsWith("2")) {
                        FbForceRight = false;
                    } else {
                        FbForceRight = true;
                    }
                } catch (Exception E) {
                    FbForceRight = false;
                }
            }
        }).start();
    }


    @Override
    public void addListener() {
        ivZxing.setOnClickListener(instance);
        mTxtPer.setOnClickListener(instance);
        mTxtCome.setOnClickListener(instance);
        mTxtModify.setOnClickListener(instance);
        mTxtOrg.setOnClickListener(instance);
        mTxtUpdate.setOnClickListener(instance);
        ivChaCha.setOnClickListener(instance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_zxing://条形码扫描
                String scanResult = etOrderNum.getText().toString();
                if (!scanResult.equals("")) {//跳转到查询结果界面
                    Intent intent = new Intent(instance,QueryActivity.class);
                    intent.putExtra("code",scanResult);
                    startActivity(intent);
                } else {//为空时
                    Intent intent2 = new Intent(instance, CaptureActivity.class);
                    startActivityForResult(intent2, 0);
                }
                break;
            case R.id.txt_per_total://快递员查询
                startActivity(new Intent(instance,QueryPerActivity.class));
                break;
            case R.id.txt_com_total://企业查询
                startActivity(new Intent(instance,QueryComActivity.class));
                break;
            case R.id.txt_modify_total://修改密码
                startActivity(new Intent(instance, FindKeyActivity.class));
                break;
            case R.id.txt_org_total://数据统计
                startActivity(new Intent(instance, QueryOrgActivity.class));
                break;
            case R.id.txt_update_total://检查更新
                getVersion();
                FbUpdate = true;
                break;
            case R.id.iv_chacha_total://退出
                startActivity(new Intent(instance,LoginActivity.class));
                break;
        }
    }


    /**
     * 二维码扫面后返回的数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //扫描结果
        if (resultCode == RESULT_OK && requestCode == 0) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String scanResult = bundle.getString("result");
                //跳转到查询结果结果界面
                Intent intent = new Intent(instance,QueryActivity.class);
                intent.putExtra("code",scanResult);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (etOrderNum.getText()!=null){
            etOrderNum.setText("");
        }
    }


    //获取版本信息
    public void getVersion() {
        if (FsUrl.length() < 6) {
            showMess("地址错误，请在系统设置中设置上传地址。", true);
            return;
        }
        if (!CheckNetwork.isNetworkAvailable(MyApplication.instance)) {
            showMess("网络未连接", false);
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    LogUtils.i("fsurl==",FsUrl );

                    String sRet = ProcUnit.httpGetMore(FsUrl );
                    if (sRet.substring(0, 1).equals("0")) {
                        FsRet = sRet.substring(1);
                        FoHandler.sendMessage(M(0));
                    } else {
                        //FsRet=sRet.substring(1);
                        FsRet = "获取版本信息错误，请与管理员联系！";
                        FoHandler.sendMessage(M(10));
                    }
                } catch (Exception E) {
                    FsRet = E.getMessage();
                    FoHandler.sendMessage(M(10));
                }
            }

            protected Message M(int iWhat) {
                Message oMess = new Message();
                oMess.what = iWhat;
                return oMess;
            }
        }).start();

    }


    @Override
    protected void dialogOk() {

        switch (FiDialogType) {
            case 0:
                Intent oInt = new Intent(this, SendService.class);
                stopService(oInt);
                this.finish();
                break;
            case 1:
                downVersion();
                break;
        }

    }


    Handler FoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: //获取版本号
                    if (FsRet.length() == 0) {
                        FiDialogType = 10;
                        showDialog("错误", "获取版本错误", "确定", "", 0);
                        return;
                    }
                    String sVersion = getVersionName();
                    if (sVersion.length() == 0) {
                        FiDialogType = 10;
                        showDialog("错误", "获取版本号错误", "关闭", "", 0);
                        return;
                    }
                    String[] aData = FsRet.split(",");
                    if (aData.length > 0) {
                        aData[0] = aData[0].trim();
                        if (sVersion.compareTo(aData[0]) < 0) {
                            FiDialogType = 1;
                            FsVersion = aData[0];
                            if (aData.length > 1) {
                                aData[1] = aData[1].trim();
                                if (aData[1].compareTo("1") == 0) FbForceUpdate = true;
                            }
                            showDialog("更新提示", "检测到新版本，是否更新", "立刻更新", "以后再说", 0);
                        } else {
                            FiDialogType = 10;
                            if (FbUpdate)
                                showDialog("提示", "已经是最新版本", "确定", "", 0);
                        }
                    }
                    break;
                case 1://下载完成
                    if (FoWait != null) FoWait.dismiss();
                    File oFile = new File(FsFile);
                    if (!oFile.exists()) {
                        FiDialogType = 10;
                        showDialog("错误", "下载文件不存在", "关闭", "", 0);
                        return;
                    }

                    Intent oInt = new Intent(Intent.ACTION_VIEW);
                    oInt.setDataAndType(Uri.fromFile(oFile), "application/vnd.android.package-archive");
                    TotalActivity.this.startActivity(oInt);
                    break;
                case 3://显示进度
                    if (FoBar != null)
                        FoBar.setProgress(msg.arg1);
                    break;

                case 10:
                    if (FoWait != null) FoWait.dismiss();
                    FiDialogType = 10;
                    //showDialog("错误",FsRet,"确定","",0);
                    showMess(FsRet, true);
                    break;
            }
            super.handleMessage(msg);
        }
    };


    public String getVersionName() {
        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);

            // 当前应用的版本名称
            return info.versionName;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            return "";
        }
    }


    protected void downVersion() {
        Intent oInt = new Intent();
        oInt.setClass(this, UpdateActivity.class);
        oInt.putExtra("url", HttpUrlUtils.getHttpUrl().get_updata()+"/policenw.apk");
        oInt.putExtra("file", "policenw.apk");
        startActivity(oInt);
    }

}