package com.zmx.mobilecashier.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zmx.mobilecashier.R;
import com.zmx.mobilecashier.adapter.CommodityPositionAdapter;
import com.zmx.mobilecashier.bean.Goods;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-12-01 10:12
 * 类功能：显示商品列表
 */

public class CommodityPositionFragment extends BaseFragment{

    private GridView gridView;
    //RecyclerView自定义Adapter
    private CommodityPositionAdapter adapter;
    //添加Header和Footer的封装类
    private List<Goods> lists;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commodity_position,container,false);

        lists = (List<Goods>) getArguments().getSerializable("goods");

        gridView = view.findViewById(R.id.gridView);
        adapter = new CommodityPositionAdapter(mActivity, lists);
        gridView.setAdapter(adapter);
        WindowManager wm = this.getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        gridView.setColumnWidth(width/10);
        gridView.setHorizontalSpacing(2);
        gridView.setVerticalSpacing(2);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Log.e("点击了",""+lists.get(position).getName());
                gs.setGoodsShopping(lists.get(position));

            }
        });

        return view;
    }


    @Override
    protected void initView() {

    }

    //商品放入购物车监听

    public GoodsShopping gs;

    public interface GoodsShopping{

       void setGoodsShopping(Goods g);

    }

    public void setGoodsShoppingListener(GoodsShopping gs){
        this.gs = gs;
    }

}
