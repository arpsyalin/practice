package com.lyl.ifw;

import android.text.TextUtils;

/**
 * * @Description 处理工厂
 * * @Author 刘亚林
 * * @CreateDate 2020/10/29
 * * @Version 1.0
 * * @Remark TODO
 **/
public class DealFactory implements IDealFactory {
    //一级缓存
    private ICache mCache;
    //网络请求框架
    private INetwork mNetwork;
    //请求的切面处理
    private IRequestAop mRequestAop;
    //请求参数构建
    private RequestMapBuilder mRequestMapBuilder;
    private static volatile DealFactory instance;
    private IJsonAnalysis mIJsonAnalysis;

    private DealFactory() {
        if (instance != null) {
            throw new RuntimeException("can't newInstance!");
        }
    }

    public static DealFactory getInstance() {
        if (instance == null) {
            synchronized (DealFactory.class) {
                if (instance == null) {
                    instance = new DealFactory();
                }
            }
        }
        return instance;
    }

    //初始化传入1级缓存
    public IDealFactory cache(ICache iCache) {
        this.mCache = iCache;
        return this;
    }


    public DealFactory baseUrl(String baseUrl) {
        if (mRequestMapBuilder == null) clone();
        mRequestMapBuilder.baseUrl = baseUrl;
        return this;
    }

    public DealFactory network(INetwork network) {
        mNetwork = network;
        return this;
    }

    public DealFactory rAop(IRequestAop requestAop) {
        mRequestAop = requestAop;
        return this;
    }

    public DealFactory jsonAnalysis(IJsonAnalysis iJsonAnalysis) {
        mIJsonAnalysis = iJsonAnalysis;
        return this;
    }

    public DealFactory addHead(String key, String value) {
        if (mRequestMapBuilder == null) clone();
        mRequestMapBuilder.head.put(key, value);
        return this;
    }

    public DealFactory removeHead(String key) {
        if (mRequestMapBuilder == null) clone();
        mRequestMapBuilder.head.remove(key);
        return this;
    }

    public RequestMapBuilder clone() {
        if (mRequestMapBuilder != null) {
            try {
                return (RequestMapBuilder) mRequestMapBuilder.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        RequestMapBuilder requestMap = RequestMapBuilder.build();
        mRequestMapBuilder = requestMap;
        return mRequestMapBuilder;
    }

    public <T> void request(final IView view, final RequestMapBuilder requestMapBuilder, final IRequestAop requestAop, final Class<T> callBackClass) {
        if (requestMapBuilder == null) {
            throw new RuntimeException("no requestMapBuilder");
        } else {
            if (TextUtils.isEmpty(requestMapBuilder.baseUrl) && TextUtils.isEmpty(requestMapBuilder.actionUrl)) {
                throw new RuntimeException("no Url");
            }
        }
        String url = requestMapBuilder.getRequestUrl();
        if (TextUtils.isEmpty(url)) {
            view.notifyResult(ResponseCode.FAIL, requestMapBuilder, null);
            onEnd(requestMapBuilder, requestAop);
            return;
        }
        onStart(requestMapBuilder, requestAop);
        if (mCache != null) {
            T cache1 = mCache.getCache(requestMapBuilder, callBackClass);
            if (cache1 != null) {
                view.notifyResult(ResponseCode.SUCCESS, requestMapBuilder, cache1);
                onEnd(requestMapBuilder, requestAop);
                return;
            }
        }

        if (mNetwork != null) {
            mNetwork.request(new INetworkCallBack() {
                @Override
                public void netCallBack(int code, RequestMapBuilder mapBuilder, String o) {
                    if (mIJsonAnalysis == null) {
                        view.notifyResult(code, mapBuilder, o);
                    } else {
                        T d = mIJsonAnalysis.toJson(o, callBackClass);
                        if (mCache != null) {
                            mCache.saveCache(mapBuilder, d, callBackClass);
                        }
                        view.notifyResult(code, mapBuilder, d);
                    }

                    onEnd(requestMapBuilder,requestAop);
                }
            }, requestMapBuilder);
        }
    }

    public <T> void request(IView iView, RequestMapBuilder requestMapBuilder, Class<T> callBackClass) {
        request(iView, requestMapBuilder, mRequestAop, callBackClass);
    }

    void onStart(RequestMapBuilder requestMapBuilder, IRequestAop iRequestAop) {
        if (requestMapBuilder.needAop) {
            if (iRequestAop != null) {
                iRequestAop.onStart();
            }
        }
    }

    void onEnd(RequestMapBuilder requestMapBuilder, IRequestAop iRequestAop) {
        if (requestMapBuilder.needAop) {
            if (iRequestAop != null) {
                iRequestAop.onEnd();
            }
        }
    }
}
