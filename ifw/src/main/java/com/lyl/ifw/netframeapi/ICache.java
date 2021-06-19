package com.lyl.ifw.netframeapi;

import com.lyl.ifw.RequestMapBuilder;

/**
 * * @Description 缓存
 * * @Author 刘亚林
 * * @CreateDate 2020/10/29
 * * @Version 1.0
 * * @Remark TODO
 **/
public interface ICache {
    boolean saveCache(RequestMapBuilder requestMapBuilder, Object t);

    Object getCache(RequestMapBuilder requestMapBuilder );
}
