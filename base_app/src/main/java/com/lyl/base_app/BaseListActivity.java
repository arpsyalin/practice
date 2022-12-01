package com.lyl.base_app;

import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lyl.baseutil.adapter.BaseEmptyRecyclerAdapter;
import com.lyl.baseutil.adapter.viewholder.SmartViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public abstract class BaseListActivity<T> extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {
    protected RecyclerView mRvList;
    protected SmartRefreshLayout mSmartRefresh;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected boolean isInitView = false;

    protected abstract void onListBindDataViewHolder(SmartViewHolder holder, T model, int position);

    protected abstract int getListDataLayoutId(int viewType);

    protected BaseEmptyRecyclerAdapter<T> mBaseEmptyRecyclerAdapter = new BaseEmptyRecyclerAdapter<T>() {
        @Override
        protected void onBindDataViewHolder(SmartViewHolder holder, T model, int position) {
            onListBindDataViewHolder(holder, model, position);
        }

        @Override
        protected void onBindNoDataViewHolder(SmartViewHolder holder) {
            onListBindNoDataViewHolder(holder);
        }

        @Override
        protected int getEmptyLayoutId() {
            return getListEmptyLayoutId();
        }

        @Override
        protected int getDataLayoutId(int viewType) {
            return getListDataLayoutId(viewType);
        }

        protected boolean needClick(int viewType) {
            return needListClick(viewType);
        }

        public int getItemViewType(int position) {
            return isEmpty() ? BaseEmptyRecyclerAdapter.EMPTY_ITEM : getListItemViewType(position);
        }
    };

    protected void onListBindNoDataViewHolder(SmartViewHolder holder) {

    }

    protected boolean needListClick(int viewType) {
        return true;
    }

    protected int getListItemViewType(int position) {
        return BaseEmptyRecyclerAdapter.CONTENT_ITEM;
    }

    //一定要记得调用
    protected void initView() {
        isInitView = true;
        mRvList = findViewById(getRVListId());
        mSmartRefresh = findViewById(getSRViewId());
        if (mRvList != null) {
            mRvList.setLayoutManager(getLayoutManager());
            mRvList.setAdapter(mBaseEmptyRecyclerAdapter);
            mBaseEmptyRecyclerAdapter.setOnItemClickListener(getOnItemClickListener());
        }
        if (mSmartRefresh != null) {
            mSmartRefresh.setOnRefreshListener(this);
            mSmartRefresh.setEnableLoadMore(needLoadMore());
            if (needLoadMore()) {
                mSmartRefresh.setOnLoadMoreListener(this);
            }
        }
    }

    protected boolean needLoadMore() {
        return true;
    }

    protected abstract int getListEmptyLayoutId();

    protected abstract int getSRViewId();

    protected abstract int getRVListId();

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
    }

    protected AdapterView.OnItemClickListener getOnItemClickListener() {
        return null;
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        mLayoutManager = new LinearLayoutManager(getContext());
        return mLayoutManager;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
