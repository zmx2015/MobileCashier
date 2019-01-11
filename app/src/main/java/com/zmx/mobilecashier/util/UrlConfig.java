package com.zmx.mobilecashier.util;

/**
 * Created by Administrator on 2019-01-07.
 */

public class UrlConfig {

    private static String URL = "http://api.yiyuangy.com/admin/";
    public static String IMG_URL ="http://api.yiyuangy.com/uploads/goods/";//图片路径

    public static String LOGIN = URL+"api.line/login";//登录

    public static String TYPE_LIST =  URL+"api.class/typeList";//获取门店分类列表和商城分类列表

    public static String ADD_ORDER =  URL+"api.order/orderAdd";//新增订单

    public static String GOODS_LIST= URL+"api.goods/goodGroupsList";//获取商品列表

    public static String SELECT_MEMBER =  URL+"api.lineapi/getUserInfo";//查找会员

    public static String COUPONS_LIST =  URL+"api.lineapi/getCoupons";//获取优惠卷列表
}
