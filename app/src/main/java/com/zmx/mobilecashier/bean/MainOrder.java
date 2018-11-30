package com.zmx.mobilecashier.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-29 15:32
 * 类功能：订单数据
 */
public class MainOrder implements Serializable {

    private int mo_id;
    private String mo_pckey;
    private String mo_account;
    private String mo_zmey;
    private String mo_yhui;
    private String mo_shishou;
    private String mo_yingzhao;
    private String mo_discount;
    private String mo_payment;
    private Date mo_time;
    private String mo_ordernumber;
    private String mo_state;
    private String mo_classify;

    private List<ViceOrder> lists;

    public int getMo_id() {
        return mo_id;
    }
    public void setMo_id(int mo_id) {
        this.mo_id = mo_id;
    }
    public String getMo_pckey() {
        return mo_pckey;
    }
    public void setMo_pckey(String mo_pckey) {
        this.mo_pckey = mo_pckey;
    }
    public String getMo_account() {
        return mo_account;
    }
    public void setMo_account(String mo_account) {
        this.mo_account = mo_account;
    }
    public String getMo_zmey() {
        return mo_zmey;
    }
    public void setMo_zmey(String mo_zmey) {
        this.mo_zmey = mo_zmey;
    }
    public String getMo_yhui() {
        return mo_yhui;
    }
    public void setMo_yhui(String mo_yhui) {
        this.mo_yhui = mo_yhui;
    }
    public String getMo_shishou() {
        return mo_shishou;
    }
    public void setMo_shishou(String mo_shishou) {
        this.mo_shishou = mo_shishou;
    }
    public String getMo_yingzhao() {
        return mo_yingzhao;
    }
    public void setMo_yingzhao(String mo_yingzhao) {
        this.mo_yingzhao = mo_yingzhao;
    }
    public String getMo_payment() {
        return mo_payment;
    }
    public void setMo_payment(String mo_payment) {
        this.mo_payment = mo_payment;
    }


    public Date getMo_time() {
        return mo_time;
    }
    public void setMo_time(Date mo_time) {
        this.mo_time = mo_time;
    }
    public List<ViceOrder> getLists() {
        return lists;
    }
    public void setLists(List<ViceOrder> lists) {
        this.lists = lists;
    }
    public String getMo_ordernumber() {
        return mo_ordernumber;
    }
    public void setMo_ordernumber(String mo_ordernumber) {
        this.mo_ordernumber = mo_ordernumber;
    }
    public String getMo_state() {
        return mo_state;
    }
    public void setMo_state(String mo_state) {
        this.mo_state = mo_state;
    }
    public String getMo_discount() {
        return mo_discount;
    }
    public void setMo_discount(String mo_discount) {
        this.mo_discount = mo_discount;
    }
    public String getMo_classify() {
        return mo_classify;
    }
    public void setMo_classify(String mo_classify) {
        this.mo_classify = mo_classify;
    }


}
