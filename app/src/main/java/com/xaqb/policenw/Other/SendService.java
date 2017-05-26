package com.xaqb.policenw.Other;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.xaqb.policenw.Utils.ProcUnit;

import java.io.File;

public class SendService extends Service {

    protected SendFileThread FoThread = null;
    protected ServerSet FoServer = new ServerSet();
    SQLiteDatabase oDB;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    protected String appPath() {

        Context oContext = this;//首先，在Activity里获取context
        File oFile = oContext.getFilesDir();
        return oFile.getAbsolutePath();

    }

    protected void readConfig(String[] aName, String[] aValue) {
        SharedPreferences oConfig = getSharedPreferences("config", Activity.MODE_PRIVATE);
        int i;
        for (i = 0; i < aName.length; i++)
            aValue[i] = oConfig.getString(aName[i], "");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (FoThread == null) FoThread = new SendFileThread();
        String[] aName = {"user", "url", "right", "urltime"};
        String[] aValue = {"", "", "", ""};
        readConfig(aName, aValue);
        FoThread.FsUser = aValue[0];
        FoThread.FsUrl = aValue[1];
        FoThread.FsRight = aValue[2];
        FoThread.FsPath = appPath();
        FoThread.FoHandler = FoHandler;
        FoServer.load(aValue[1]);
        FoServer.setTime(aValue[3]);
        if (FoServer.size() > 1) FoThread.FoServer = FoServer;
        FoThread.open();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        if (FoThread != null) FoThread.over();
        FoThread = null;
        super.onDestroy();
    }

    public void addLog(String[] sText) {

        oDB = this.openOrCreateDatabase("sendlog", 0, null);
        Cursor cursor = null;
        try {
//            MySQLiteHelper helper = new MySQLiteHelper(this, "sendlog", null, 2);
//            helper.getWritableDatabase();
            oDB.execSQL("create table if not exists log(id integer primary key autoincrement, filename text,sendtime text,ydh text)");
//            cursor = oDB.rawQuery("SELECT * FROM log WHERE column='ydh'", null);
//            if (cursor.getCount() == 0) {
//                oDB.execSQL("alter table log add ydh text");
//            }
            float vercode = Float.parseFloat(getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName);
//            Log.i("vercode",vercode+"");
            if (vercode <= 1.024) {
                oDB.execSQL("alter table log add ydh text");
            }
            //alter table log add ydh text;
            oDB.execSQL("insert into log (filename,sendtime,ydh) values('"
                    + sText[0]
                    + "','"
                    + ProcUnit.getTimeStr("yyyy-MM-dd HH:mm:ss")
                    + "','"
                    + sText[1]
                    + "')");
            oDB.execSQL("delete from log where id<(select max(id) from log)-1000");

        } catch (Exception E) {
            Log.w("error", E.getMessage());
        } finally {
            oDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    Handler FoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: //获取版本号
                    String[] sData = (String[]) msg.obj;
                    addLog(sData);
                    break;
            }
            super.handleMessage(msg);
        }
    };


//    public class MySQLiteHelper extends SQLiteOpenHelper {
//
//        public static final String CREATE_NEWS = "create table if not exists log(id integer primary key autoincrement, filename text,sendtime text,ydh text)";
//
//
//        public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
//                              int version) {
//            super(context, name, factory, version);
//        }
//
//        @Override
//        public void onCreate(SQLiteDatabase sqLiteDatabase) {
//            oDB.execSQL(CREATE_NEWS);
//        }
//
//        @Override
//        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//            switch (i1) {
//                case 1:
//                    oDB.execSQL(CREATE_NEWS);
//                    break;
//                case 2:
//                    oDB.execSQL("alter table log add ydh text");
//                    break;
//            }
//        }
//
//    }
}