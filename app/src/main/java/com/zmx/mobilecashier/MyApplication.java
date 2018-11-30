package com.zmx.mobilecashier;

import android.app.Application;

import com.zmx.mobilecashier.util.MySharedPreferences;

/**
 * Created by Administrator on 2018-11-28.
 */

public class MyApplication  extends Application {

    private static MyApplication myApplication;

    public static String store_id = "";//用户id
    public static String name = "";//手机号码

    public static String NetworkRequestMode = "post";

    public static MyApplication getInstance() {
        return myApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        myApplication = this;
        getInstance();

    }
    public static String getStore_id() {
        return MySharedPreferences.getInstance(myApplication).getString(MySharedPreferences.store_id,"");

    }
    public static String getNetworkRequestMode() {
        return "get";
    }
    public static String getName() {
        return MySharedPreferences.getInstance(myApplication).getString(MySharedPreferences.name,"");

    }
}