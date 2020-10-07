package com.lyl.arouter.annotations;

/**
 * * @Description 默认拦截器
 * * @Author 刘亚林
 * * @CreateDate 2020/10/7
 * * @Version 1.0
 * * @Remark TODO
 **/
public class DefaultInterceptor implements IInterceptor {
    @Override
    public boolean interceptor() {
        return false;
    }
}
