package com.zmx.mobilecashier.dao;

import com.zmx.mobilecashier.MyApplication;
import com.zmx.mobilecashier.bean.AreCancelled;
import com.zmx.mobilecashier.bean.AreCancelledDetails;
import com.zmx.mobilecashier.greendaos.AreCancelledDao;
import com.zmx.mobilecashier.greendaos.AreCancelledDetailsDao;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-30 19:51
 * 类功能：挂单
 */

public class areCancelledDao {


    private static AreCancelledDao dao = MyApplication.getInstance().getDaoInstant().getAreCancelledDao();


    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param
     */
    public long insertCp(AreCancelled ac) {
        return dao.insertOrReplace(ac);
    }

    /**
     * 查询全部数据
     */
    public List<AreCancelled> queryAll() {
        return dao.loadAll();
    }

    /**
     * 修改
     */
    public void UpdateAc(String number,String member,double d){

        //1.where是查询条件，
        //2.unique()表示查询结果为一条数据，若数据不存在，findUser为null。
        AreCancelled g = dao.queryBuilder().where(AreCancelledDao.Properties.Number.eq(number)).build().unique();
        //如果没有就添加进入本地
        if(g != null) {


            g.setMembers(member);
            g.setDiscount(d);
            dao.update(g);

        }

    }


    /**
     * 删除记录
     * @param ac
     */
    public void deleteAc(AreCancelled ac){

        AreCancelled g = dao.queryBuilder().where(AreCancelledDao.Properties.Id.eq(ac.getId())).build().unique();

        if(g != null){
            //通过Key来删除，这里的Key就是user字段中的ID号
            dao.deleteByKey(g.getId());
        }

    }

    /**
     * 根据商品订单编号删除
     * @param number
     */
    public void deleteAc(String number){

        AreCancelled g = dao.queryBuilder().where(AreCancelledDao.Properties.Number.eq(number)).build().unique();

        if(g != null){

            //通过Key来删除，这里的Key就是user字段中的ID号
            dao.deleteByKey(g.getId());

        }
    }

    public void deleteData(){

        dao.deleteAll();

    }




}
