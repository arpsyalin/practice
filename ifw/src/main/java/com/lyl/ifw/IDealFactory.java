package com.lyl.ifw;

/**
 * * @Description
 * * @Author 刘亚林
 * * @CreateDate 2020/10/29
 * * @Version 1.0
 * * @Remark TODO
 **/
public interface IDealFactory {
    <T> void request(IView iView, RequestMapBuilder requestMapBuilder, Class<T> callBackClass);

    IDealFactory cache(ICache iCache);


    IDealFactory baseUrl(String baseUrl);

    IDealFactory network(INetwork network);

    IDealFactory rAop(IRequestAop requestAop);

    IDealFactory jsonAnalysis(IJsonAnalysis iJsonAnalysis);

    IDealFactory addHead(String key, String value);

    IDealFactory removeHead(String key);

    RequestMapBuilder clone();

    <T> void request(final IView view, RequestMapBuilder requestMapBuilder, final IRequestAop requestAop, final Class<T> callBackClass);


}
