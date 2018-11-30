package com.zmx.mobilecashier.bean;

import java.io.Serializable;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-28 21:37
 * 类功能：商品类
 */
public class Goods implements Serializable{

    private int gid;
    private String img;
    private String name;
    private String describe;
    private int wap_synchro;
    private int pc_synchro;
    private String gds_price;
    private String vip_price;
    private String mall_price;
    private int group;
    private int mall_state;
    private int store_state;
    private int state;

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getGid() {
        return gid;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }

    public void setWap_synchro(int wap_synchro) {
        this.wap_synchro = wap_synchro;
    }

    public int getWap_synchro() {
        return wap_synchro;
    }

    public void setPc_synchro(int pc_synchro) {
        this.pc_synchro = pc_synchro;
    }

    public int getPc_synchro() {
        return pc_synchro;
    }

    public void setGds_price(String gds_price) {
        this.gds_price = gds_price;
    }

    public String getGds_price() {
        return gds_price;
    }

    public void setVip_price(String vip_price) {
        this.vip_price = vip_price;
    }

    public String getVip_price() {
        return vip_price;
    }

    public void setMall_price(String mall_price) {
        this.mall_price = mall_price;
    }

    public String getMall_price() {
        return mall_price;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getGroup() {
        return group;
    }

    public void setMall_state(int mall_state) {
        this.mall_state = mall_state;
    }

    public int getMall_state() {
        return mall_state;
    }

    public void setStore_state(int store_state) {
        this.store_state = store_state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getStore_state() {
        return store_state;
    }
}
