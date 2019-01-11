package com.zmx.mobilecashier;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zmx.mobilecashier.adapter.GoodsShoppingAdapter;
import com.zmx.mobilecashier.bean.Goods;
import com.zmx.mobilecashier.bean.MembersMessage;
import com.zmx.mobilecashier.bean.ViceOrder;
import com.zmx.mobilecashier.ui.view.GlideCircleTransform;
import com.zmx.mobilecashier.util.TransformationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2019-01-11 10:57
 * 类功能：副屏
 */
public class Main extends Presentation implements OnBannerListener {

    private Context mContext;

    private Banner banner;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;

    //显示放进购物车的模块u
    private ListView listView;
    private List<ViceOrder> vos;
    private GoodsShoppingAdapter vos_adapter;

    private TextView t_weight,t_total,t_discount_price,paid_in,user_number,user_integral,user_balance,user_discount;

    public Main(Context mContext, Display display) {

        super(mContext, display);
        this.mContext = mContext;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main);
        initView();

        t_weight = findViewById(R.id.t_weight);
        t_total = findViewById(R.id.t_total);
        t_discount_price = findViewById(R.id.t_discount_price);
        paid_in = findViewById(R.id.paid_in);
        user_number = findViewById(R.id.user_number);
        user_integral = findViewById(R.id.user_integral);
        user_balance = findViewById(R.id.user_balance);
        user_discount = findViewById(R.id.user_discount);

        //购物车
        listView = findViewById(R.id.listView);
        vos = new ArrayList<>();
        vos_adapter = new GoodsShoppingAdapter(vos, mContext);
        listView.setAdapter(vos_adapter);



    }

    /**
     * 设置左侧预购买商品信息
     */
    public void setProductList(ViceOrder v) {

        if(v != null){

            vos.add(v);
            vos_adapter.notifyDataSetChanged();

        }

    }

    //删除某个商品
    public void deleteGoods(ViceOrder v){

        if(v != null){

            if(vos.size()>0) {

                vos.remove(v);
                vos_adapter.notifyDataSetChanged();

            }
        }

    }

    //显示会员信息
    public void ShowUser(MembersMessage mm){

        user_number.setText("账号："+mm.getAccount());
        user_discount.setText("折扣："+mm.getDiscounts());
        user_balance.setText("余额："+mm.getMoney());
        user_integral.setText("积分："+mm.getIntegral());

    }

    //撤单操作
    public void Cancellations(){

        if(vos.size()>0){
            vos.clear();
            vos_adapter.notifyDataSetChanged();
            t_weight.setText("0.00斤");
            t_total.setText("0.00");
            t_discount_price.setText("0.00");
            paid_in.setText("0.00");
            user_number.setText("账号：0");
            user_discount.setText("折扣：0");
            user_balance.setText("余额：0");
            user_integral.setText("积分：0");
        }

    }

    //设置选中
    public void Selected(int i){
        vos_adapter.setSelectPosition(i);
    }

    //更改总价
    public void UpdateMoney(String t_weights,String t_totals,String t_discount_prices,String paid_ins){

        t_weight.setText(t_weights+"斤");
        t_total.setText(t_totals);
        t_discount_price.setText(t_discount_prices);
        paid_in.setText(paid_ins);

    }

    private void initData() {

        list_path.add("https://gss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/zhidao/wh%3D600%2C800/sign=e9873bfca944ad342eea8f81e09220cc/a8ec8a13632762d08fa73daea8ec08fa513dc602.jpg");
        list_path.add("http://zmx.yiyuangy.com/web/uploads/20190111163442303802638.jpg");
        list_path.add("http://zmx.yiyuangy.com/web/uploads/20190111172036927f84899.jpg");
        list_path.add("http://zmx.yiyuangy.com/web/uploads/20190111163442303802638.jpg");

        list_title.add("免费开通会员，即可享受88折");
        list_title.add("免费开通会员，即可享受88折");
        list_title.add("免费开通会员，即可享受88折");
        list_title.add("免费开通会员，即可享受88折");
    }

    private void initView() {
        banner = findViewById(R.id.banner);
        list_path = new ArrayList<>();
        list_title = new ArrayList<>();
        initData();
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setImageLoader(new MyLoader());
        banner.setBannerAnimation(Transformer.Default);
        banner.setBannerTitles(list_title);
        banner.setDelayTime(3000);
        banner.isAutoPlay(true);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setImages(list_path)
                .setOnBannerListener(this)
                .start();
    }


    /**
     * 轮播监听
     *
     * @param position
     */
    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(this.getContext(), "你点了第" + (position + 1) + "张轮播图", Toast.LENGTH_SHORT).show();
    }

    /**
     * 网络加载图片
     * 使用了Glide图片加载框架
     */
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, final ImageView imageView) {
            loadIntoUseFitWidth(mContext,(String) path,R.mipmap.ic_shouji,imageView);
        }

    }

    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public static void loadIntoUseFitWidth(Context context, final String imageUrl, int errorImageId, final ImageView imageView) {

        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .placeholder(errorImageId)
                .error(errorImageId)
                .into(imageView);
    }




}
