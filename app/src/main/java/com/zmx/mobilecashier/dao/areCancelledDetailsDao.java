package com.zmx.mobilecashier.dao;

import com.zmx.mobilecashier.MyApplication;
import com.zmx.mobilecashier.bean.AreCancelled;
import com.zmx.mobilecashier.bean.AreCancelledDetails;
import com.zmx.mobilecashier.greendaos.AreCancelledDetailsDao;

import java.util.List;

/**
 * Created by Administrator on 2018-11-30.
 */

public class areCancelledDetailsDao {


    private static AreCancelledDetailsDao dao = MyApplication.getInstance().getDaoInstant().getAreCancelledDetailsDao();

    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param
     */
    public long insertCp(AreCancelledDetails acd) {
        return dao.insertOrReplace(acd);
    }

    /**
     * 根据订单编号查询商品
     */
    public static List<AreCancelledDetails> queryWhere(String name) {
        return dao.queryBuilder().where(AreCancelledDetailsDao.Properties.Number.eq(name)).list();
    }

    /**
     * 删除记录
     * @param sb
     */
    public void deleteAcd(AreCancelledDetails sb){

        AreCancelledDetails g = dao.queryBuilder().where(AreCancelledDetailsDao.Properties.Id.eq(sb.getId())).build().unique();

        if(g != null){
            //通过Key来删除，这里的Key就是user字段中的ID号
            dao.deleteByKey(g.getId());
        }

    }


}
