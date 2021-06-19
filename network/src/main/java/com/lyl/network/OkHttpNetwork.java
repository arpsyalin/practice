package com.lyl.network;

import com.lyl.ifw.netframeapi.INetwork;
import com.lyl.ifw.netframeapi.INetworkCallBack;
import com.lyl.ifw.constant.Method;
import com.lyl.ifw.RequestMapBuilder;
import com.lyl.ifw.constant.ResponseCode;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * * @Description OkhttpNetwork封装
 * * @Author 刘亚林
 * * @CreateDate 2020/10/29
 * * @Version 1.0
 * * @Remark TODO
 **/
public class OkHttpNetwork implements INetwork {

    @Override
    public void request(final RequestMapBuilder requestMapBuilder, final INetworkCallBack networkCallBack) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder request = new Request.Builder().url(requestMapBuilder.getRequestUrl());
        Set<Map.Entry<String, Object>> entrySet = requestMapBuilder.getHead().entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            request.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
        }
        RequestBody requestBody;
        if (requestMapBuilder.getMethod() != Method.GET) {
            if (requestMapBuilder.getBody() != null) {
                requestBody = RequestBody.create(new JSONObject(requestMapBuilder.getBody()).toString(), MediaType.parse("application/json;charset=utf-8"));
            } else {
                requestBody = RequestBody.create("", MediaType.parse("application/json;charset=utf-8"));
            }
            request.method(requestMapBuilder.getMethod().name(), requestBody);
        } else {
            request.method(requestMapBuilder.getMethod().name(), null);
        }
        client.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                networkCallBack.netCallBack(ResponseCode.FAIL, requestMapBuilder, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String result = responseBody.string();
                networkCallBack.netCallBack(ResponseCode.FAIL, requestMapBuilder, result);
            }
        });
    }
}
