package com.lyl.okrf;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HttpMethod {
    private static HttpMethod instance;
    List<RetrofitFactory> retrofitFactories;

    private HttpMethod() {
        retrofitFactories = new ArrayList<>();
    }

    public static HttpMethod getInstance() {
        if (instance == null) {
            synchronized (HttpMethod.class) {
                if (instance == null) {
                    instance = new HttpMethod();
                }
            }
        }
        return instance;
    }

    public HttpMethod put(RetrofitFactory retrofitFactory) {
        retrofitFactories.add(retrofitFactory);
        return this;
    }

    public <T> T get(Class<T> tClass) throws Exception {
        if (retrofitFactories.size() > 0) {
            for (RetrofitFactory retrofitFactory : retrofitFactories) {
                return retrofitFactory.getT(tClass);
            }
        } else {
            throw new Exception("please put RetrofitFactory!");
        }
        return null;
    }

    public boolean contains(String defaultOrderDealBaseUrl) {
        for (RetrofitFactory retrofitFactory :
                retrofitFactories) {
            if (defaultOrderDealBaseUrl.equals(retrofitFactory.getBaseUrl())) {
                return true;
            }
        }
        return false;
    }

    //执行
    public static void implement(Observable observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static void implementIo(Observable observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(observer);
    }
}
