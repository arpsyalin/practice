package com.lyl.ifw.netexample;

import android.text.TextUtils;

import com.lyl.ifw.netframeapi.ICache;
import com.lyl.ifw.netframeapi.IDataAnalysis;
import com.lyl.ifw.netframeapi.IDealFactory;
import com.lyl.ifw.netframeapi.INetwork;
import com.lyl.ifw.netframeapi.INetworkCallBack;
import com.lyl.ifw.netframeapi.IRequestAop;
import com.lyl.ifw.netframeapi.IRequestCallBack;
import com.lyl.ifw.constant.ResponseCode;

import java.util.HashMap;

/**
 * * @Description 处理工厂例子
 * * @Author 刘亚林
 * * @CreateDate 2020/10/29
 * * @Version 1.0
 * * @Remark TODO
 **/
public class DealFactory implements IDealFactory {
    private static volatile DealFactory instance;
    //缓存
    private ICache mCache;
    //网络请求框架
    private INetwork mNetwork;
    //请求的切面处理
    private IRequestAop mRequestAop;
    //请求默认参数构建
    private RequestMapBuilder mRequestMapBuilder;
    //解析框架
    private IDataAnalysis mIDataAnalysis;
    HashMap<Long, IRequestCallBack> requestCallBack = new HashMap<>();

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

    /**
     * 传入缓存处理框架
     */
    @Override
    public IDealFactory cache(ICache iCache) {
        this.mCache = iCache;
        return this;
    }

    /**
     * 默认的网络请求框架
     *
     * @param network
     * @return
     */
    @Override
    public DealFactory network(INetwork network) {
        mNetwork = network;
        return this;
    }

    /**
     * 默认的切面
     *
     * @param requestAop
     * @return
     */
    @Override
    public DealFactory defaultAop(IRequestAop requestAop) {
        mRequestAop = requestAop;
        return this;
    }

    /**
     * 默认的解析框架
     *
     * @param iDataAnalysis
     * @return
     */
    @Override
    public DealFactory defaultAnalysis(IDataAnalysis iDataAnalysis) {
        mIDataAnalysis = iDataAnalysis;
        return this;
    }

    /**
     * 传入默认的请求URL
     *
     * @param baseUrl
     * @return
     */
    public DealFactory baseUrl(String baseUrl) {
        if (mRequestMapBuilder == null) clone();
        mRequestMapBuilder.baseUrl = baseUrl;
        return this;
    }

    /**
     * 添加默认的请求头KEY
     *
     * @param key
     * @param value
     * @return todo 比较适用如：在登录后添加必要的HEAD
     */
    public DealFactory addHead(String key, String value) {
        if (mRequestMapBuilder == null) clone();
        mRequestMapBuilder.head.put(key, value);
        return this;
    }

    /**
     * 移除默认的请求的HEAD KEY
     *
     * @param key
     * @return
     */
    public DealFactory removeHead(String key) {
        if (mRequestMapBuilder == null) clone();
        mRequestMapBuilder.head.remove(key);
        return this;
    }

    /**
     * 克隆默认的请求参数
     */
    @Override
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

    @Override
    public void request(final RequestMapBuilder requestMapBuilder, final IRequestCallBack view, final IRequestAop requestAop) {
        if (requestMapBuilder == null) {
            throw new RuntimeException("no requestMapBuilder");
        } else {
            if (TextUtils.isEmpty(requestMapBuilder.baseUrl) && TextUtils.isEmpty(requestMapBuilder.actionUrl)) {
                throw new RuntimeException("no Url");
            }
        }
        if (!requestCallBack.containsKey(requestMapBuilder.requestId)) {
            requestCallBack.put(requestMapBuilder.requestId, view);
        } else {
            return;
        }
        onStart(requestMapBuilder, view);
        String url = requestMapBuilder.getRequestUrl();
        if (TextUtils.isEmpty(url)) {
            view.notifyResult(ResponseCode.FAIL, requestMapBuilder, null);
            onEnd(requestMapBuilder, view);
            return;
        }
        if (mCache != null) {
            Object cache1 = mCache.getCache(requestMapBuilder);
            if (cache1 != null) {
                view.notifyResult(ResponseCode.SUCCESS, requestMapBuilder, cache1);
                onEnd(requestMapBuilder, view);
                return;
            }
        }

        if (mNetwork != null) {
            mNetwork.request(requestMapBuilder, mINetworkCallBack);
        }
    }

    @Override
    public void cancel(RequestMapBuilder requestMapBuilder, IRequestCallBack view, IRequestAop requestAop) {

    }

    @Override
    public void cancel(RequestMapBuilder requestMapBuilder, IRequestCallBack view) {
        cancel(requestMapBuilder, view, mRequestAop);
    }

    public void request(RequestMapBuilder requestMapBuilder, IRequestCallBack iRequestCallBack) {
        request(requestMapBuilder, iRequestCallBack, mRequestAop);
    }

    void onStart(RequestMapBuilder requestMapBuilder, IRequestCallBack iRequestAop) {
        if (requestMapBuilder.needAop) {
            if (mRequestAop != null) {
                mRequestAop.onStart(requestMapBuilder);
            }
        }
        if (iRequestAop != null) {
            iRequestAop.onStart(requestMapBuilder);
        }
    }

    void onEnd(RequestMapBuilder requestMapBuilder, IRequestCallBack iRequestAop) {
        if (requestMapBuilder.needAop) {
            if (mRequestAop != null) {
                mRequestAop.onEnd(requestMapBuilder);
            }

        }
        if (iRequestAop != null) {
            iRequestAop.onEnd(requestMapBuilder);
        }
    }

    INetworkCallBack mINetworkCallBack = new INetworkCallBack() {
        @Override
        public void netCallBack(int fail, RequestMapBuilder requestMapBuilder, String o) {
            IRequestCallBack view = requestCallBack.get(requestMapBuilder.requestId);
            if (mIDataAnalysis == null) {
                view.notifyResult(fail, requestMapBuilder, o);
            } else {
                Object d = mIDataAnalysis.analysis(requestMapBuilder, o);
                if (mCache != null) {
                    mCache.saveCache(requestMapBuilder, d);
                }
                view.notifyResult(fail, requestMapBuilder, d);
            }

            onEnd(requestMapBuilder, view);
            requestCallBack.remove(requestMapBuilder.requestId);
        }

        @Override
        public void netUserCancel(RequestMapBuilder requestMapBuilder) {

        }
    };

}
