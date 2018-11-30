package com.zmx.mobilecashier.adapter;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zmx.mobilecashier.R;
import com.zmx.mobilecashier.bean.CouponsMessage;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-29 23:35
 * 类功能：会员优惠卷
 */
public class MemberCouponsAdapter extends BaseAdapter{

    private Context context;
    private List<CouponsMessage> lists;
    private LayoutInflater inflater;

    public MemberCouponsAdapter(Context context,List<CouponsMessage> lists){

        this.lists = lists;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ViewManHolder holder;
        if (view == null) {

            view = inflater.inflate(R.layout.adapter_member_coupons, null);
            holder = new ViewManHolder(view);
            view.setTag(holder);

        } else {

            holder = (ViewManHolder) view.getTag();

        }

        holder.money_text.setText(lists.get(i).getC_quota()+"元");
        holder.conditions_text.setText("满"+lists.get(i).getC_term()+"元可用");
        holder.youxiaoqi_text.setText(lists.get(i).getValidity());
        return view;
    }

    class ViewManHolder {

        TextView money_text, describe, conditions_text, youxiaoqi_text;
        public ViewManHolder(View itemView) {

            money_text = itemView.findViewById(R.id.money_text);
            describe = itemView.findViewById(R.id.describe);
            conditions_text = itemView.findViewById(R.id.conditions_text);
            youxiaoqi_text = itemView.findViewById(R.id.youxiaoqi_text);

        }
    }

}
