package com.zmx.mobilecashier.util;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.view.Display;
import android.view.WindowManager;

import com.zmx.mobilecashier.Main;
import com.zmx.mobilecashier.bean.Goods;
import com.zmx.mobilecashier.bean.MembersMessage;
import com.zmx.mobilecashier.bean.ViceOrder;

/**
 * 开发人员：曾敏祥
 * 开发时间：2019-01-11 11:17
 * 类功能：副屏幕
 */

public class CustomerEngine {
    // 获取设备上的屏幕
    private DisplayManager mDisplayManager;// 屏幕管理器
    private Display[] displays;// 屏幕数组
    private Main mCustomerDisplay;   //（继承Presentation）

    private static CustomerEngine instance;

    /**
     * 单例模式，创建的时候把界面绑定到第二个屏幕中
     * @param context 这里需要传入getApplicationContext(),就能实现全局双屏异显
     * @return
     */
    public static CustomerEngine getInstance(Context context){
        if(instance == null){
            instance = new CustomerEngine(context);
        }
        return instance;
    }

    public static void colose(){
        instance = null;
    }

    private CustomerEngine(Context context){
        mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        displays = mDisplayManager.getDisplays();
        if (null == mCustomerDisplay && displays.length > 1) {
            mCustomerDisplay =  new Main(context, displays[1]);// displays[1]是副屏
            mCustomerDisplay.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            mCustomerDisplay.show();
        }
    }

    /**
     * 显示客户购买的商品集合
     */
    public void setProductList(ViceOrder v){
        if(mCustomerDisplay != null){
            mCustomerDisplay.setProductList(v);
        }
    }

    //撤单操作
    public void Cancellations(){

        if(mCustomerDisplay != null){
            mCustomerDisplay.Cancellations();
        }

    }

    //删除某个
    public void deleteGoods(ViceOrder v){

        if(mCustomerDisplay != null){
            mCustomerDisplay.deleteGoods(v);
        }

    }
    //更改总价
    public void UpdateMoney(String t_weights,String t_totals,String t_discount_prices,String paid_ins){

        if(mCustomerDisplay != null){
            mCustomerDisplay.UpdateMoney(t_weights,t_totals,t_discount_prices,paid_ins);
        }

    }

    //设置选中
    public void Selected(int i) {

        if(mCustomerDisplay != null){
            mCustomerDisplay.Selected(i);
        }

    }

    //显示会员信息
    public void ShowUser(MembersMessage mm) {
        if(mCustomerDisplay != null){
            mCustomerDisplay.ShowUser(mm);
        }
    }

    }
