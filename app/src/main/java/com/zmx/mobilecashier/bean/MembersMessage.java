package com.zmx.mobilecashier.bean;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-29 19:35
 * 类功能：会员信息
 */
public class MembersMessage {


    private long sign;
    private String pubtime;
    private int uid;
    private String birthday;
    private String mob;
    private String discounts;
    private String money;
    private String account;
    private int mid;
    private String wechat;
    private String password;
    private int integral;

    private List<CouponsMessage> lists;


    public long getSign() {
        return sign;
    }
    public void setSign(long sign) {
        this.sign = sign;
    }
    public String getPubtime() {
        return pubtime;
    }
    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getMob() {
        return mob;
    }
    public void setMob(String mob) {
        this.mob = mob;
    }
    public String getDiscounts() {
        return discounts;
    }
    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }
    public String getMoney() {
        return money;
    }
    public void setMoney(String money) {
        this.money = money;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public int getMid() {
        return mid;
    }
    public void setMid(int mid) {
        this.mid = mid;
    }
    public String getWechat() {
        return wechat;
    }
    public void setWechat(String wechat) {
        this.wechat = wechat;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getIntegral() {
        return integral;
    }
    public void setIntegral(int integral) {
        this.integral = integral;
    }
    public List<CouponsMessage> getLists() {
        return lists;
    }
    public void setLists(List<CouponsMessage> lists) {
        this.lists = lists;
    }



}
