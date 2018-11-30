package com.zmx.mobilecashier;

import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.zmx.mobilecashier.adapter.CPFragmentAdapter;
import com.zmx.mobilecashier.adapter.GoodsShoppingAdapter;
import com.zmx.mobilecashier.adapter.MemberCouponsAdapter;
import com.zmx.mobilecashier.bean.CouponsMessage;
import com.zmx.mobilecashier.bean.Goods;
import com.zmx.mobilecashier.bean.Group;
import com.zmx.mobilecashier.bean.MembersMessage;
import com.zmx.mobilecashier.bean.ViceOrder;
import com.zmx.mobilecashier.fragment.CommodityPositionFragment;
import com.zmx.mobilecashier.http.OkHttp3ClientManager;
import com.zmx.mobilecashier.ui.BaseActivity;
import com.zmx.mobilecashier.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private List<String> mPageTitleList = new ArrayList<String>();
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private TabLayout tabLayout;
    private ViewPager mViewPager;

    //右侧控件
    private Button button_zero, button1, button2, button3, button4, button5, button6, button7, button8, button9, button0, button_point;

    //左侧控件
    private TextView text_weight, text_order_number, text_variable, text_integral, text_account;
    private String orderNumber = "";//订单编号
    private String variable = "";//输入的变量
    private Button button_delete_all,button_delete_selected, button_account, button_coupons,button_discount,button_intending,button_paid_in;
    private static double discount = 1;// 折扣默认为不打折
    private static  boolean memberCoupons = false;//优惠卷，默认不使用
    private static CouponsMessage couponsMessage;//优惠卷类

    //显示放进购物车的模块u
    private ListView listView;
    private List<ViceOrder> vos;
    private GoodsShoppingAdapter vos_adapter;

    //底部布局
    private Button bottom_button1, bottom_button2, bottom_button3, bottom_button4, bottom_button5, bottom_button6, bottom_button7, bottom_button8, bottom_button9, bottom_button10;

    private String weight = "0.00";//全局重量
    private int vosPosition = -1;//选中购物车的商品，默认没选中
    private float paid_in=0;//实收金额,默认为零，没有收


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {

        //头部
        text_total_price = findViewById(R.id.text_total_price);
        text_discount_price = findViewById(R.id.text_discount_price);
        text_discount = findViewById(R.id.text_discount);
        text_paid_in = findViewById(R.id.text_paid_in);
        text_yingzhao = findViewById(R.id.text_yingzhao);

        //商品
        tabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        selectGoods();

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

            //删除选中
            case R.id.button_delete_selected:

                //判断是否有选中了商品
                if(vosPosition == -1){
                    Toast("请选择要删除的商品！");
                }else{

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

                //清空订单编号
                orderNumber = "";

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

                break;
            //移动支付
            case R.id.bottom_button9:

                break;
            //现金
            case R.id.bottom_button10:

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
                if(vos.size()>0){

                    //判断用户输入
                    if(TextUtils.isEmpty(text_variable.getText().toString())){

                        Toast("请输入折扣，例如95折就输入0.95");

                    }else{

                        //判断用户输入的内容
                        if(Float.parseFloat(text_variable.getText().toString())<=1){

                            memberCoupons = false;//初始化优惠卷状态
                            discount = Float.parseFloat(text_variable.getText().toString());
                            text_variable.setText("");
                            variable = "";
                            countMoney();

                        }else{

                            Toast("这么大的折扣，都可以打骨折了，请重新输入！");

                        }

                    }


                }else{

                    Toast("没有商品，无需打折！");

                }

                break;

                //变价
            case R.id.button_intending:

                //判断是否有输入
                if (variable.equals("")){

                    Toast("请输入价格！");

                }else{

                    //判断有没有选中商品先
                    if (vosPosition == -1){

                        Toast("请选中要变价的商品！");

                    }else{

                        ViceOrder vo = vos.get(vosPosition);
                        vo.setVo_price(new Tools().priceResult(Float.parseFloat(variable)));
                        vo.setVo_subtotal(new Tools().priceResult(Float.parseFloat(vo.getVo_weight()) * Float.parseFloat(variable)));
                        vos.set(vosPosition,vo);
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
                if (variable.equals("")){

                    Toast("请输入实收金额！");

                }else{

                    //2种状态下判断实收金额有没有小于总金额，没有使用折扣和优惠卷下，使用优惠卷和折扣下
                    float total = 0;

                    if(discount == 1 && !memberCoupons){

                        total = Float.parseFloat(text_total_price.getText().toString());//获取总价

                    }else{
                        total = Float.parseFloat(text_discount_price.getText().toString());//折后价
                    }

                    paid_in = Float.parseFloat(variable);

                    if(paid_in >= total){

                        //初始化变量
                        variable = "";
                        text_variable.setText("");
                        countMoney();

                    }else{

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

                    try {

                        Log.e("返回参数", "" + msg.obj.toString());

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());

                        if (jsonObject.getString("code").equals("1")) {

                            JSONArray groupArray = jsonObject.getJSONArray("group");
                            JSONObject array = jsonObject.getJSONObject("list");
                            JSONArray goodsArray = array.getJSONArray("data");

                            List<Group> groups = new ArrayList<>();
                            Gson g = new Gson();
                            for (int i = 0; i < groupArray.length(); i++) {

                                List<Goods> goods = new ArrayList<>();
                                JSONObject json = groupArray.getJSONObject(i);
                                Group group = g.fromJson(json.toString(), Group.class);
                                groups.add(group);


                                for (int j = 0; j < goodsArray.length(); j++) {

                                    JSONObject goodsJson = goodsArray.getJSONObject(j);
                                    Goods good = g.fromJson(goodsJson.toString(), Goods.class);

                                    if (good.getGroup() == group.getId()) {

                                        goods.add(good);

                                    }
                                }

                                mPageTitleList.add(group.getGname());

                                CommodityPositionFragment fragment = new CommodityPositionFragment();
                                fragment.setGoodsShoppingListener(MainActivity.this);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("goods", (Serializable) goods);
                                fragment.setArguments(bundle);
                                Log.e("的大小", "" + goods.size());
                                mFragmentList.add(fragment);
                            }



  /*viewPager通过适配器与fragment关联*/
                            CPFragmentAdapter adapter = new CPFragmentAdapter(
                                    getSupportFragmentManager(), mFragmentList, mPageTitleList);
                            //TabLayout和ViewPager的关联
                            mViewPager.setAdapter(adapter);
                            tabLayout.setupWithViewPager(mViewPager);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();

                        Log.e("解析错误", "" + e.toString());

                    }

                    break;

                case 1:

                    try {

                        JSONObject bodys = new JSONObject(msg.obj.toString());


                        if (bodys.has("msg")) {

                            Toast("会员不存在!");

                        } else {

                            MembersMessage mm = new MembersMessage();
                            mm.setAccount(bodys.getString("account"));
                            mm.setDiscounts(bodys.getString("discounts"));
                            mm.setIntegral(bodys.getInt("integral"));
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

                            //设置折扣
                            discount = bodys.getDouble("discounts");

                            text_integral.setText("积分：" + bodys.getInt("integral"));
                            text_account.setText("" + bodys.getString("account"));
                            text_variable.setText("");
                            variable = "";
                            countMoney();//重新统计金额

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case 2:

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
        float totla_price = 0;//总价
        float discount_price = 0;//折后价
        float discount_yh = 0;//优惠金额
        float yinzhao = 0;

        for (int i = 0; i < vos.size(); i++) {

            totla_price = Float.parseFloat(vos.get(i).getVo_subtotal()) + totla_price;

        }
        //显示出来
        text_total_price.setText(Tools.priceResult(totla_price));

        //统计使用折扣后的折后价和优惠金额
        if (discount < 1) {

            text_discount_price.setText(Tools.priceResult(totla_price));
            discount_price = (float) (Math.round((float) (totla_price * discount) * 100)) / 100;// 折后价
            text_discount_price.setText(Tools.priceResult(discount_price) + "");
            discount_yh = totla_price - discount_price;

        }

        //使用优惠卷
        if(memberCoupons){

            if(couponsMessage != null){

                discount_price = totla_price - Float.parseFloat(couponsMessage.getC_quota());
                text_discount_price.setText(Tools.priceResult(discount_price) + "");
                discount_yh = Float.parseFloat(couponsMessage.getC_quota());
            }

        }

        //判断实收金额有没有输入
        if(paid_in != 0){

            //减总价
            if(discount == 1 && !memberCoupons){

                yinzhao = paid_in - totla_price;

            }else{

                //减折后价
                yinzhao = paid_in - discount_price;
            }

        }

        text_discount_price.setText(Tools.priceResult(discount_price) + "");//设置折后价
        text_discount.setText(Tools.priceResult(discount_yh) + "");//优惠的金额
        text_paid_in.setText(paid_in+"");//实收
        text_yingzhao.setText(Tools.priceResult(yinzhao) + "");//应找

    }

    //优惠卷界面
    private MemberCouponsAdapter mcAdapter;
    private RelativeLayout layout1,layout2,layout3;
    private TextView textView1,textView2,textView3;
    private View view1,view2,view3;

    public void dialogCoupons(final List<CouponsMessage> cms) {

        final List<CouponsMessage> show = new ArrayList<>();
        for (CouponsMessage c:filterCoupons(cms, 0)){
            show.add(c);
        }

        Log.e("过滤后的show", "：" + show.size());

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
                if (Float.parseFloat(text_total_price.getText().toString()) < Float.parseFloat(show.get(i).getC_term())){

                    Toast("无法使用优惠卷，未满足使用条件");

                }else{
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
                for (CouponsMessage c:filterCoupons(cms, 0)){
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
                for (CouponsMessage c:filterCoupons(cms, 1 )){
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
                for (CouponsMessage c:filterCoupons(cms, 2)){
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
     * @param state
     */
    public void initChoose(int state){

        switch (state){

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
     * @param show  优惠卷集合
     * @param state  要留的优惠卷
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

                        cm.setValidity("(有效期"+cm.getC_starttime()+"~"+sdf.format(today)+")");

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


    //获取商品信息
    public void selectGoods() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("pckey", Tools.getKey(MyApplication.getName()));
        params.put("account", "0");
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());

        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.goods/goodGroupsList", params, mHandler, 0, 404);

    }

    //查询会员
    public void selectMember(String account) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("pckey", Tools.getKey(account));
        params.put("account", "0");
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("account", account);

        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.lineapi/getUserInfo", params, mHandler, 1, 404);

    }

    //获取会员优惠卷
    public void selectMemberCoupons(String account) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("pckey", Tools.getKey(account));
        params.put("account", "0");
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("account", account);

        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.lineapi/getCoupons", params, mHandler, 2, 404);

    }

}
