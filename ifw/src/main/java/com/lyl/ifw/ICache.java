package com.lyl.ifw;

/**
 * * @Description 缓存
 * * @Author 刘亚林
 * * @CreateDate 2020/10/29
 * * @Version 1.0
 * * @Remark TODO
 **/
public interface ICache {
    <T> boolean saveCache(RequestMapBuilder requestMapBuilder, T t, Class<T> dataClass);

    <T> T getCache(RequestMapBuilder requestMapBuilder, Class<T> dataClass);
}
