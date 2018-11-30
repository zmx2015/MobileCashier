package com.zmx.mobilecashier.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-28 21:48
 * 类功能：商品分类
 */

@Entity
public class Group {

    @Unique
    private int id;
    private String gname;
    private int state;
    private int mid;

    @Generated(hash = 126462603)
    public Group(int id, String gname, int state, int mid) {
        this.id = id;
        this.gname = gname;
        this.state = state;
        this.mid = mid;
    }

    @Generated(hash = 117982048)
    public Group() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGname() {
        return gname;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getMid() {
        return mid;
    }


}
