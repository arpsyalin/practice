package com.lyl.baseutil.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lyl.baseutil.adapter.viewholder.SmartViewHolder;

/**
 * * @Description 封装有空白页的Adapter
 * * @Author 刘亚林
 * * @CreateDate 2021/7/29
 * * @Version 1.0
 * * @Remark TODO
 **/
public abstract class BaseEmptyRecyclerAdapter<T> extends BaseRecyclerAdapter<T> {
    public static int EMPTY_ITEM = 9527;
    public static int CONTENT_ITEM = 9528;

    public BaseEmptyRecyclerAdapter() {
        setHasStableIds(false);
        this.mList = new ArrayList<>();
    }

    public BaseEmptyRecyclerAdapter(Collection<T> collection) {
        setHasStableIds(false);
        this.mList = new ArrayList<>(collection);
    }

    public BaseEmptyRecyclerAdapter(@LayoutRes int layoutId) {
        setHasStableIds(false);
        this.mList = new ArrayList<>();
        this.mLayoutId = layoutId;
    }

    public BaseEmptyRecyclerAdapter(T[] collection, @LayoutRes int layoutId) {
        setHasStableIds(false);
        this.mList = Arrays.asList(collection);
        this.mLayoutId = layoutId;
    }

    public BaseEmptyRecyclerAdapter(Collection<T> collection, @LayoutRes int layoutId) {
        setHasStableIds(false);
        this.mList = new ArrayList<>(collection);
        this.mLayoutId = layoutId;
    }

    public BaseEmptyRecyclerAdapter(Collection<T> collection, @LayoutRes int layoutId, AdapterView.OnItemClickListener listener) {
        setHasStableIds(false);
        setOnItemClickListener(listener);
        this.mList = new ArrayList<>(collection);
        this.mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public int getItemViewType(int position) {
        return isEmpty() ? EMPTY_ITEM : CONTENT_ITEM;
    }

    public int getItemCount() {
        return isEmpty() ? 1 : mList.size();
    }

    public int getViewTypeCount() {
        return 1;
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    protected void onBindViewHolder(SmartViewHolder holder, T model, int position) {
        if (model != null) {
            onBindDataViewHolder(holder, model, position);
        } else {
            onBindNoDataViewHolder(holder);
        }
    }

    protected void onBindNoDataViewHolder(SmartViewHolder holder) {
    }

    protected abstract void onBindDataViewHolder(SmartViewHolder holder, T model, int position);

    @Override
    public SmartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EMPTY_ITEM) {
            return new SmartViewHolder(LayoutInflater.from(parent.getContext()).inflate(getEmptyLayoutId(), parent, false));
        } else {
            if (needClick(viewType)) {
                return new SmartViewHolder(LayoutInflater.from(parent.getContext()).inflate(getDataLayoutId(viewType), parent, false), mListener);
            } else {
                return new SmartViewHolder(LayoutInflater.from(parent.getContext()).inflate(getDataLayoutId(viewType), parent, false));
            }
        }
    }

    protected boolean needClick(int viewType) {
        return true;
    }

    protected abstract int getEmptyLayoutId();

    protected abstract int getDataLayoutId(int viewType);

    public void notifyVisibleItem(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager.getClass() == GridLayoutManager.class) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            int start = gridLayoutManager.findFirstVisibleItemPosition();
            int end = gridLayoutManager.findLastVisibleItemPosition();
            if (start == -1 || end == -1) {
                return;
            }
            notifyItemRangeChanged(start, end - start + 1);
        } else if (layoutManager.getClass() == LinearLayoutManager.class) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            int start = linearManager.findFirstVisibleItemPosition();
            int end = linearManager.findLastVisibleItemPosition();
            if (start == -1 || end == -1) {
                return;
            }
            notifyItemRangeChanged(start, end - start + 1);
        }
    }
}
