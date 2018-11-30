package com.zmx.mobilecashier.dao;

import com.zmx.mobilecashier.MyApplication;
import com.zmx.mobilecashier.bean.Goods;
import com.zmx.mobilecashier.greendaos.GoodsDao;

import java.util.List;

/**
 * Created by Administrator on 2018-11-30.
 */

public class goodsDao {

    private static GoodsDao dao = MyApplication.getInstance().getDaoInstant().getGoodsDao();

    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param
     */
    public long insertCp(Goods g) {

        return dao.insertOrReplace(g);

    }

    /**
     * 根据商品分类查询
     */
    public static List<Goods> queryWhere(String group) {
        return dao.queryBuilder().where(GoodsDao.Properties.Group.eq(group)).list();
    }

    /**
     * 查询全部数据
     */
    public List<Goods> queryAll() {
        return dao.loadAll();
    }

    public void deleteData(){

        dao.deleteAll();

    }

}
