package com.xaqb.policenw.Utils;

/**
 * Created by lenovo on 2017/3/7.
 */


import android.content.Context;
import android.view.Display;
import android.view.WindowManager;


public class SystemUtil {


    public static Long getSystemWidth(Context context) {

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();

        return null;
    }

    public static Long getSystemHeight(Context context) {

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int width = display.getHeight();
        return null;
    }

}
