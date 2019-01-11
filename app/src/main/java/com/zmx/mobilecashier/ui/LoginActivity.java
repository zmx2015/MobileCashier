package com.zmx.mobilecashier.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.zmx.mobilecashier.MainActivity;
import com.zmx.mobilecashier.R;
import com.zmx.mobilecashier.bean.StoresMessage;
import com.zmx.mobilecashier.http.OkHttp3ClientManager;
import com.zmx.mobilecashier.util.Base64Utils;
import com.zmx.mobilecashier.util.MySharedPreferences;
import com.zmx.mobilecashier.util.UrlConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{


    //布局内的控件
    private EditText et_name;
    private EditText et_password;
    private Button mLoginBtn;
    private CheckBox checkBox_password;
    private CheckBox checkBox_login;
    private ImageView iv_see_password;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    private void initData() {

        //判断用户第一次登陆
        if (firstLogin()) {
            checkBox_password.setChecked(false);//取消记住密码的复选框
            checkBox_login.setChecked(false);//取消自动登录的复选框
        }
        //判断是否记住密码
        if (remenberPassword()) {
            checkBox_password.setChecked(true);//勾选记住密码
            setTextNameAndPassword();//把密码和账号输入到输入框中
        } else {
            setTextName();//把用户账号放到输入账号的输入框中
        }

        //判断是否自动登录
        if (autoLogin()) {
            checkBox_login.setChecked(true);
            login();//去登录就可以
        }
    }

    /**
     * 把本地保存的数据设置数据到输入框中
     */
    public void setTextNameAndPassword() {
        et_name.setText("" + getLocalName());
        et_password.setText("" + getLocalPassword());
    }

    /**
     * 设置数据到输入框中
     */
    public void setTextName() {
        et_name.setText("" + getLocalName());
    }


    /**
     * 获得保存在本地的用户名
     */
    public String getLocalName() {

        String name =MySharedPreferences.getInstance(this).getString(MySharedPreferences.name,"");
        return name;
    }


    /**
     * 获得保存在本地的密码
     */
    public String getLocalPassword() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        String password =  MySharedPreferences.getInstance(this).getString(MySharedPreferences.password,"");
        return Base64Utils.decryptBASE64(password);   //解码一下
//       return password;   //解码一下

    }

    /**
     * 判断是否自动登录
     */
    private boolean autoLogin() {

        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        boolean autoLogin = MySharedPreferences.getInstance(this).getBoolean(MySharedPreferences.autoLogin,false);
        return autoLogin;
    }

    /**
     * 判断是否记住密码
     */
    private boolean remenberPassword() {

        boolean remenberPassword = MySharedPreferences.getInstance(this).getBoolean(MySharedPreferences.remenberPassword,false);
        return remenberPassword;
    }

    private void setupEvents() {
        mLoginBtn.setOnClickListener(this);
        checkBox_password.setOnCheckedChangeListener(this);
        checkBox_login.setOnCheckedChangeListener(this);
        iv_see_password.setOnClickListener(this);

    }

    /**
     * 判断是否是第一次登陆
     */
    private boolean firstLogin() {

        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        boolean first =MySharedPreferences.getInstance(this).getBoolean(MySharedPreferences.first,false);

        if (first) {
            //创建一个ContentVa对象（自定义的）设置不是第一次登录，,并创建记住密码和自动登录是默认不选，创建账号和密码为空

            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.first,false);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.remenberPassword,false);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.autoLogin,false);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.name,"");
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.password,"");

            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                loadUserName();    //无论如何保存一下用户名
                login(); //登陆
                break;
            case R.id.iv_see_password:
                setPasswordVisibility();    //改变图片并设置输入框的文本可见或不可见
                break;

        }
    }


    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        et_name = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        checkBox_password = (CheckBox) findViewById(R.id.checkBox_password);
        checkBox_login = (CheckBox) findViewById(R.id.checkBox_login);
        iv_see_password = findViewById(R.id.iv_see_password);
        setupEvents();
        initData();

    }

    /**
     * 模拟登录情况
     * 用户名csdn，密码123456，就能登录成功，否则登录失败
     */
    private void login() {

        //先做一些基本的判断，比如输入的用户命为空，密码为空，网络不可用多大情况，都不需要去链接服务器了，而是直接返回提示错误
        if (getAccount().isEmpty()){
            Toast("你输入的账号为空！");
            return;
        }

        if (getPassword().isEmpty()){
            Toast("你输入的密码为空！");
            return;
        }
        //登录一般都是请求服务器来判断密码是否正确，要请求网络，要子线程
        showLoadingView("正在登陆...");//显示加载框

        //请求网络
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", getAccount());
        params.put("password", md5(getPassword()));

        OkHttp3ClientManager.getInstance().NetworkRequestMode(UrlConfig.LOGIN, params, handler, 1, 404);


        //请求网络
//        Map<String, String> paramss = new HashMap<String, String>();
//        paramss.put("account","123456");
////        paramss.put("mobileNo", "76ecc68a-36e3-3f5d-b2a5-a8e6a56c2f82");
//        paramss.put("password","wrbzgz10");
////        paramss.put("pageSize","20");
////        paramss.put("type","10");
////        paramss.put("page","1");
//
//        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://app.yogofruit.cn:8080/tob-gateway-web/store/user/login", paramss, handler, 4, 404);


    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    try {

                        JSONObject json = new JSONObject(msg.obj.toString());

                        List<StoresMessage> lists = new ArrayList<>();

                        //获取登录账号的信息
                        if(json.getString("code") == "1" || json.getString("code").equals("1")) {

                            int st = json.getInt("codes");

                            if (st == 1) {

                                JSONArray array = json.getJSONArray("store");

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject j = array.getJSONObject(i);

                                    Gson gson = new Gson();
                                    StoresMessage sm = gson.fromJson(j.toString(), StoresMessage.class);
                                    lists.add(sm);

                                }

                                Toast("登录成功");
                                loadCheckBoxState();//记录下当前用户记住密码和自动登录的状态;

                                //判断是否已经保存了门店信息了，有就直接跳到主页，没有就跳到门店列表
                                //获取SharedPreferences对象，使用自定义类的方法来获取对象
                                String mid = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, "");
                                Log.e("门店id", "mid" + mid);
                                if (mid.equals("-1") || mid == "") {

                                    MySharedPreferences.getInstance(mActivity).setDataList("store", lists);

                                    // 通过Intent传递对象给Service
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("stores", (Serializable) lists);
                                    intent.setClass(mActivity, StoreListActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();//关闭页面

                                } else {

                                    startActivity(MainActivity.class);
                                    finish();//关闭页面

                                }

                            } else {

                                Toast("没有门店，请在后台添加门店");
                                setLoginBtnClickable(true);  //这里解放登录按钮，设置为可以点击
                                dismissLoadingView();//隐藏加载框

                            }
                        }else{
                            Toast("输入的登录账号或密码不正确");
                            dismissLoadingView();//隐藏加载框
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        Toast("登录失败！请联系客服");
                        setLoginBtnClickable(true);  //这里解放登录 按钮，设置为可以点击
                        dismissLoadingView();//隐藏加载框

                    }

                    break;

                case 4:

                    Log.e("请求返回的内容",""+msg.obj.toString());
                    break;

                case 404:

                    Toast("网络连接失败！请检查网络");
                    setLoginBtnClickable(true);  //这里解放登录按钮，设置为可以点击
                    dismissLoadingView();//隐藏加载框

                    break;

            }

        }
    };


    /**
     * 保存用户账号
     */
    public void loadUserName() {
        if (!getAccount().equals("") || !getAccount().equals("请输入登录账号")) {

            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.name,getAccount());
        }

    }

    /**
     * 设置密码可见和不可见的相互转换
     */
    private void setPasswordVisibility() {
        if (iv_see_password.isSelected()) {
            iv_see_password.setSelected(false);
            //密码不可见
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            iv_see_password.setSelected(true);
            //密码可见
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }

    }

    /**
     * 获取账号
     */
    public String getAccount() {
        return et_name.getText().toString().trim();//去掉空格
    }

    /**
     * 获取密码
     */
    public String getPassword() {
        return et_password.getText().toString().trim();//去掉空格
    }


    /**
     * 保存用户选择“记住密码”和“自动登陆”的状态
     */
    private void loadCheckBoxState() {
        loadCheckBoxState(checkBox_password, checkBox_login);
    }

    /**
     * 保存按钮的状态值
     */
    public void loadCheckBoxState(CheckBox checkBox_password, CheckBox checkBox_login) {

        //如果设置自动登录
        if (checkBox_login.isChecked()) {
            //创建记住密码和自动登录是都选择,保存密码数据
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.remenberPassword,true);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.autoLogin,true);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.password,Base64Utils.encryptBASE64(getPassword()));

        } else if (!checkBox_password.isChecked()) { //如果没有保存密码，那么自动登录也是不选的
            //创建记住密码和自动登录是默认不选,密码为空
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.remenberPassword,false);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.autoLogin,false);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.password,"");

        } else if (checkBox_password.isChecked()) {   //如果保存密码，没有自动登录
            //创建记住密码为选中和自动登录是默认不选,保存密码数据
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.remenberPassword,true);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.autoLogin,false);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.password, Base64Utils.encryptBASE64(getPassword()));
        }
    }

    /**
     * 是否可以点击登录按钮
     *
     * @param clickable
     */
    public void setLoginBtnClickable(boolean clickable) {
        mLoginBtn.setClickable(clickable);
    }


    /**
     * CheckBox点击时的回调方法 ,不管是勾选还是取消勾选都会得到回调
     *
     * @param buttonView 按钮对象
     * @param isChecked  按钮的状态
     */
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == checkBox_password) {  //记住密码选框发生改变时
            if (!isChecked) {   //如果取消“记住密码”，那么同样取消自动登陆
                checkBox_login.setChecked(false);
            }
        } else if (buttonView == checkBox_login) {   //自动登陆选框发生改变时
            if (isChecked) {   //如果选择“自动登录”，那么同样选中“记住密码”
                checkBox_password.setChecked(true);
            }
        }
    }




    /**
     * 页面销毁前回调的方法
     */
    protected void onDestroy() {

        super.onDestroy();
    }

    /**
     * Md5加密函数
     *
     * @param txt
     * @return
     */
    public String md5(String txt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(txt.getBytes("GBK")); // Java的字符串是unicode编码，不受源码文件的编码影响；而PHP的编码是和源码文件的编码一致，受源码编码影响。
            StringBuffer buf = new StringBuffer();
            for (byte b : md.digest()) {
                buf.append(String.format("%02x", b & 0xff));
            }
            return buf.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }



}
