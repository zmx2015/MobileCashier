package com.zmx.mobilecashier;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.google.gson.Gson;
import com.zmx.mobilecashier.adapter.AreCancelledAdapter;
import com.zmx.mobilecashier.adapter.CPFragmentAdapter;
import com.zmx.mobilecashier.adapter.GoodsShoppingAdapter;
import com.zmx.mobilecashier.adapter.MemberCouponsAdapter;
import com.zmx.mobilecashier.bean.AreCancelled;
import com.zmx.mobilecashier.bean.AreCancelledDetails;
import com.zmx.mobilecashier.bean.CouponsMessage;
import com.zmx.mobilecashier.bean.Goods;
import com.zmx.mobilecashier.bean.Group;
import com.zmx.mobilecashier.bean.MainOrder;
import com.zmx.mobilecashier.bean.MembersMessage;
import com.zmx.mobilecashier.bean.ViceOrder;
import com.zmx.mobilecashier.bean.ViceOrder_A;
import com.zmx.mobilecashier.dao.areCancelledDao;
import com.zmx.mobilecashier.dao.areCancelledDetailsDao;
import com.zmx.mobilecashier.dao.goodsDao;
import com.zmx.mobilecashier.dao.groupDao;
import com.zmx.mobilecashier.fragment.CommodityPositionFragment;
import com.zmx.mobilecashier.http.OkHttp3ClientManager;
import com.zmx.mobilecashier.ui.BaseActivity;
import com.zmx.mobilecashier.util.CustomerEngine;
import com.zmx.mobilecashier.util.Tools;
import com.zmx.mobilecashier.util.UrlConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends BaseActivity implements CommodityPositionFragment.GoodsShopping {

    //头部控件
    private TextView text_total_price, text_discount_price, text_discount, text_paid_in, text_yingzhao;

    //显示商品和分类模块
    private TabLayout tabLayout;
    private ViewPager mViewPager;

    //右侧控件
    private Button button_zero, button1, button2, button3, button4, button5, button6, button7, button8, button9, button0, button_point;

    //左侧控件
    private TextView text_weight, text_order_number, text_variable, text_integral, text_account;
    private Button button_delete_all, button_delete_selected, button_are_cancelled, button_a_single,button_cancellations,
            button_account, button_coupons, button_discount, button_intending, button_paid_in;

    //显示放进购物车的模块u
    private ListView listView;
    private List<ViceOrder> vos;
    private GoodsShoppingAdapter vos_adapter;

    //底部布局
    private Button bottom_button1, bottom_button2, bottom_button3, bottom_button4, bottom_button5, bottom_button6, bottom_button7, bottom_button8, bottom_button9, bottom_button10;

    private String weight = "0.00";//全局重量
    private int vosPosition = -1;//选中购物车的商品，默认没选中
    private float paid_in = 0;//实收金额,默认为零，没有收
    private static double discount = 1;// 折扣默认为不打折
    private static boolean memberCoupons = false;//优惠卷，默认不使用
    private static CouponsMessage couponsMessage;//优惠卷类
    private String orderNumber = "";//订单编号
    private String variable = "";//输入的变量

    private goodsDao gdao;
    private groupDao cpdao;
    private areCancelledDao acdao;
    private areCancelledDetailsDao acddao;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {

        CustomerEngine.getInstance(getApplicationContext()).setProductList(null);
        setTitleColor(R.id.position_view);

        gdao = new goodsDao();
        cpdao = new groupDao();
        acdao = new areCancelledDao();
        acddao = new areCancelledDetailsDao();

        //头部
        text_total_price = findViewById(R.id.text_total_price);
        text_discount_price = findViewById(R.id.text_discount_price);
        text_discount = findViewById(R.id.text_discount);
        text_paid_in = findViewById(R.id.text_paid_in);
        text_yingzhao = findViewById(R.id.text_yingzhao);

        //商品
        tabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        selectGoodsData();

        //购物车
        listView = findViewById(R.id.listView);
        vos = new ArrayList<>();
        vos_adapter = new GoodsShoppingAdapter(vos, this);
        listView.setAdapter(vos_adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                vosPosition = i;
                vos_adapter.setSelectPosition(i);
                CustomerEngine.getInstance(getApplicationContext()).Selected(i);

            }
        });

        //左侧
        text_variable = findViewById(R.id.text_variable);
        text_integral = findViewById(R.id.text_integral);
        text_account = findViewById(R.id.text_account);
        text_weight = findViewById(R.id.text_weight);
        text_order_number = findViewById(R.id.text_order_number);
        button_delete_all = findViewById(R.id.button_delete_all);
        button_delete_all.setOnClickListener(this);
        button_delete_selected = findViewById(R.id.button_delete_selected);
        button_delete_selected.setOnClickListener(this);
        button_coupons = findViewById(R.id.button_coupons);
        button_coupons.setOnClickListener(this);
        button_discount = findViewById(R.id.button_discount);
        button_discount.setOnClickListener(this);
        button_intending = findViewById(R.id.button_intending);
        button_intending.setOnClickListener(this);
        button_paid_in = findViewById(R.id.button_paid_in);
        button_paid_in.setOnClickListener(this);
        button_are_cancelled = findViewById(R.id.button_are_cancelled);
        button_are_cancelled.setOnClickListener(this);
        button_a_single = findViewById(R.id.button_a_single);
        button_a_single.setOnClickListener(this);
        button_cancellations = findViewById(R.id.button_cancellations);
        button_cancellations.setOnClickListener(this);

        //左侧
        button1 = findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button3 = findViewById(R.id.button3);
        button3.setOnClickListener(this);
        button4 = findViewById(R.id.button4);
        button4.setOnClickListener(this);
        button5 = findViewById(R.id.button5);
        button5.setOnClickListener(this);
        button6 = findViewById(R.id.button6);
        button6.setOnClickListener(this);
        button7 = findViewById(R.id.button7);
        button7.setOnClickListener(this);
        button8 = findViewById(R.id.button8);
        button8.setOnClickListener(this);
        button9 = findViewById(R.id.button9);
        button9.setOnClickListener(this);
        button0 = findViewById(R.id.button0);
        button0.setOnClickListener(this);
        button_point = findViewById(R.id.button_point);
        button_point.setOnClickListener(this);
        button_zero = findViewById(R.id.button_zero);
        button_zero.setOnClickListener(this);
        button_account = findViewById(R.id.button_account);
        button_account.setOnClickListener(this);

        //底部
        bottom_button1 = findViewById(R.id.bottom_button1);
        bottom_button1.setOnClickListener(this);
        bottom_button2 = findViewById(R.id.bottom_button2);
        bottom_button2.setOnClickListener(this);
        bottom_button3 = findViewById(R.id.bottom_button3);
        bottom_button3.setOnClickListener(this);
        bottom_button4 = findViewById(R.id.bottom_button4);
        bottom_button4.setOnClickListener(this);
        bottom_button5 = findViewById(R.id.bottom_button5);
        bottom_button5.setOnClickListener(this);
        bottom_button6 = findViewById(R.id.bottom_button6);
        bottom_button6.setOnClickListener(this);
        bottom_button7 = findViewById(R.id.bottom_button7);
        bottom_button7.setOnClickListener(this);
        bottom_button8 = findViewById(R.id.bottom_button8);
        bottom_button8.setOnClickListener(this);
        bottom_button9 = findViewById(R.id.bottom_button9);
        bottom_button9.setOnClickListener(this);
        bottom_button10 = findViewById(R.id.bottom_button10);
        bottom_button10.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            //挂单
            case R.id.button_are_cancelled:

                //判断有没有挂单数据
                if (vos.size() > 0) {

                    //查询挂单数量是否超出10个
                    List<AreCancelled> ac_lists = acdao.queryAll();

                    if(ac_lists.size() < 10){

                        //再判断这单有没有挂单过了
                        if (orderNumber.indexOf("A") == -1) {

                            // 进来没挂过单的操作
                            orderNumber = orderNumber + "A";
                            text_order_number.setText(orderNumber);

                            //拿到挂单数据
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            String g_time = sdf.format(new Date());// 挂单时间
                            String g_members = text_account.getText().toString();// 是否有会员号

                            for (int i = 0; i < vos.size(); i++) {

                                AreCancelledDetails acd = new AreCancelledDetails();
                                acd.setNumber(orderNumber);
                                acd.setG_id(vos.get(i).getVo_g_id() + "");
                                acd.setGd_name(vos.get(i).getVo_name());
                                acd.setGd_price(vos.get(i).getVo_price());
                                acd.setGd_mey(Float.parseFloat(vos.get(i).getVo_subtotal()));
                                acd.setGd_weight(vos.get(i).getVo_weight());
                                long l = acddao.insertCp(acd);//保存到数据库
                            }

                            AreCancelled ac = new AreCancelled();
                            ac.setMembers(g_members);
                            ac.setDate(g_time);
                            ac.setNumber(orderNumber);
                            ac.setDiscount(discount);
                            ac.setTotal(Float.parseFloat(text_total_price.getText().toString()));
                            long l = acdao.insertCp(ac);//保存到数据库

                            if (l > 0) {

                                Toast("挂单成功！");

                            } else {

                                Toast("挂单失败！");

                            }

                        } else {

                            Toast("已经是挂单状态，无需挂单啦！");

                        }


                    }else{

                        Toast("挂单失败,挂单数量超出上限了，请删除历史挂单！");

                    }

                } else {

                    Toast("没有商品，无法挂单！");

                }


                break;

            //取单
            case R.id.button_a_single:

                dialogGD();

                break;

                //撤单
            case R.id.button_cancellations:

                String str = orderNumber;
                boolean g_status = str.contains("A");

                // 判断是否是挂单号，是就删除该挂单
                if (g_status) {

                    //删除该订单编号的所有详情
                    acddao.deleteAcd(orderNumber);
                    //再删除主单
                    acdao.deleteAc(orderNumber);

                }

                //清空订单编号
                orderNumber = "";
                text_order_number.setText(orderNumber);

                discount = 1;//初始化折扣
                memberCoupons = false;//初始化优惠卷

                //清空商品
                vos.clear();
                vos_adapter.notifyDataSetChanged();
                //清空价格
                text_total_price.setText("0.00");
                text_discount_price.setText("0.00");
                text_discount.setText("0.00");
                text_paid_in.setText("0.00");
                text_yingzhao.setText("0.00");

                //清空会员信息
                text_integral.setText("积分：");
                text_account.setText("");
//副屏撤单
                CustomerEngine.getInstance(getApplicationContext()).Cancellations();


                break;

            //删除选中
            case R.id.button_delete_selected:

                //判断是否有选中了商品
                if (vosPosition == -1) {

                    Toast("请选择要删除的商品！");

                } else {

                    // 判断是否是挂单状态,是挂单就要删除挂单里面的商品
                    if (orderNumber.indexOf("A") != -1) {

                        ViceOrder vs = vos.get(vosPosition);
                        AreCancelledDetails acd = new AreCancelledDetails();
                        acd.setNumber(orderNumber);
                        acd.setId(vs.getAreC_id());
                        acd.setG_id(vs.getVo_g_id() + "");
                        acd.setGd_name(vs.getVo_name());
                        acd.setGd_price(vs.getVo_price());
                        acd.setGd_mey(Float.parseFloat(vs.getVo_subtotal()));
                        acd.setGd_weight(vs.getVo_weight());

                        //再判断这个商品是挂单前加入的还是挂单后加入的，通过保存本地的id来判断
                        if(vs.getAreC_id() != null ){

                            acddao.deleteAcd(acd);//删除商品记录

                        }


                    }

                    //删除副屏的
                    CustomerEngine.getInstance(getApplicationContext()).deleteGoods(vos.get(vosPosition));
                    //删除选中商品，刷新
                    vos.remove(vosPosition);
                    vos_adapter.setSelectPosition(-1);//设置没有选中了
                    vosPosition = -1;


                    //重新计算金额
                    countMoney();

                }

                break;

            //删除所有
            case R.id.button_delete_all:


                //判断下，没有商品提示下
                if (vos.size() > 0) {

                    //清空订单编号
                    orderNumber = "";
                    text_order_number.setText(orderNumber);

                    discount = 1;//初始化折扣
                    memberCoupons = false;//初始化优惠卷

                    //清空商品
                    vos.clear();
                    vos_adapter.notifyDataSetChanged();
                    //清空价格
                    text_total_price.setText("0.00");
                    text_discount_price.setText("0.00");
                    text_discount.setText("0.00");
                    text_paid_in.setText("0.00");
                    text_yingzhao.setText("0.00");

                    //清空会员信息
                    text_integral.setText("积分：");
                    text_account.setText("");

                    //副屏撤单
                    CustomerEngine.getInstance(getApplicationContext()).Cancellations();

                } else {

                    Toast("没有商品！");

                }

                break;


            //退出系统
            case R.id.bottom_button1:

                break;
            //最小化
            case R.id.bottom_button2:

                break;
            //配置应用
            case R.id.bottom_button3:

                break;
            //云同步
            case R.id.bottom_button4:
                selectGoods();
                break;
            //后台
            case R.id.bottom_button5:

                break;
            //钱箱
            case R.id.bottom_button6:

                // 随机重量
                Random ran = new Random();
                float f = ran.nextFloat() * 100 / 100;
                String c = String.valueOf(f);
                text_weight.setText(c.substring(0, 5) + "kg/" + Float.parseFloat((c.substring(0, 5))) * 1000 / 500
                        + "斤");
                weight = c.substring(0, 5) + "";

                break;
            //小票
            case R.id.bottom_button7:

                break;
            //会员支付
            case R.id.bottom_button8:

                //先判断有没有商品
                if(vos.size() > 0){

                    //判断有没有会员先
                    if(TextUtils.isEmpty(text_account.getText().toString())){

                        Toast("没有输入会员，无法使用会员支付！");

                    }else {
                        //提交订单
                        submitOrder("3","4");
                    }



                }else{

                    Toast("没有商品，无法提交订单");

                }

                break;
            //移动支付
            case R.id.bottom_button9:

                //先判断有没有商品
                if(vos.size() > 0){

                    //提交订单
                    submitOrder("4","2");


                }else{

                    Toast("没有商品，无法提交订单");

                }


                break;
            //现金
            case R.id.bottom_button10:

                //先判断有没有商品
                if(vos.size() > 0){

                    //提交订单
                    submitOrder("4","1");


                }else{

                    Toast("没有商品，无法提交订单");

                }

                break;

            //会员按钮
            case R.id.button_account:

                if (variable.equals("")) {

                    Toast("请输入会员账号");

                } else {

                    //先要初始化优惠卷,预防上一个会员使用优惠卷了
                    memberCoupons = false;
                    selectMember(variable);

                }

                break;

            //会员优惠卷
            case R.id.button_coupons:

                //判断是否输入了会员号
                if (text_account.getText().toString().equals("")) {

                    Toast("请输入会员账号");

                } else {

                    selectMemberCoupons(text_account.getText().toString());

                }


                break;

            //折扣按钮
            case R.id.button_discount:

                //先判断有没有商品先
                if (vos.size() > 0) {

                    //判断用户输入
                    if (TextUtils.isEmpty(text_variable.getText().toString())) {

                        Toast("请输入折扣，例如95折就输入0.95");

                    } else {

                        //判断用户输入的内容
                        if (Float.parseFloat(text_variable.getText().toString()) <= 1) {

                            memberCoupons = false;//初始化优惠卷状态
                            discount = Float.parseFloat(text_variable.getText().toString());
                            text_variable.setText("");
                            variable = "";
                            countMoney();

                        } else {

                            Toast("这么大的折扣，都可以打骨折了，请重新输入！");

                        }

                    }


                } else {

                    Toast("没有商品，无需打折！");

                }

                break;

            //变价
            case R.id.button_intending:

                //判断是否有输入
                if (variable.equals("")) {

                    Toast("请输入价格！");

                } else {

                    //判断有没有选中商品先
                    if (vosPosition == -1) {

                        Toast("请选中要变价的商品！");

                    } else {

                        ViceOrder vo = vos.get(vosPosition);
                        vo.setVo_price(new Tools().priceResult(Float.parseFloat(variable)));
                        vo.setVo_subtotal(new Tools().priceResult(Float.parseFloat(vo.getVo_weight()) * Float.parseFloat(variable)));
                        vos.set(vosPosition, vo);
                        vos_adapter.notifyDataSetChanged();

                        //初始化输入变量
                        variable = "";
                        text_variable.setText(variable);

                        //再统计价格
                        countMoney();

                    }

                }

                break;

            //实收
            case R.id.button_paid_in:

                //判断是否有输入
                if (variable.equals("")) {

                    Toast("请输入实收金额！");

                } else {

                    //2种状态下判断实收金额有没有小于总金额，没有使用折扣和优惠卷下，使用优惠卷和折扣下
                    float total = 0;

                    if (discount == 1 && !memberCoupons) {

                        total = Float.parseFloat(text_total_price.getText().toString());//获取总价

                    } else {
                        total = Float.parseFloat(text_discount_price.getText().toString());//折后价
                    }

                    paid_in = Float.parseFloat(variable);

                    if (paid_in >= total) {

                        //初始化变量
                        variable = "";
                        text_variable.setText("");
                        countMoney();

                    } else {

                        Toast("实收不能小于总价格！");

                    }


                }

                break;

            case R.id.button0:

                variable = variable + 0;
                text_variable.setText(variable);

                break;
            case R.id.button1:

                variable = variable + 1;
                text_variable.setText(variable);
                break;
            case R.id.button2:

                variable = variable + 2;
                text_variable.setText(variable);
                break;
            case R.id.button3:

                variable = variable + 3;
                text_variable.setText(variable);
                break;
            case R.id.button4:

                variable = variable + 4;
                text_variable.setText(variable);
                break;
            case R.id.button5:

                variable = variable + 5;
                text_variable.setText(variable);
                break;
            case R.id.button6:

                variable = variable + 6;
                text_variable.setText(variable);
                break;
            case R.id.button7:

                variable = variable + 7;
                text_variable.setText(variable);
                break;
            case R.id.button8:

                variable = variable + 8;
                text_variable.setText(variable);
                break;
            case R.id.button9:

                variable = variable + 9;
                text_variable.setText(variable);
                break;
            case R.id.button_point:


                if (!variable.equals("")) {

                    boolean status = variable.contains(".");

                    //包含了
                    if (!status) {

                        variable = variable + ".";
                        text_variable.setText(variable);

                    }
                }

                break;

            //归零
            case R.id.button_zero:

                variable = "";
                text_variable.setText(variable);
                break;


        }


    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 0:

                    dismissLoadingView();
                    try {

                        Log.e("返回参数", "" + msg.obj.toString());

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());

                        if (jsonObject.getString("code").equals("1")) {

                            JSONArray groupArray = jsonObject.getJSONArray("group");
                            JSONObject array = jsonObject.getJSONObject("list");
                            JSONArray goodsArray = array.getJSONArray("data");

                            Gson g = new Gson();
                            //循环类目
                            for (int i = 0; i < groupArray.length(); i++) {

                                JSONObject json = groupArray.getJSONObject(i);
                                Group group = g.fromJson(json.toString(), Group.class);

                                cpdao.insertCp(group); //保存到本地


                            }
                            //循环商品
                            for (int j = 0; j < goodsArray.length(); j++) {

                                JSONObject goodsJson = goodsArray.getJSONObject(j);
                                Goods good = g.fromJson(goodsJson.toString(), Goods.class);

                                gdao.insertCp(good);//保存到本地

                            }

                            selectGoodsData();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();

                        Log.e("解析错误", "" + e.toString());

                    }

                    break;

                case 1:

                    dismissLoadingView();
                    try {

                        JSONObject bodys = new JSONObject(msg.obj.toString());


                        if (bodys.has("msg")) {

                            Toast("会员不存在!");

                        } else {

                            MembersMessage mm = new MembersMessage();
                            mm.setAccount(bodys.getString("account"));
                            mm.setDiscounts(bodys.getString("discounts"));
                            mm.setIntegral(bodys.getInt("integral"));
                            mm.setMoney(bodys.getString("money"));
                            JSONArray array = bodys.getJSONArray("coupons");
                            List<CouponsMessage> cmss = new ArrayList<CouponsMessage>();
                            for (int z = 0; z < array.length(); z++) {

                                JSONObject j = array.getJSONObject(z);

                                CouponsMessage cm = new CouponsMessage();
                                cm.setC_days(j.getInt("days") + "");
                                cm.setC_id(j.getInt("id") + "");
                                cm.setC_name(j.getString("name"));
                                cm.setC_quota(j.getInt("quota") + "");
                                cm.setC_starttime(j.getString("starttime"));
                                cm.setC_state(j.getInt("state") + "");
                                cm.setC_term(j.getInt("term") + "");
                                cm.setC_type(j.getInt("type") + "");
                                cmss.add(cm);
                                mm.setLists(cmss);

                            }

                            // 判断是否是挂单状态,是挂单就修改保存的会员号
                            if (orderNumber.indexOf("A") != -1) {

                                acdao.UpdateAc(orderNumber,bodys.getString("account"),bodys.getDouble("discounts"));

                            }

                            //设置折扣
                            discount = bodys.getDouble("discounts");

                            text_integral.setText("积分：" + bodys.getInt("integral"));
                            text_account.setText("" + bodys.getString("account"));
                            text_variable.setText("");
                            variable = "";

                            CustomerEngine.getInstance(getApplicationContext()).ShowUser(mm);

                            countMoney();//重新统计金额

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case 2:

                    dismissLoadingView();
                    try {

                        Log.e("返回的数据", "数据" + msg.obj.toString());
                        List<CouponsMessage> cms = new ArrayList<>();
                        JSONArray array = new JSONArray(msg.obj.toString());

                        if (array.length() > 0) {

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject j = array.getJSONObject(i);

                                CouponsMessage cm = new CouponsMessage();
                                cm.setC_days(j.getInt("days") + "");
                                cm.setC_id(j.getInt("id") + "");
                                cm.setC_name(j.getString("name"));
                                cm.setC_quota(j.getInt("quota") + "");
                                cm.setC_starttime(j.getString("starttime"));
                                cm.setC_state(j.getInt("state") + "");
                                cm.setC_term(j.getInt("term") + "");
                                cm.setC_type(j.getInt("type") + "");
                                cms.add(cm);
                            }

                            dialogCoupons(cms);
                        } else {

                            Toast("该会员没有可用优惠卷！");

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case 3:

                    dismissLoadingView();
                    JSONObject bodys = null;

                    Log.e("提交订单返回数据","数据："+msg.obj.toString());

                    try {
                        bodys = new JSONObject(msg.obj.toString());

                        if (bodys != null) {

                            if (bodys.getString("status").equals("1")) {

                                Toast("提交成功！");
                                // 设置返回数据

                            } else {

                                Toast("提交失败 ！");

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case 404:

                    dismissLoadingView();

                    Toast("连接服务器失败，请重新连接！");

                    break;
            }



        }
    };


    @Override
    public void setGoodsShopping(Goods g) {

        if (!weight.equals("0.00") || !variable.equals("")) {


            //判断订单编号是否为空,如果为空就生成一个
            if (TextUtils.isEmpty(orderNumber)) {

                orderNumber = new Date().getTime() + "";
                text_order_number.setText(orderNumber);

            }


            //获取要放到购物车的商品
            ViceOrder v = new ViceOrder();
            v.setVo_g_id(g.getGid());
            v.setVo_name(g.getName());
            v.setVo_price(g.getGds_price());

            //判断是否是个数类还是重量
            if (!variable.equals("")) {

                v.setVo_weight(variable);
                v.setVo_subtotal(new Tools().priceResult(Float.parseFloat(variable) * Float.parseFloat(g.getGds_price())));

            } else {

                v.setVo_weight(weight);
                v.setVo_subtotal(new Tools().priceResult(Float.parseFloat(weight) * Float.parseFloat(g.getGds_price())));

            }

            v.setVo_time(new Tools().DateConversions(new Date()));
            // 判断是否是挂单状态,是挂单就保存好
            if (orderNumber.indexOf("A") != -1) {

                AreCancelledDetails acd = new AreCancelledDetails();
                acd.setNumber(orderNumber);
                acd.setG_id(v.getVo_g_id() + "");
                acd.setGd_name(v.getVo_name());
                acd.setGd_price(v.getVo_price());
                acd.setGd_mey(Float.parseFloat(v.getVo_subtotal()));
                acd.setGd_weight(v.getVo_weight());
                long l = acddao.insertCp(acd);//保存到数据库
                v.setAreC_id(l);
            }

            CustomerEngine.getInstance(getApplicationContext()).setProductList(v);

            vos.add(v);

            //重置变量
            variable = "";
            text_variable.setText("");

            //重新统计金额
            countMoney();
            //更新
            vos_adapter.notifyDataSetChanged();
            listView.setSelection(vos.size() - 1);//控制消息保持在底部


            } else {

            if (Float.parseFloat(weight) < 0) {

                Toast("重量出现异常");

            } else {

                Toast("没有重量或者数量，无法添加商品！");
            }

        }


    }


    //统计总金额
    public void countMoney() {

        //第一步，先获取总金额
        float total_price = 0;//总价
        float discount_price = 0;//折后价
        float discount_yh = 0;//优惠金额
        float yinzhao = 0;

        for (int i = 0; i < vos.size(); i++) {

            total_price = Float.parseFloat(vos.get(i).getVo_subtotal()) + total_price;

        }
        //显示出来
        text_total_price.setText(Tools.priceResult(total_price));

        //统计使用折扣后的折后价和优惠金额
        if (discount < 1) {

            text_discount_price.setText(Tools.priceResult(total_price));
            discount_price = (float) (Math.round((float) (total_price * discount) * 100)) / 100;// 折后价
            text_discount_price.setText(Tools.priceResult(discount_price) + "");
            discount_yh = total_price - discount_price;

        }

        //使用优惠卷
        if (memberCoupons) {

            if (couponsMessage != null) {

                discount_price = total_price - Float.parseFloat(couponsMessage.getC_quota());
                text_discount_price.setText(Tools.priceResult(discount_price) + "");
                discount_yh = Float.parseFloat(couponsMessage.getC_quota());
            }

        }

        //判断实收金额有没有输入
        if (paid_in != 0) {

            //减总价
            if (discount == 1 && !memberCoupons) {

                yinzhao = paid_in - total_price;

            } else {

                //减折后价
                yinzhao = paid_in - discount_price;
            }

        }

        text_discount_price.setText(Tools.priceResult(discount_price) + "");//设置折后价
        text_discount.setText(Tools.priceResult(discount_yh) + "");//优惠的金额
        text_paid_in.setText(paid_in + "");//实收
        text_yingzhao.setText(Tools.priceResult(yinzhao) + "");//应找

        CustomerEngine.getInstance(getApplicationContext()).UpdateMoney("",total_price+"",Tools.priceResult(discount_yh) + "",paid_in + "");

    }

    //优惠卷界面
    private MemberCouponsAdapter mcAdapter;
    private RelativeLayout layout1, layout2, layout3;
    private TextView textView1, textView2, textView3;
    private View view1, view2, view3;

    public void dialogCoupons(final List<CouponsMessage> cms) {

        final List<CouponsMessage> show = new ArrayList<>();
        for (CouponsMessage c : filterCoupons(cms, 0)) {
            show.add(c);
        }

        LayoutInflater inflater = LayoutInflater.from(this);//获取一个填充器
        View view = inflater.inflate(R.layout.dialog_coupons, null);//填充我们自定义的布局

        Display display = getWindowManager().getDefaultDisplay();//得到当前屏幕的显示器对象
        Point size = new Point();//创建一个Point点对象用来接收屏幕尺寸信息
        display.getSize(size);//Point点对象接收当前设备屏幕尺寸信息
        int width = size.x;//从Point点对象中获取屏幕的宽度(单位像素)
        int height = size.y;//从Point点对象中获取屏幕的高度(单位像素)
        Log.v("zxy", "width=" + width + ",height=" + height);//width=480,height=854可知手机的像素是480x854的
        //创建一个PopupWindow对象，第二个参数是设置宽度的，用刚刚获取到的屏幕宽度乘以2/3，取该屏幕的2/3宽度，从而在任何设备中都可以适配，高度则包裹内容即可，最后一个参数是设置得到焦点
        final PopupWindow popWindow = new PopupWindow(view, 2 * width / 3, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());//设置PopupWindow的背景为一个空的Drawable对象，如果不设置这个，那么PopupWindow弹出后就无法退出了
        popWindow.setOutsideTouchable(true);//设置是否点击PopupWindow外退出PopupWindow
        WindowManager.LayoutParams params = getWindow().getAttributes();//创建当前界面的一个参数对象
        params.alpha = 0.8f;//设置参数的透明度为0.8，透明度取值为0~1，1为完全不透明，0为完全透明，因为android中默认的屏幕颜色都是纯黑色的，所以如果设置为1，那么背景将都是黑色，设置为0，背景显示我们的当前界面
        getWindow().setAttributes(params);//把该参数对象设置进当前界面中
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {//设置PopupWindow退出监听器
            @Override
            public void onDismiss() {//如果PopupWindow消失了，即退出了，那么触发该事件，然后把当前界面的透明度设置为不透明
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1.0f;//设置为不透明，即恢复原来的界面
                getWindow().setAttributes(params);
            }
        });
        //第一个参数为父View对象，即PopupWindow所在的父控件对象，第二个参数为它的重心，后面两个分别为x轴和y轴的偏移量
        popWindow.showAtLocation(inflater.inflate(R.layout.activity_main, null), Gravity.CENTER, 0, 0);

        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView3);
        view1 = view.findViewById(R.id.view1);
        view2 = view.findViewById(R.id.view2);
        view3 = view.findViewById(R.id.view3);
        final ImageView ic_no_card = view.findViewById(R.id.ic_no_card);
        mcAdapter = new MemberCouponsAdapter(this, show);
        ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(mcAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //先判断是否满足使用条件了先
                if (Float.parseFloat(text_total_price.getText().toString()) < Float.parseFloat(show.get(i).getC_term())) {

                    Toast("无法使用优惠卷，未满足使用条件");

                } else {

                    //先将折扣设置为1
                    discount = 1;
                    memberCoupons = true;
                    couponsMessage = show.get(i);
                    countMoney();
                    popWindow.dismiss();

                }

            }
        });

        layout1 = view.findViewById(R.id.layout1);
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initChoose(1);
                show.clear();
                for (CouponsMessage c : filterCoupons(cms, 0)) {
                    show.add(c);
                }
                if (show.size() > 0) {

                    ic_no_card.setVisibility(View.GONE);

                } else {

                    ic_no_card.setVisibility(View.VISIBLE);

                }
                mcAdapter.notifyDataSetChanged();


            }
        });

        layout2 = view.findViewById(R.id.layout2);
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initChoose(2);
                show.clear();
                for (CouponsMessage c : filterCoupons(cms, 1)) {
                    show.add(c);
                }

                if (show.size() > 0) {
                    ic_no_card.setVisibility(View.GONE);
                } else {
                    ic_no_card.setVisibility(View.VISIBLE);
                }
                mcAdapter.notifyDataSetChanged();
            }
        });

        layout3 = view.findViewById(R.id.layout3);
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initChoose(3);
                show.clear();
                for (CouponsMessage c : filterCoupons(cms, 2)) {
                    show.add(c);
                }
                if (show.size() > 0) {
                    ic_no_card.setVisibility(View.GONE);
                } else {
                    ic_no_card.setVisibility(View.VISIBLE);
                }
                mcAdapter.notifyDataSetChanged();

            }
        });

    }

    /**
     * 切换优惠卷
     *
     * @param state
     */
    public void initChoose(int state) {

        switch (state) {

            case 1:

                textView1.setTextColor(getResources().getColor(R.color.tou));
                view1.setBackgroundColor(getResources().getColor(R.color.tou));
                textView2.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view2.setBackgroundColor(getResources().getColor(R.color.tv_color));
                textView3.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view3.setBackgroundColor(getResources().getColor(R.color.tv_color));

                break;
            case 2:

                textView1.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view1.setBackgroundColor(getResources().getColor(R.color.tv_color));
                textView2.setTextColor(getResources().getColor(R.color.tou));
                view2.setBackgroundColor(getResources().getColor(R.color.tou));
                textView3.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view3.setBackgroundColor(getResources().getColor(R.color.tv_color));

                break;
            case 3:
                textView1.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view1.setBackgroundColor(getResources().getColor(R.color.tv_color));
                textView2.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view2.setBackgroundColor(getResources().getColor(R.color.tv_color));
                textView3.setTextColor(getResources().getColor(R.color.tou));
                view3.setBackgroundColor(getResources().getColor(R.color.tou));
                break;

        }

    }

    /**
     * 过滤优惠卷
     *
     * @param show  优惠卷集合
     * @param state 要留的优惠卷
     * @return
     */
    public List<CouponsMessage> filterCoupons(List<CouponsMessage> show, int state) {

        //重新设置优惠卷的状态
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < show.size(); i++) {

            CouponsMessage cm = show.get(i);

            try {

                // 先判断是否已经过期了，在判断状态//true 表示过期了
                if (getFetureDate(sdf.parse(cm.getC_starttime()), Integer.parseInt(cm.getC_days()))) {

                    if (cm.getC_state().equals("2")) {

                        cm.setState(2);
                        cm.setValidity("(已过期)");

                    } else if (cm.getC_state().equals("1")) {

                        cm.setState(1);
                        cm.setValidity("(已使用)");

                    } else {

                        cm.setState(1);
                        cm.setValidity("(已使用)");

                    }

                } else {


                    if (cm.getC_state().equals("2")) {

                        cm.setState(0);

                        Date d = sdf.parse(cm.getC_starttime());

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(d);

                        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)
                                + Integer.parseInt(cm.getC_days()));

                        Date today = calendar.getTime();

                        cm.setValidity("(有效期" + cm.getC_starttime() + "~" + sdf.format(today) + ")");

                    } else {


                        cm.setState(1);
                        cm.setValidity("(已使用)");

                    }


                }

            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            } catch (ParseException e1) {
                e1.printStackTrace();
            }

            show.set(i, cm);
        }

        List<CouponsMessage> lcm = new ArrayList<>();

        for (int i = 0; i < show.size(); i++) {

            if (show.get(i).getState() == state) {

                lcm.add(show.get(i));

            }

        }

        return lcm;
    }

    /**
     * 获取未来 第 past 天的日期
     *
     * @param past
     * @return
     */
    public static boolean getFetureDate(Date d, int past) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(new Date());

        Date to = null;
        try {
            to = sdf.parse(s);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);

        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)
                + past);

        Date today = calendar.getTime();

        System.out.println("优惠卷结束时间" + sdf.format(today));

        boolean flag = true;

        if (today.getTime() >= to.getTime()) {
            flag = false;
        }


        return flag;
    }

    //查询本地全部商品数据
    public void selectGoodsData() {


        List<String> mPageTitleList = new ArrayList<String>();
        List<Fragment> mFragmentList = new ArrayList<>();
        //先查询全部分类出来
        List<Group> groups = cpdao.queryAll();

        if (groups.size() > 0) {

            for (int i = 0; i < groups.size(); i++) {

                List<Goods> goods = gdao.queryWhere(groups.get(i).getId() + "");
                mPageTitleList.add(groups.get(i).getGname());
                CommodityPositionFragment fragment = new CommodityPositionFragment();
                fragment.setGoodsShoppingListener(MainActivity.this);
                Bundle bundle = new Bundle();
                bundle.putSerializable("goods", (Serializable) goods);
                fragment.setArguments(bundle);
                mFragmentList.add(fragment);

            }

  /*viewPager通过适配器与fragment关联*/
            CPFragmentAdapter adapter = new CPFragmentAdapter(
                    getSupportFragmentManager(), mFragmentList, mPageTitleList);
            //TabLayout和ViewPager的关联
            mViewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(mViewPager);

        } else {

            Toast("没有商品数据！");

        }
    }

    private int ac_position = -1;//记录选择的挂单

    //取单列表
    public void dialogGD() {

        final AreCancelledAdapter ac_adapter;
        final List<AreCancelled> ac_lists = acdao.queryAll();
        ac_adapter = new AreCancelledAdapter(this, ac_lists);

        //详情
        ListView acd_listView, ac_listView;
        final List<ViceOrder> acd_vos = new ArrayList<>();

        //赋值第一个挂单的数据
        if (ac_lists.size() > 0) {

            //生成订单详情
            List<AreCancelledDetails> acd_lists = acddao.queryWhere(ac_lists.get(0).getNumber());

            for (AreCancelledDetails a : acd_lists) {

                ViceOrder v = new ViceOrder();
                v.setVo_g_id(Integer.parseInt(a.getG_id()));
                v.setVo_subtotal(a.getGd_mey() + "");
                v.setVo_price(a.getGd_price());
                v.setVo_weight(a.getGd_weight());
                v.setVo_name(a.getGd_name());
                acd_vos.add(v);

            }

            ac_adapter.setSelectPosition(0);//设置默认选中第一个
            ac_position = 0;
        }
        final GoodsShoppingAdapter acd_adapter = new GoodsShoppingAdapter(acd_vos, this);

        LayoutInflater inflater = LayoutInflater.from(this);//获取一个填充器
        View view = inflater.inflate(R.layout.dialog_are_cancelled, null);//填充我们自定义的布局

        Display display = getWindowManager().getDefaultDisplay();//得到当前屏幕的显示器对象
        Point size = new Point();//创建一个Point点对象用来接收屏幕尺寸信息
        display.getSize(size);//Point点对象接收当前设备屏幕尺寸信息
        int width = size.x;//从Point点对象中获取屏幕的宽度(单位像素)
        int height = size.y;//从Point点对象中获取屏幕的高度(单位像素)
        Log.v("zxy", "width=" + width + ",height=" + height);//width=480,height=854可知手机的像素是480x854的
        //创建一个PopupWindow对象，第二个参数是设置宽度的，用刚刚获取到的屏幕宽度乘以2/3，取该屏幕的2/3宽度，从而在任何设备中都可以适配，高度则包裹内容即可，最后一个参数是设置得到焦点
        final PopupWindow popWindow = new PopupWindow(view, 2 * width / 3, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());//设置PopupWindow的背景为一个空的Drawable对象，如果不设置这个，那么PopupWindow弹出后就无法退出了
        popWindow.setOutsideTouchable(true);//设置是否点击PopupWindow外退出PopupWindow
        WindowManager.LayoutParams params = getWindow().getAttributes();//创建当前界面的一个参数对象
        params.alpha = 0.8f;//设置参数的透明度为0.8，透明度取值为0~1，1为完全不透明，0为完全透明，因为android中默认的屏幕颜色都是纯黑色的，所以如果设置为1，那么背景将都是黑色，设置为0，背景显示我们的当前界面
        getWindow().setAttributes(params);//把该参数对象设置进当前界面中
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {//设置PopupWindow退出监听器
            @Override
            public void onDismiss() {//如果PopupWindow消失了，即退出了，那么触发该事件，然后把当前界面的透明度设置为不透明
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1.0f;//设置为不透明，即恢复原来的界面
                getWindow().setAttributes(params);
            }
        });
        //第一个参数为父View对象，即PopupWindow所在的父控件对象，第二个参数为它的重心，后面两个分别为x轴和y轴的偏移量
        popWindow.showAtLocation(inflater.inflate(R.layout.activity_main, null), Gravity.CENTER, 0, 0);

        ac_listView = view.findViewById(R.id.ac_listView);
        ac_listView.setAdapter(ac_adapter);
        ac_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                ac_position = position;
                ac_adapter.setSelectPosition(position);
                acd_vos.clear();
                //生成订单详情
                List<AreCancelledDetails> acd_lists = acddao.queryWhere(ac_lists.get(position).getNumber());

                for (AreCancelledDetails a : acd_lists) {

                    ViceOrder v = new ViceOrder();
                    v.setVo_g_id(Integer.parseInt(a.getG_id()));
                    v.setVo_subtotal(a.getGd_mey() + "");
                    v.setVo_price(a.getGd_price());
                    v.setVo_weight(a.getGd_weight());
                    v.setVo_name(a.getGd_name());
                    v.setAreC_id(a.getId());
                    acd_vos.add(v);

                }

                acd_adapter.notifyDataSetChanged();

            }
        });

        acd_listView = view.findViewById(R.id.acd_listView);
        acd_listView.setAdapter(acd_adapter);

        Button button_are_qu = view.findViewById(R.id.button_are_qu);
        button_are_qu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //判断有没有选中了订单
                if(ac_position != -1){

                vos.clear();//先清空购物车

                //拿出购物车的东西
                List<AreCancelledDetails> acd_lists = acddao.queryWhere(ac_lists.get(ac_position).getNumber());

                //显示单号出来先
                orderNumber = ac_lists.get(ac_position).getNumber();
                text_order_number.setText(orderNumber);
                discount = ac_lists.get(ac_position).getDiscount();//该订单的折扣

                //判断有没有会员，有会员就显示出来
                String s_member = ac_lists.get(ac_position).getMembers();
                if(!TextUtils.isEmpty(s_member)){

                    text_account.setText(s_member);

                }

                for (AreCancelledDetails ac:acd_lists){

                    ViceOrder v = new ViceOrder();
                    v.setVo_name(ac.getGd_name());
                    v.setVo_weight(ac.getGd_weight());
                    v.setVo_price(ac.getGd_price());
                    v.setVo_subtotal(ac.getGd_mey()+"");
                    v.setVo_g_id(Integer.parseInt(ac.getG_id()));
                    v.setAreC_id(ac.getId());
                    vos.add(v);
                }

                countMoney();
                vos_adapter.notifyDataSetChanged();
                popWindow.dismiss();

                }else{

                    Toast("请选择挂单！");

                }

            }
        });

        Button button_are_delete = view.findViewById(R.id.button_are_delete);
        button_are_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //判断有没有选中了订单
                if(ac_position != -1){

                    //获取要选中要删除的挂单的订单编号
                    //删除该订单编号的所有详情
                    acddao.deleteAcd(ac_lists.get(ac_position).getNumber());
                    //再删除主单
                    acdao.deleteAc(ac_lists.get(ac_position));

                    //刷新数据
                    ac_lists.remove(ac_position);
                    acd_vos.clear();

                    //删除选中商品，刷新
                    ac_adapter.setSelectPosition(-1);//设置没有选中了
                    ac_position = -1;
                    acd_adapter.notifyDataSetChanged();

                }else{

                    Toast("请选择挂单！");

                }




            }
        });

    }

    //获取商品信息
    public void selectGoods() {

        showLoadingView("更新中...");
        Map<String, String> params = new HashMap<String, String>();
        params.put("pckey", Tools.getKey(MyApplication.getName()));
        params.put("account", "0");
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());

        OkHttp3ClientManager.getInstance().NetworkRequestMode(UrlConfig.GOODS_LIST, params, mHandler, 0, 404);

    }

    //查询会员
    public void selectMember(String account) {


        showLoadingView("查询中...");
        Map<String, String> params = new HashMap<String, String>();
        params.put("pckey", Tools.getKey(account));
        params.put("account", "0");
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("account", account);

        OkHttp3ClientManager.getInstance().NetworkRequestMode(UrlConfig.SELECT_MEMBER, params, mHandler, 1, 404);

    }

    //获取会员优惠卷
    public void selectMemberCoupons(String account) {

        showLoadingView("获取中...");
        Map<String, String> params = new HashMap<String, String>();
        params.put("pckey", Tools.getKey(account));
        params.put("account", "0");
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("account", account);

        OkHttp3ClientManager.getInstance().NetworkRequestMode(UrlConfig.COUPONS_LIST, params, mHandler, 2, 404);

    }


    //提交订单
    public void submitOrder(String classify,String payment){

        showLoadingView("加载中...");
        List<ViceOrder_A> lists = new ArrayList<>();
        for (ViceOrder v : vos){

            ViceOrder_A va = new ViceOrder_A();

            va.setGoods_id(v.getVo_g_id()+"");
            va.setName(v.getVo_name());
            va.setPrice(v.getVo_price());
            va.setSubtotal(v.getVo_subtotal());
            va.setWeight(v.getVo_weight());

            lists.add(va);
        }

        Gson g = new Gson();
        String jsonString = g.toJson(lists);

        Map<String, String> mapData = new HashMap<String, String>();
        mapData.put("admin", MyApplication.getName());
        mapData.put("mid", MyApplication.getStore_id());
        mapData.put("pckey", new Tools().getKey(this));

        //判断是否是会员单
        if(TextUtils.isEmpty(text_account.getText().toString())){

            mapData.put("account", "0");

        }else{
            mapData.put("account", text_account.getText().toString());
        }

        mapData.put("orderAll", jsonString);
        mapData.put("zmey", text_total_price.getText().toString());// 总价
        mapData.put("yhui", text_discount_price.getText().toString());
        mapData.put("shishou", text_paid_in.getText().toString());// 实收
        mapData.put("yingzhao", text_yingzhao.getText().toString());// 应找
        mapData.put("payment", payment);
        mapData.put("ordernumber", orderNumber);
        mapData.put("discount", text_discount.getText().toString());
        String phpTime = new Date().getTime() + "";
        mapData.put("synchro", phpTime.substring(0, phpTime.length() - 3));
        mapData.put("buytime", phpTime.substring(0, phpTime.length() - 3));
        mapData.put("classify", classify);

        OkHttp3ClientManager.getInstance().NetworkRequestMode(UrlConfig.ADD_ORDER, mapData, mHandler, 3, 404);


    }


    @Override
    public void onBackPressed() {
        onDestroy();
        //完全退出应用，取消双屏异显
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        System.exit(0);
    }

}
