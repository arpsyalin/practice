package com.lyl.ifw.netframeapi;

import com.lyl.ifw.netexample.RequestMapBuilder;

/**
 * * @Description
 * * @Author 刘亚林
 * * @CreateDate 2020/10/29
 * * @Version 1.0
 * * @Remark TODO
 **/
public interface IDealFactory {
    IDealFactory cache(ICache iCache);

    IDealFactory network(INetwork network);

    IDealFactory defaultAop(IRequestAop requestAop);

    IDealFactory defaultAnalysis(IDataAnalysis iDataAnalysis);

    RequestMapBuilder clone();

    void request(RequestMapBuilder requestMapBuilder, IRequestCallBack iRequestCallBack);

    void request(RequestMapBuilder requestMapBuilder, final IRequestCallBack view, final IRequestAop requestAop);

    void cancel(RequestMapBuilder requestMapBuilder, final IRequestCallBack view, final IRequestAop requestAop);

    void cancel(RequestMapBuilder requestMapBuilder, final IRequestCallBack view);
}
