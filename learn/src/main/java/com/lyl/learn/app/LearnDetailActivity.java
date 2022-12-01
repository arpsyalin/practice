package com.lyl.learn.app;

import androidx.annotation.NonNull;

import android.os.Bundle;

import com.lyl.base_app.BaseListActivity;
import com.lyl.baseutil.adapter.viewholder.SmartViewHolder;
import com.lyl.learn.R;
import com.lyl.learn.model.ItemModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

public class LearnDetailActivity extends BaseListActivity<ItemModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        initData();
        initView();
    }

    private void initData() {

    }

    @Override
    protected void onListBindDataViewHolder(SmartViewHolder holder, ItemModel model, int position) {
        holder.text(R.id.tv_title, model.getCn() + "\n" + model.getEn());
    }

    @Override
    protected int getListDataLayoutId(int viewType) {
        return R.layout.item_learn_model;
    }

    @Override
    protected int getListEmptyLayoutId() {
        return R.layout.list_empty;
    }

    @Override
    protected int getSRViewId() {
        return R.id.smart_refresh;
    }

    @Override
    protected int getRVListId() {
        return R.id.rv_list;
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.finishRefresh();
    }
}