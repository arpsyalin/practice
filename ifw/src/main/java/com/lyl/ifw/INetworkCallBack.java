package com.lyl.ifw;

/**
 * * @Description 网络回调
 * * @Author 刘亚林
 * * @CreateDate 2020/10/30
 * * @Version 1.0
 * * @Remark TODO
 **/
public interface INetworkCallBack {
    void netCallBack(int fail, RequestMapBuilder requestMapBuilder, String o);
}
