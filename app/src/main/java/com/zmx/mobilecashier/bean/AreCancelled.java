package com.zmx.mobilecashier.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-30 19:17
 * 类功能：挂单
 */
@Entity
public class AreCancelled {

    @Id(autoincrement = true)
    private Long id;
    private String number;//订单编号
    private String date;//挂单时间
    private String members;//会员
    private double discount;//该订单的折扣
    private float total;//订单总额

    @Generated(hash = 501295813)
    public AreCancelled(Long id, String number, String date, String members,
            double discount, float total) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.members = members;
        this.discount = discount;
        this.total = total;
    }

    @Generated(hash = 934200587)
    public AreCancelled() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public float getTotal() {
        return this.total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public double getDiscount() {
        return this.discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
