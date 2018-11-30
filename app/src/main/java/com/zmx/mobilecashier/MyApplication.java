package com.zmx.mobilecashier;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.zmx.mobilecashier.greendaos.DaoMaster;
import com.zmx.mobilecashier.greendaos.DaoSession;
import com.zmx.mobilecashier.util.MySharedPreferences;

/**
 * Created by Administrator on 2018-11-28.
 */

public class MyApplication  extends Application {

    private static MyApplication myApplication;
    private static DaoSession daoSession;

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
        setupDatabase();//配置数据库

    }

    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //创建数据库shop.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "cashier.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant() {

        return daoSession;

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