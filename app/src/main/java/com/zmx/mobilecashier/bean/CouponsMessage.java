package com.zmx.mobilecashier.bean;

import java.util.Date;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-29 19:35
 * 类功能：会员优惠卷信息
 */

public class CouponsMessage {


    private String c_name;
    private String c_id;
    private String c_quota;
    private String c_term;
    private String c_state;
    private String c_type;
    private String c_days;
    private String c_starttime;
    private int state;//优惠卷的状态，0为可用，1为已用，2为过期
    private String validity;//有效期

    public String getC_name() {
        return c_name;
    }
    public void setC_name(String c_name) {
        this.c_name = c_name;
    }
    public String getC_id() {
        return c_id;
    }
    public void setC_id(String c_id) {
        this.c_id = c_id;
    }
    public String getC_quota() {
        return c_quota;
    }
    public void setC_quota(String c_quota) {
        this.c_quota = c_quota;
    }
    public String getC_term() {
        return c_term;
    }
    public void setC_term(String c_term) {
        this.c_term = c_term;
    }
    public String getC_state() {
        return c_state;
    }
    public void setC_state(String c_state) {
        this.c_state = c_state;
    }
    public String getC_type() {
        return c_type;
    }
    public void setC_type(String c_type) {
        this.c_type = c_type;
    }
    public String getC_days() {
        return c_days;
    }
    public void setC_days(String c_days) {
        this.c_days = c_days;
    }
    public String getC_starttime() {
        return c_starttime;
    }
    public void setC_starttime(String c_starttime) {
        this.c_starttime = c_starttime;
    }

    public int getState() {
        return state;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public void setState(int state) {
        this.state = state;
    }
}
