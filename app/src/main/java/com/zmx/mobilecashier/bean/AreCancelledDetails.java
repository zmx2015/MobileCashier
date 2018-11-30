package com.zmx.mobilecashier.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-30 19:22
 * 类功能：挂单的商品详情
 *
 */

@Entity
public class AreCancelledDetails {

    @Id(autoincrement = true)
    private Long id;
    private String g_id;//商品id
    private String number;//关联的订单编号
    private String gd_price;//单价
    private String gd_name;//名称
    private String gd_weight;//重量
    private float gd_mey;//总计

    @Generated(hash = 583885561)
    public AreCancelledDetails(Long id, String g_id, String number, String gd_price,
            String gd_name, String gd_weight, float gd_mey) {
        this.id = id;
        this.g_id = g_id;
        this.number = number;
        this.gd_price = gd_price;
        this.gd_name = gd_name;
        this.gd_weight = gd_weight;
        this.gd_mey = gd_mey;
    }

    @Generated(hash = 579513048)
    public AreCancelledDetails() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGd_price() {
        return gd_price;
    }

    public void setGd_price(String gd_price) {
        this.gd_price = gd_price;
    }

    public String getGd_name() {
        return gd_name;
    }

    public void setGd_name(String gd_name) {
        this.gd_name = gd_name;
    }

    public String getGd_weight() {
        return gd_weight;
    }

    public void setGd_weight(String gd_weight) {
        this.gd_weight = gd_weight;
    }

    public float getGd_mey() {
        return gd_mey;
    }

    public void setGd_mey(float gd_mey) {
        this.gd_mey = gd_mey;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getG_id() {
        return this.g_id;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }
}
