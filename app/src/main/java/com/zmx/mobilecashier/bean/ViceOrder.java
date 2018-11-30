package com.zmx.mobilecashier.bean;

import java.io.Serializable;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-29 15:30
 * 类功能：放入购物车的商品
 */

public class ViceOrder implements Serializable {

    private int vo_id;
    private String vo_ordernumber;
    private int vo_g_id;
    private String vo_name;
    private String vo_weight;
    private String vo_price;
    private String vo_subtotal;
    private String vo_time;
    private Long id;//挂单后用到的id


    public int getVo_id() {
        return vo_id;
    }
    public void setVo_id(int vo_id) {
        this.vo_id = vo_id;
    }

    public String getVo_ordernumber() {
        return vo_ordernumber;
    }
    public void setVo_ordernumber(String vo_ordernumber) {
        this.vo_ordernumber = vo_ordernumber;
    }
    public int getVo_g_id() {
        return vo_g_id;
    }
    public void setVo_g_id(int vo_g_id) {
        this.vo_g_id = vo_g_id;
    }
    public String getVo_name() {
        return vo_name;
    }
    public void setVo_name(String vo_name) {
        this.vo_name = vo_name;
    }
    public String getVo_weight() {
        return vo_weight;
    }
    public void setVo_weight(String vo_weight) {
        this.vo_weight = vo_weight;
    }
    public String getVo_price() {
        return vo_price;
    }
    public void setVo_price(String vo_price) {
        this.vo_price = vo_price;
    }
    public String getVo_subtotal() {
        return vo_subtotal;
    }
    public void setVo_subtotal(String vo_subtotal) {
        this.vo_subtotal = vo_subtotal;
    }
    public String getVo_time() {
        return vo_time;
    }
    public void setVo_time(String vo_time) {
        this.vo_time = vo_time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
