package com.lyl.ifw.netframeapi;

/**
 * * @Description
 * * @Author 刘亚林
 * * @CreateDate 2021/6/18
 * * @Version 1.0
 * * @Remark TODO
 **/
public interface IRequestMapBuilder extends Cloneable {
    long getRequestId();

    Object clone() throws CloneNotSupportedException;
}
