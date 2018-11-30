package com.zmx.mobilecashier.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.zmx.mobilecashier.MainActivity;
import com.zmx.mobilecashier.R;
import com.zmx.mobilecashier.adapter.StoreListAdapter;
import com.zmx.mobilecashier.bean.StoresMessage;
import com.zmx.mobilecashier.util.MySharedPreferences;

import java.util.List;


public class StoreListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    //支持下拉刷新的ViewGroup
    private PtrClassicFrameLayout mPtrFrame;
    private RecyclerAdapterWithHF mAdapter;
    //List数据
    private List<StoresMessage> lists;
    //RecyclerView自定义Adapter
    private StoreListAdapter adapter;
    //添加Header和Footer的封装类


    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_list;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);

        lists = MySharedPreferences.getInstance(this).getDataList("store");

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new StoreListAdapter(this, lists);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {


                MySharedPreferences.getInstance(mActivity).saveKeyObjValue(MySharedPreferences.fb_message, "0");//区分插入客服提示消息
                MySharedPreferences.getInstance(mActivity).saveKeyObjValue(MySharedPreferences.store_id, lists.get(position).getId() + "");
                MySharedPreferences.getInstance(mActivity).saveKeyObjValue(MySharedPreferences.store_name, lists.get(position).getAname());
                Intent intent = new Intent(StoreListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();//关闭页面

            }
        });
        mPtrFrame = findViewById(R.id.rotate_header_list_view_frame);
//下拉刷新支持时间
        mPtrFrame.setLastUpdateTimeRelateObject(this);
//下拉刷新一些设置 详情参考文档
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
// default is false
        mPtrFrame.setPullToRefresh(false);
// default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);

        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return false;
            }
        });
    }

}



