package com.lyl.arouter.annotations;


/**
 * * @Description 拦截器接口
 * * @Author 刘亚林
 * * @CreateDate 2020/10/7
 * * @Version 1.0
 * * @Remark TODO
 **/
public interface IInterceptor {
    boolean interceptor(Object context);
}
