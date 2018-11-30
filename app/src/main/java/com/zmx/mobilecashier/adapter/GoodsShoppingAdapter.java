package com.zmx.mobilecashier.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zmx.mobilecashier.R;
import com.zmx.mobilecashier.bean.ViceOrder;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-29 15:37
 * 类功能：购物车的适配器
 */
public class GoodsShoppingAdapter extends BaseAdapter{

    private List<ViceOrder> lists;
    private LayoutInflater inflater;
    private Context context;

    private int defaultSelection = -1;
    private int text_selected_color;
    private int bg_selected_color;
    private ColorStateList colors;

    public GoodsShoppingAdapter(List<ViceOrder> lists,Context context){

        this.lists = lists;
        this.context = context;
        inflater = LayoutInflater.from(context);

        Resources resources = context.getResources();
        text_selected_color = resources.getColor(R.color.white);// 文字选中的颜色
        bg_selected_color = resources.getColor(R.color.tou);// 背景选中的颜色
        colors = context.getResources().getColorStateList(
                R.color.grey_700);// 文字未选中状态的selector
        resources = null;


    }

    @Override
    public int getCount() {
        return lists == null ? 0 : lists.size();
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

            view = inflater.inflate(R.layout.adapter_goods_shopping, null);
            holder = new ViewManHolder(view);
            view.setTag(holder);

        } else {

            holder = (ViewManHolder) view.getTag();

        }

        holder.textView0.setText((i+1)+"");
        holder.textView1.setText(lists.get(i).getVo_name());
        holder.textView2.setText(lists.get(i).getVo_weight());
        holder.textView3.setText(lists.get(i).getVo_price());
        holder.textView4.setText(lists.get(i).getVo_subtotal());

        if (i == defaultSelection) {// 选中时设置单纯颜色
            holder.textView0.setTextColor(text_selected_color);
            holder.textView1.setTextColor(text_selected_color);
            holder.textView2.setTextColor(text_selected_color);
            holder.textView3.setTextColor(text_selected_color);
            holder.textView4.setTextColor(text_selected_color);
            view.setBackgroundColor(bg_selected_color);
        } else {// 未选中时设置selector

            holder.textView0.setTextColor(colors);
            holder.textView1.setTextColor(colors);
            holder.textView2.setTextColor(colors);
            holder.textView3.setTextColor(colors);
            holder.textView4.setTextColor(colors);
            view.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        return view;
    }


    class ViewManHolder {

        TextView textView0, textView1, textView2, textView3, textView4;

        public ViewManHolder(View itemView) {

            textView0 = itemView.findViewById(R.id.textView0);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);

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