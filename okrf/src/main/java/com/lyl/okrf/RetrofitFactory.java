package com.lyl.okrf;

import android.text.TextUtils;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class RetrofitFactory {
    private String baseUrl = "http://localhost/";
    private HashMap<Class<?>, Object> classObject;
    public Retrofit retrofit;
    OkHttpClient okHttpClient;
    Converter.Factory converterFactory;
    CallAdapter.Factory callAdapterFactory;

    public RetrofitFactory(String baseUrl, HashMap<Class<?>, Object> classObject, Retrofit retrofit, OkHttpClient okHttpClient, Converter.Factory converterFactory, CallAdapter.Factory callAdapterFactory) {
        this.baseUrl = baseUrl;
        this.classObject = classObject;
        this.retrofit = retrofit;
        this.okHttpClient = okHttpClient;
        this.converterFactory = converterFactory;
        this.callAdapterFactory = callAdapterFactory;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public <T> T getT(Class<T> tClass) throws Exception {
        if (classObject.containsKey(tClass)) {
            if (retrofit != null) {
                Object object = classObject.get(tClass);

                if (object.equals(0)) {
                    T t = retrofit.create(tClass);
                    classObject.put(tClass, t);
                    return t;
                } else {
                    return (T) classObject.get(tClass);
                }
            } else {
                throw new Exception("no init retrofit");
            }
        } else {
            throw new Exception("no " + tClass.getName());
        }
    }

    public synchronized RetrofitFactory service(Class<?>... clazzs) {
        for (Class<?> clazz : clazzs) {
            classObject.put(clazz, 0);
        }
        return this;
    }

    public static class Builder {
        private Object emptyObject = new Object();
        private String baseUrl = "http://localhost/";
        private HashMap<Class<?>, Object> classObject;
        private Retrofit retrofit;

        OkHttpClient okHttpClient;
        Converter.Factory converterFactory;
        CallAdapter.Factory callAdapterFactory;

        public Builder() {
            classObject = new HashMap<>();
        }

        public Builder okHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public Builder converterFactory(Converter.Factory converterFactory) {
            this.converterFactory = converterFactory;
            return this;
        }

        public Builder callAdapterFactory(CallAdapter.Factory callAdapterFactory) {
            this.callAdapterFactory = callAdapterFactory;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder service(Class<?>... clazzs) {
            for (Class<?> clazz : clazzs) {
                classObject.put(clazz, 0);
            }
            return this;
        }


        public synchronized RetrofitFactory build() throws Exception {
            Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
            if (okHttpClient != null) {
                retrofitBuilder.client(okHttpClient);
            } else {
                throw new Exception("please set OKHttpClient");
            }
            if (TextUtils.isEmpty(baseUrl)) {
                throw new Exception("please set baseUrl");
            }
            retrofitBuilder.baseUrl(baseUrl);
            if (converterFactory != null) {
                retrofitBuilder.addConverterFactory(converterFactory);
            }
            if (callAdapterFactory != null) {
                retrofitBuilder.addCallAdapterFactory(callAdapterFactory);
            }
            retrofit = retrofitBuilder.build();
            return new RetrofitFactory(baseUrl, classObject, retrofit, okHttpClient, converterFactory, callAdapterFactory);
        }
    }
}
