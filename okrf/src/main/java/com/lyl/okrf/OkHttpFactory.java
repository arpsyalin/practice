package com.lyl.okrf;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpFactory {
    SSLSocketFactory sslSocketFactory;
    HttpLoggingInterceptor httpLoggingInterceptor;
    boolean retryOnConnectionFailure = false;
    long timeout = 10;
    HostnameVerifier hostnameVerifier;
    Interceptor[] customInterceptor = new Interceptor[]{};

    public OkHttpFactory loggingInterceptor(HttpLoggingInterceptor.Level level) {
        httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(level);
        return this;
    }

    public OkHttpFactory interceptors(Interceptor... interceptors) {
        if (customInterceptor == null) return this;
        customInterceptor = interceptors;
        return this;
    }

    public OkHttpFactory setSSLSocketFactory(String protocol, String keyAlgorithm, String trustAlgorithm, String keyType, String trustType, InputStream keyFile, InputStream trustFile, String password) {
        try {
            //取得SSL的SSLContext实例
            SSLContext sslContext = SSLContext.getInstance(protocol);
            //取得KeyManagerFactory和TrustManagerFactory的X509密钥管理器实例
            KeyManagerFactory keyManager = KeyManagerFactory.getInstance(keyAlgorithm);
            TrustManagerFactory trustManager = TrustManagerFactory.getInstance(trustAlgorithm);
            //取得BKS密库实例
            KeyStore kks = KeyStore.getInstance(keyType);
            KeyStore tks = KeyStore.getInstance(trustType);
            //加客户端载证书和私钥,通过读取资源文件的方式读取密钥和信任证书
            kks.load(keyFile, password.toCharArray());
            tks.load(trustFile, password.toCharArray());
            //初始化密钥管理器
            keyManager.init(kks, password.toCharArray());
            trustManager.init(tks);
            //初始化SSLContext
            sslContext.init(keyManager.getKeyManagers(), trustManager.getTrustManagers(), null);
            //生成SSLSocket
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public OkHttpFactory setTimeOut(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public OkHttpFactory setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }


    public OkHttpClient build() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(retryOnConnectionFailure);
        builder.connectTimeout(timeout, TimeUnit.SECONDS);
        builder.readTimeout(timeout, TimeUnit.SECONDS);
        if (hostnameVerifier != null)
            builder.hostnameVerifier(hostnameVerifier);
        if (sslSocketFactory != null)
            builder.sslSocketFactory(sslSocketFactory);
        if (httpLoggingInterceptor != null)
            builder.addInterceptor(httpLoggingInterceptor);
        for (Interceptor it : customInterceptor) {
            builder.addInterceptor(it);
        }
        return builder.build();
    }
}
