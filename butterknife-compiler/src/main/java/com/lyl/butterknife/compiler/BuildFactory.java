package com.lyl.butterknife.compiler;

import com.lyl.butterknife.annotations.BindView;
import com.lyl.butterknife.annotations.OnClick;

import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;

/**
 * * @Description 构建Build对象
 * * @Author 刘亚林
 * * @CreateDate 2020/9/26
 * * @Version 1.0
 * * @Remark TODO
 **/
public class BuildFactory {
    static BuildFactory instance = new BuildFactory();

    public static BuildFactory getInstance() {
        return instance;
    }

    public BuildObject getBuildObject(Class<? extends Annotation> clazz, Element element) {
        if (clazz == BindView.class) {
            return new BuildObjectBindView(clazz, element);
        } else if (clazz == OnClick.class) {
            return new BuildObjectOnClick(clazz, element);
        }
        return null;
    }
}
