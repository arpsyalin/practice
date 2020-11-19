package com.lyl.arouter.livedata;

import java.util.HashMap;
import java.util.Map;

/**
 * * @Description LiveData工厂
 * * @Author 刘亚林
 * * @CreateDate 2020/10/8
 * * @Version 1.0
 * * @Remark 为什么写成在ARouter里面仅仅是为了提醒应该使用LiveData做数据传输监听
 **/
public class ALiveDataFactory {
    private static ALiveDataFactory instance = new ALiveDataFactory();
    Map<String, ALiveData<Object>> mALiveDataMap;

    private ALiveDataFactory() {
        mALiveDataMap = new HashMap<>();
    }

    public static ALiveDataFactory getInstance() {
        return instance;
    }

    public <T> ALiveData<T> withActivity(String jumpActivity, String key, Class<T> clazz) {
        return with(jumpActivity + "$" + key, clazz);
    }

    public <T> ALiveData<T> with(String key, Class<T> clazz) {
        if (!mALiveDataMap.containsKey(key)) {
            mALiveDataMap.put(key, new ALiveData<Object>());
        }
        return (ALiveData<T>) mALiveDataMap.get(key);
    }

    //移除
    public void remove(String key) {
        mALiveDataMap.remove(key);
    }
}
