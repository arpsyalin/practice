package com.lyl.arouter.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(CLASS)
@Target(ElementType.TYPE)
public @interface IsActivity {
    String value();

    Class<? extends IInterceptor> interceptor() default DefaultInterceptor.class;
}
