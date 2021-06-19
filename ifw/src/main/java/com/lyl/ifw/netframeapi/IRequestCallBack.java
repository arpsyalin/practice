package com.lyl.ifw.netframeapi;

import com.lyl.ifw.netexample.RequestMapBuilder;

/**
 * * @Description 布局
 * * @Author 刘亚林
 * * @CreateDate 2020/10/29
 * * @Version 1.0
 * * @Remark TODO
 **/
public interface IRequestCallBack<T> extends IStart, IEnd {
    void notifyResult(int resultCode, RequestMapBuilder requestMapBuilder, Object callBackData);
}
