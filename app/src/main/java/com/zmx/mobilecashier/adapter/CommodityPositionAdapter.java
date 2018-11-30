package com.zmx.mobilecashier.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zmx.mobilecashier.R;
import com.zmx.mobilecashier.bean.Goods;
import com.zmx.mobilecashier.ui.view.GlideCircleTransform;

import java.util.List;

/**
 * Created by Administrator on 2018-11-29.
 */

public class CommodityPositionAdapter extends BaseAdapter{

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<Goods> lists;

        public CommodityPositionAdapter(Context context,
                         List<Goods> list) {
            this.lists = list;
            this.mContext = context;
            mLayoutInflater = LayoutInflater.from(context);

        }

        /**
         * 数据总数
         */
        @Override
        public int getCount() {

            return lists.size();
        }

        /**
         * 获取当前数据
         */
        @Override
        public Object getItem(int position) {

            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            final ViewManHolder holder;
            if (view == null) {

                view = mLayoutInflater.inflate(R.layout.adapter_commodity_position, null);
                holder = new ViewManHolder(view);
                view.setTag(holder);

            } else {

                holder = (ViewManHolder) view.getTag();

            }

            holder.goods_name.setText(lists.get(position).getName());
            holder.goods_price.setText(lists.get(position).getGds_price());
            Glide.with(mContext).load("http://www.yiyuangy.com/uploads/goods/" + lists.get(position).getImg()).transform(new GlideCircleTransform(mContext)).error(R.mipmap.ic_shouji).into(holder.goods_image);

            return view;
        }


    class ViewManHolder {

        TextView goods_name, goods_price;
        ImageView goods_image;

        public ViewManHolder(View itemView) {

            goods_name = itemView.findViewById(R.id.goods_name);
            goods_price = itemView.findViewById(R.id.goods_price);
            goods_image = itemView.findViewById(R.id.goods_image);

        }
    }

    }
