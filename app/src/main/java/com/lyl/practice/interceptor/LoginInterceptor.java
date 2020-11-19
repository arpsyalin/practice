package com.lyl.practice.interceptor;

import android.content.Context;

import com.lyl.arouter.ARouter;
import com.lyl.arouter.annotations.IInterceptor;

/**
 * * @Description 登录拦截器
 * * @Author 刘亚林
 * * @CreateDate 2020/10/7
 * * @Version 1.0
 * * @Remark TODO
 **/
public class LoginInterceptor implements IInterceptor {
    @Override
    public boolean interceptor(Object context) {
//        ARouter.getInstance().jumpActivity((Context) context, "com.lyl.login/LoginActivity");
        return false;
    }
}
