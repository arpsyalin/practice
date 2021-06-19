package com.lyl.ifw.netframeapi;

import com.lyl.ifw.RequestMapBuilder;

/**
 * * @Description 请求
 * * @Author 刘亚林
 * * @CreateDate 2020/10/29
 * * @Version 1.0
 * * @Remark TODO
 **/
public interface IRequest {
    void request(RequestMapBuilder requestMapBuilder, INetworkCallBack networkCallBack);
}
