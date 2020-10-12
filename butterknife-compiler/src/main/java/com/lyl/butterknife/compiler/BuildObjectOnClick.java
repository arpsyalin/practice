package com.lyl.butterknife.compiler;

import com.lyl.butterknife.annotations.OnClick;


import javax.lang.model.element.Element;

/**
 * * @Description BindView构建对象
 * * @Author 刘亚林
 * * @CreateDate 2020/9/26
 * * @Version 1.0
 * * @Remark TODO
 **/
public class BuildObjectOnClick extends BuildObject {
    public BuildObjectOnClick(Class clazz, Element element) {
        super(clazz, element);
    }


    @Override
    public void initStatement() {
        String filedName = element.getSimpleName().toString();
        int[] resIds = element.getAnnotation(OnClick.class).value();
        for (int resId : resIds) {
            Statement statement = new Statement();
            Object[] objects = new Object[2];
            objects[0] = resId;
            objects[1] = filedName;
            statement.setFormat("target.findViewById($L).setOnClickListener(new android.view.View.OnClickListener() { @Override public void onClick(android.view.View v) {target.$N(v);}})");
            statement.setArgs(objects);
            statements.add(statement);
//            System.out.println("BuildObjectOnClick initStatement end");
        }
    }

}
