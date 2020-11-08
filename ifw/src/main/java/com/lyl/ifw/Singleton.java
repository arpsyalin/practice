package com.lyl.ifw;

/**
 * * @Description 单例
 * * @Author 刘亚林
 * * @CreateDate 2020/10/29
 * * @Version 1.0
 * * @Remark TODO
 **/

public abstract class Singleton<T> {
    T instance;

    Singleton() {
        instance = create();
    }

    public abstract T create();

    public T get() {
        return instance;
    }
}