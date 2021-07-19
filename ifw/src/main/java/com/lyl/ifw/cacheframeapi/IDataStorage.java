package com.lyl.ifw.cacheframeapi;

import android.content.Context;

import java.util.Collection;

/**
 * * @Description 数据存储接口
 * * @Author 刘亚林
 * * @CreateDate 2021/6/19
 * * @Version 1.0
 * * @Remark TODO
 **/
public interface IDataStorage {
    boolean put(Context context, String key, String subKey, Object object);

    boolean put(Context context, String key, String subKey, Collection object);

    Object get(Context context, String key, String subKey);

    <T> T get(Context context, String key, String subKey, Class<T> clazz);
}
