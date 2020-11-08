package com.lyl.network;

import android.text.TextUtils;

import com.lyl.ifw.DealFactory;
import com.lyl.ifw.IJsonAnalysis;
import com.lyl.ifw.INetwork;
import com.lyl.ifw.INetworkCallBack;
import com.lyl.ifw.IRequestAop;
import com.lyl.ifw.IView;
import com.lyl.ifw.Method;
import com.lyl.ifw.RequestMapBuilder;
import com.lyl.ifw.ResponseCode;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.lang.ref.WeakReference;
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
    public void request(final INetworkCallBack networkCallBack, final RequestMapBuilder requestMapBuilder) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder request = new Request.Builder().url(requestMapBuilder.getRequestUrl());
        Set<Map.Entry<String, String>> entrySet = requestMapBuilder.getHead().entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            request.addHeader(entry.getKey(), entry.getValue());
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
