package com.lyl.baseutil.adapter;


import android.database.DataSetObservable;
import android.database.DataSetObserver;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.lyl.baseutil.adapter.viewholder.SmartViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<SmartViewHolder> implements ListAdapter {

    protected int mLayoutId;
    protected List<T> mList;
    private int mLastPosition = -1;
    private boolean mOpenAnimationEnable = true;
    protected AdapterView.OnItemClickListener mListener;

    public BaseRecyclerAdapter() {
        setHasStableIds(false);
        this.mList = new ArrayList<>();
    }

    public BaseRecyclerAdapter(Collection<T> collection) {
        setHasStableIds(false);
        this.mList = new ArrayList<>(collection);
    }

    public BaseRecyclerAdapter(@LayoutRes int layoutId) {
        setHasStableIds(false);
        this.mList = new ArrayList<>();
        this.mLayoutId = layoutId;
    }

    public BaseRecyclerAdapter(T[] collection, @LayoutRes int layoutId) {
        setHasStableIds(false);
        this.mList = Arrays.asList(collection);
        this.mLayoutId = layoutId;
    }

    public BaseRecyclerAdapter(Collection<T> collection, @LayoutRes int layoutId) {
        setHasStableIds(false);
        this.mList = new ArrayList<>(collection);
        this.mLayoutId = layoutId;
    }

    public BaseRecyclerAdapter(Collection<T> collection, @LayoutRes int layoutId, AdapterView.OnItemClickListener listener) {
        setHasStableIds(false);
        setOnItemClickListener(listener);
        this.mList = new ArrayList<>(collection);
        this.mLayoutId = layoutId;
    }

    private void addAnimate(SmartViewHolder holder, int position) {
        if (mOpenAnimationEnable) {
//            && mLastPosition < position
            holder.itemView.setAlpha(0);
            holder.itemView.animate().alpha(1).start();
            mLastPosition = position;
        }
    }

    @Override
    public SmartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SmartViewHolder(LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(SmartViewHolder holder, int position) {
        onBindViewHolder(holder, position < mList.size() ? mList.get(position) : null, position);
    }

    protected abstract void onBindViewHolder(SmartViewHolder holder, T model, int position);

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onViewAttachedToWindow(SmartViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        addAnimate(holder, holder.getLayoutPosition());
    }

    public void setOpenAnimationEnable(boolean enabled) {
        this.mOpenAnimationEnable = enabled;
    }

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }


    public void notifyListDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }


    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    public boolean areAllItemsEnabled() {
        return true;
    }

    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SmartViewHolder holder;
        if (convertView != null) {
            holder = (SmartViewHolder) convertView.getTag();
        } else {
            holder = onCreateViewHolder(parent, getItemViewType(position));
            convertView = holder.itemView;
            convertView.setTag(holder);
        }
        holder.setPosition(position);
        onBindViewHolder(holder, position);
        addAnimate(holder, position);
        return convertView;
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public T get(int index) {
        return mList.get(index);
    }

    public BaseRecyclerAdapter<T> setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mListener = listener;
        return this;
    }

    public BaseRecyclerAdapter<T> loadMore(Collection<T> collection) {
        if (collection != null) {
            mList.addAll(collection);
        }
        notifyDataSetChanged();
        notifyListDataSetChanged();
        return this;
    }

    public BaseRecyclerAdapter<T> refresh(Collection<T> collection) {

        mList.clear();
        if (collection != null) {
            mList.addAll(collection);
        }
        notifyDataSetChanged();
        notifyListDataSetChanged();
        mLastPosition = -1;
        return this;
    }

    public boolean contains(T t) {
        return mList.contains(t);
    }

    public BaseRecyclerAdapter<T> insert(Collection<T> collection) {
        mList.addAll(0, collection);
        notifyItemRangeInserted(0, collection.size());
        notifyListDataSetChanged();
        return this;
    }

    public BaseRecyclerAdapter<T> remove(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        if (position != mList.size())
            notifyItemRangeChanged(position, mList.size() - position);
        return this;
    }

    public BaseRecyclerAdapter<T> remove(int position, int size) {
        for (int i = 0; i < size; i++) {
            if (mList.size() > position) {
                mList.remove(position);
            }
        }
        notifyItemRangeChanged(position, size);
        return this;
    }

    public BaseRecyclerAdapter<T> insert(int position, T data) {
        if (data == null) return this;
        mList.add(position, data);
        notifyItemInserted(position);
        return this;
    }

    public BaseRecyclerAdapter<T> swipe(int position, int targetPosition) {
        Collections.swap(mList, position, targetPosition);
        notifyItemMoved(position, targetPosition);
        return this;
    }

    public List<T> getData() {
        return new ArrayList<>(mList);
    }

}
