package com.lyl.ifw;

/**
 * * @Description 布局
 * * @Author 刘亚林
 * * @CreateDate 2020/10/29
 * * @Version 1.0
 * * @Remark TODO
 **/
public interface IView<T> {
    void notifyResult(int resultCode, RequestMapBuilder requestMapBuilder, T callBackData );
}
