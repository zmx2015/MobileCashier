package com.zmx.mobilecashier.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zmx.mobilecashier.R;
import com.zmx.mobilecashier.bean.AreCancelled;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-30 20:58
 * 类功能：取单主订单的适配器
 */

public class AreCancelledAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<AreCancelled> lists;

    private int defaultSelection = -1;
    private int text_selected_color;
    private int bg_selected_color;
    private ColorStateList colors;

    public AreCancelledAdapter(Context context, List<AreCancelled> title){
        mContext=context;
        lists=title;
        mLayoutInflater=LayoutInflater.from(context);

        Resources resources = context.getResources();
        text_selected_color = resources.getColor(R.color.white);// 文字选中的颜色
        bg_selected_color = resources.getColor(R.color.tou);// 背景选中的颜色
        colors = context.getResources().getColorStateList(
                R.color.grey_700);// 文字未选中状态的selector
        resources = null;

    }

    @Override
    public int getCount() {
        return lists==null ? 0 : lists.size();
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
    public View getView(int position, View view, ViewGroup viewGroup) {

        final ViewManHolder holder;
        if (view == null) {

            view = mLayoutInflater.inflate(R.layout.adapter_are_cancelled, null);
            holder = new ViewManHolder(view);
            view.setTag(holder);

        } else {

            holder = (ViewManHolder) view.getTag();

        }

        holder.textView2.setText("订单总额："+lists.get(position).getTotal());
        holder.textView3.setText("会员账号："+lists.get(position).getMembers());
        holder.textView4.setText("订单时间："+lists.get(position).getDate());
        holder.ranking.setText((position+1)+"");

        if (position == defaultSelection) {
            // 选中时设置单纯颜色
            view.setBackgroundColor(bg_selected_color);
        } else {

            // 未选中时设置selector
            view.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        }

        return view;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewManHolder{
        TextView textView2,textView3,textView4,ranking;

        public ViewManHolder(View itemView) {

            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            ranking = itemView.findViewById(R.id.ranking);

        }
    }

    /**
     * @param position
     *            设置高亮状态的item
     */
    public void setSelectPosition(int position) {
        if (!(position < 0 || position > lists.size())) {
            defaultSelection = position;
        }else{
            defaultSelection = position;
        }

        notifyDataSetChanged();
    }

}