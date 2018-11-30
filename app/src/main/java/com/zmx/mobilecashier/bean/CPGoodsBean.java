package com.zmx.mobilecashier.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-28 21:36
 * 类功能：
 */
public class CPGoodsBean  implements Serializable {

    public Group group;
    public List<Goods> list;//该类目下的商品

    public List<Goods> getList() {
        return list;
    }
    public void setList(List<Goods> list) {
        this.list = list;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
