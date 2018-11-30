package com.zmx.mobilecashier.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-28 19:23
 * 类功能：商品分类的名称 导航条用到
 */
public class CPFragmentAdapter extends FragmentPagerAdapter {

    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList;
    private List<String> titles;

    public CPFragmentAdapter(FragmentManager fm, List<Fragment> list, List<String> titles) {
        super(fm);
        this.fragmentManager = fm;
        this.fragmentList = list;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
