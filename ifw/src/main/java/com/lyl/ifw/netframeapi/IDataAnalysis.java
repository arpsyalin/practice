package com.lyl.ifw.netframeapi;

import com.lyl.ifw.RequestMapBuilder;

/**
 * * @Description Json转换
 * * @Author 刘亚林
 * * @CreateDate 2020/10/30
 * * @Version 1.0
 * * @Remark TODO
 **/
public interface IDataAnalysis {
    Object analysis(RequestMapBuilder requestMapBuilder, Object data);
}
