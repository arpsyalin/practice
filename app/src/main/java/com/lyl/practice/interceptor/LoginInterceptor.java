package com.lyl.practice.interceptor;

import com.lyl.arouter.ARouter;
import com.lyl.arouter.annotations.IInterceptor;
import com.lyl.practice.PApplication;

/**
 * * @Description 登录拦截器
 * * @Author 刘亚林
 * * @CreateDate 2020/10/7
 * * @Version 1.0
 * * @Remark TODO
 **/
public class LoginInterceptor implements IInterceptor {
    @Override
    public boolean interceptor() {
        ARouter.getInstance().jumpActivity(PApplication.getInstant(), "com.lyl.login/LoginActivity");
        return true;
    }
}
