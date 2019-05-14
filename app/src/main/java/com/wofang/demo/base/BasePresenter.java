package com.wofang.demo.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Create by wcm
 * on 2019/5/7 0007
 */
public abstract class BasePresenter<V extends BaseView> implements LifecycleObserver {
    //正在加载的状态
    public static final int CODE_LOADING = 1;
    //加载完成的状态
    public static final int CODE_LOAD_SUCCESS = 2;
    //加载失败的状态
    public static final int CODE_LOAD_FAILURE = 3;

    protected V mView;

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public abstract void onCreate();

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        mView = null;
    }

    /**
     * Presenter绑定view
     * @param view
     */
    protected void onPresenterView(V view) {
        mView = view;
    }

    /**
     * Presenter传递数据事件
     * @param code
     * @param bundle
     */
    protected void onPresenterEvent(int code, @Nullable Bundle bundle) {
        if (null != mView) {
            mView.onPresenterEvent(code, bundle);
        }
    }
}
