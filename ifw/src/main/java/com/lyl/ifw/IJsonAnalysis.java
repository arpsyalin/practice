package com.lyl.ifw;

/**
 * * @Description Json转换
 * * @Author 刘亚林
 * * @CreateDate 2020/10/30
 * * @Version 1.0
 * * @Remark TODO
 **/
public interface IJsonAnalysis {
    <T> T toJson(String data, Class<T> clazz);
}
