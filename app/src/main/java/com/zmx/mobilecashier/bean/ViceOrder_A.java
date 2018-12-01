package com.zmx.mobilecashier.bean;

/**
 * Created by Administrator on 2018-12-01.
 */

public class ViceOrder_A {

    private String price;
    private String subtotal;
    private String name;
    private String goods_id;
    private String weight;

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrice() {
        return price;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public String getName() {
        return name;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public String getWeight() {
        return weight;
    }

}
