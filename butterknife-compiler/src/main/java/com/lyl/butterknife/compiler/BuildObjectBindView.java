package com.lyl.butterknife.compiler;

import com.lyl.butterknife.annotations.BindView;

import javax.lang.model.element.Element;

/**
 * * @Description BindView构建对象
 * * @Author 刘亚林
 * * @CreateDate 2020/9/26
 * * @Version 1.0
 * * @Remark TODO
 **/
public class BuildObjectBindView extends BuildObject {

    public BuildObjectBindView(Class clazz, Element element) {
        super(clazz, element);
    }

    @Override
    public void initStatement() {
        String filedName = this.element.getSimpleName().toString();
        String type = this.element.asType().toString();
        int resId = this.element.getAnnotation(BindView.class).value();
        Statement statement = new Statement();
        Object[] objects = new Object[3];
        objects[0] = filedName;
        objects[1] = type;
        objects[2] = resId;
        statement.setFormat("target.$L =($L) target.findViewById($L)");
        statement.setArgs(objects);
        statements.add(statement);
        System.out.println("BuildObjectBindView initStatement end");
    }
}
