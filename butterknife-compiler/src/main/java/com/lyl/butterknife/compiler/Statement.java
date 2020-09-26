package com.lyl.butterknife.compiler;

/**
 * * @Description Statement构建属性存储对象
 * * @Author 刘亚林
 * * @CreateDate 2020/9/26
 * * @Version 1.0
 * * @Remark TODO
 **/
public class Statement {
    String format;
    Object[] args;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
