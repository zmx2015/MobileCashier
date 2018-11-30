package com.zmx.mobilecashier.dao;

import com.zmx.mobilecashier.MyApplication;
import com.zmx.mobilecashier.bean.Group;
import com.zmx.mobilecashier.greendaos.GroupDao;

import java.util.List;


public class groupDao {

    private static GroupDao dao = MyApplication.getInstance().getDaoInstant().getGroupDao();

    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param
     */
    public long insertCp(Group cp) {

        return dao.insertOrReplace(cp);

    }

    /**
     * 查询全部数据
     */
    public List<Group> queryAll() {

        return dao.loadAll();
    }

}
