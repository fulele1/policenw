package com.xaqb.policenw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;
import com.xaqb.policenw.Utils.AppManager;
import com.xaqb.policenw.Views.LoadingDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * @author fl on 2017/4/22.
 *         所有页面的基类
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener {
    /**
     * 加载数据对话框
     */
    public LoadingDialog loadingDialog;
    protected Context context;
    private TextView tv_title;
    private ImageView iv_backward;
    private TextView tv_forward;
    private FrameLayout mContentLayout;
    private LinearLayout llRoot;
    private FrameLayout layout_titlebar;
    private Toast toast;
    protected int FiDialogType = 0;//对话框处理类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashReport.initCrashReport(getApplicationContext()) ;

        //全屏显示
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AppManager.getAppManager().addActivity(this);//添加Activity到堆栈中
        try {
            setupViews();
            context = this;
            loadingDialog = new LoadingDialog(context);
            initTitleBar();
            initViews();
            initData();
            addListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化设置标题栏
     */
    public abstract void initTitleBar();

    /**
     * 初始化view控件
     */
    public abstract void initViews();

    /**
     * 初始化数据
     */
    public abstract void initData();

//

    /**
     * 给view添加事件监听
     */
    public abstract void addListener();

    /**
     * 加载 activity_title 布局 ，并获取标题及两侧按钮
     */
    private void setupViews() {
        super.setContentView(R.layout.ac_title);
        llRoot = (LinearLayout) findViewById(R.id.llRoot);
        layout_titlebar = (FrameLayout) findViewById(R.id.layout_titlebar);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backward = (ImageView) findViewById(R.id.iv_backward);
        tv_forward = (TextView) findViewById(R.id.tv_forward);
        mContentLayout = (FrameLayout) findViewById(R.id.layout_content);
    }

    /**
     * 设置标题栏是否可见
     *
     * @param visibility
     */
    public void setTitleBarVisible(int visibility) {
        layout_titlebar.setVisibility(visibility);
    }




    /**
     * 是否显示返回按钮
     *
     * @param show true则显示
     */

    protected void showBackwardView(boolean show) {
        if (iv_backward != null) {
            if (show) {
                iv_backward.setVisibility(View.VISIBLE);
            } else {
                iv_backward.setVisibility(View.INVISIBLE);
            }
        }
    }


    protected void showMess(String sMess, boolean bLong) {
        Toast.makeText(this, sMess, bLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    protected String readConfig(String sName) {

        SharedPreferences oConfig = getSharedPreferences("config", Activity.MODE_PRIVATE);
        return oConfig.getString(sName, "");
    }

    protected void readConfig(String[] aName, String[] aValue) {
        SharedPreferences oConfig = getSharedPreferences("config", Activity.MODE_PRIVATE);
        int i;
        for (i = 0; i < aName.length; i++)
            aValue[i] = oConfig.getString(aName[i], "");
    }




    /**
     * 返回按钮点击后触发
     *
     * @param backwardView
     */
    public void onBackward(View backwardView) {
//        Toast.makeText(this, "点击返回，可在此处调用finish()", Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * 提交按钮点击后触发
     *
     * @param forwardView
     */
    public void onForward(View forwardView) {
        Toast.makeText(this, "点击了标题右上角按钮", Toast.LENGTH_LONG).show();
    }

    //设置标题内容
    @Override
    public void setTitle(int titleId) {
        tv_title.setText(titleId);
    }

    //设置标题内容
    @Override
    public void setTitle(CharSequence title) {
        tv_title.setText(title);
    }

    //设置标题文字颜色
    @Override
    public void setTitleColor(int textColor) {
        tv_title.setTextColor(textColor);
    }

    //取出FrameLayout并调用父类removeAllViews()方法
    @Override
    public void setContentView(int layoutResID) {
        mContentLayout.removeAllViews();
        View.inflate(this, layoutResID, mContentLayout);
        onContentChanged();
    }

    @Override
    public void setContentView(View view) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view);
        onContentChanged();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#setContentView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view, params);
        onContentChanged();
    }

    /**
     * 弹出Toast便捷方法
     *
     * @param charSequence
     */
    public void showToast(CharSequence charSequence) {
        if (null == toast) {
            toast = Toast.makeText(context, charSequence, Toast.LENGTH_LONG);
        } else {
            toast.setText(charSequence);
        }
        toast.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != toast) {
            toast.cancel();
        }
//        MobclickAgent.onPause(this);
    }

    /* (non-Javadoc)
             * @see android.view.View.OnClickListener#onClick(android.view.View)
             * 按钮点击调用的方法
             */
    @Override
    public void onClick(View v) {
    }


    protected void setEmptyView(ListView listView) {
        TextView emptyView = new TextView(context);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText("暂无数据！");
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContentLayout.removeAllViews();
        mContentLayout = null;
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    /**
     *
     * @param context
     * @param title
     * @return
     */
    AlertDialog alertDialog;
    public AlertDialog showAdialog(final Context context, String title, String message, String ok, String no){
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_main_info);
        TextView tvTitle = (TextView) window.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(title);
        TextView tvMessage = (TextView) window.findViewById(R.id.tv_dialog_message);
        tvMessage.setText(message);
        Button btOk = (Button) window.findViewById(R.id.btn_dia_ok);
        btOk.setText(ok);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//结束当前Activity
               startActivity(new Intent(context,LoginActivity.class));

                //注销登录
//                Intent startMain = new Intent(Intent.ACTION_MAIN);
//                startMain.addCategory(Intent.CATEGORY_HOME);
//                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(startMain);
//                System.exit(0);// 退出程序
            }
        });
        Button btNo = (Button) window.findViewById(R.id.btn_dia_no);
        btNo.setText(no);
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }



    protected void writeConfig(String sName, String sValue) {
        SharedPreferences oConfig = getSharedPreferences("config", Activity.MODE_PRIVATE);
        SharedPreferences.Editor oEdit = oConfig.edit();//获得编辑器
        oEdit.putString(sName, sValue);
        oEdit.commit();//提交内容
    }


    //对话框的调用
    protected AlertDialog showDialog(String sCaption,
                                     String sText,
                                     String sOk,
                                     String sCancel,
                                     int iLayout) {
        AlertDialog.Builder oBuilder = new AlertDialog.Builder(this);
        if (iLayout > 0) {
            LayoutInflater oInflater = getLayoutInflater();
            View oLayout = oInflater.inflate(iLayout, null, false);
            oBuilder.setView(oLayout);
        } else
            oBuilder.setMessage(sText);
        oBuilder.setTitle(sCaption);
        if (sOk.length() > 0) {
            oBuilder.setPositiveButton(sOk, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BaseActivity.this.dialogOk();
                    dialog.dismiss();

                }
            });
        }
        if (sCancel.length() > 0) {
            oBuilder.setNegativeButton(sCancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BaseActivity.this.dialogCancel();
                    dialog.dismiss();
                }
            });
        }
        AlertDialog oDialog = oBuilder.create();
        oDialog.show();
        return oDialog;
    }


    //对话框单击确定按钮处理
    protected void dialogOk() {
    }

    //对话框单击取消按钮处理
    protected void dialogCancel() {

    }

    /**
     * ok进行网络请求
     * @param url
     */
    public void getOkConnection(String url){

        loadingDialog.show("请稍后...");
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        loadingDialog.dismiss();
                        showToast("网络访问异常");
                    }
                    @Override
                    public void onResponse(String s, int i) {
                        mDataFinishedLinstern.dataFinishedLinstern(s);
                    }
                });

    }
    OnDataFinishedLinstern mDataFinishedLinstern;
    public void setOnDataFinishedLinstern(OnDataFinishedLinstern dataFinishedLinstern){
        mDataFinishedLinstern = dataFinishedLinstern;
    }


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
}


interface OnDataFinishedLinstern{
    void dataFinishedLinstern(String s);
}