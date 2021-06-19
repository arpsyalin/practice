package com.lyl.ifw.netexample;

import android.text.TextUtils;

import com.lyl.ifw.netframeapi.IRequestMapBuilder;
import com.lyl.ifw.constant.Method;
import com.lyl.ifw.utils.SnowflakeIdWorker;

import java.util.HashMap;

import androidx.annotation.NonNull;

/**
 * * @Description 请求Map构建例子
 * * @Author 刘亚林
 * * @CreateDate 2020/10/29
 * * @Version 1.0
 * * @Remark TODO
 **/
public class RequestMapBuilder implements IRequestMapBuilder {
    HashMap<String, Object> body = new HashMap();
    HashMap<String, Object> head = new HashMap();
    Method method = Method.GET;
    String baseUrl;
    String actionUrl;
    long requestId;
    int type;
    boolean needAop;
    boolean useCache = true;

    private RequestMapBuilder() {
        requestId = SnowflakeIdWorker.getInstance().nextId();
    }

    public static RequestMapBuilder build() {
        return new RequestMapBuilder();
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        RequestMapBuilder requestMapBuilder = new RequestMapBuilder();
        requestMapBuilder.baseUrl = baseUrl;
        requestMapBuilder.head = head;
        requestMapBuilder.body = null;
        requestMapBuilder.method = Method.GET;
        requestMapBuilder.actionUrl = null;
        requestMapBuilder.type = type;
        requestMapBuilder.needAop = needAop;
        requestMapBuilder.useCache = useCache;
        return requestMapBuilder;
    }

    @Override
    public long getRequestId() {
        return requestId;
    }

    public HashMap<String, Object> getBody() {
        return body;
    }

    public void setBody(HashMap<String, Object> body) {
        this.body = body;
    }

    public HashMap<String, Object> getHead() {
        return head;
    }

    public void setHead(HashMap<String, Object> head) {
        this.head = head;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isNeedAop() {
        return needAop;
    }

    public void setNeedAop(boolean needAop) {
        this.needAop = needAop;
    }

    public boolean isUseCache() {
        return useCache;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    public String getRequestUrl() {
        if (TextUtils.isEmpty(baseUrl) && TextUtils.isEmpty(actionUrl)) {
            return null;
        }
        if (TextUtils.isEmpty(baseUrl)) {
            return actionUrl;
        }
        if (TextUtils.isEmpty(actionUrl)) {
            return baseUrl;
        }
        if (!baseUrl.endsWith("/") && !actionUrl.startsWith("/")) {
            return baseUrl + "/" + actionUrl;
        } else if (baseUrl.endsWith("/") && actionUrl.startsWith("/")) {
            return baseUrl + actionUrl.substring(1);
        }
        return baseUrl + actionUrl;
    }

    @Override
    public String toString() {
        return "RequestMapBuilder{" +
                "body=" + body +
                ", head=" + head +
                ", method=" + method +
                ", baseUrl='" + baseUrl + '\'' +
                ", actionUrl='" + actionUrl + '\'' +
                ", type=" + type +
                ", needAop=" + needAop +
                ", useCache=" + useCache +
                '}';
    }
}
