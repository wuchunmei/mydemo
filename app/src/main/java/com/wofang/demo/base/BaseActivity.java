package com.wofang.demo.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

/**
 * Create by wcm
 * on 2019/5/7 0007
 */
public abstract class BaseActivity<P extends BasePresenter<V>, V extends BaseView> extends AppCompatActivity implements BaseView {
    private static final String TAG = BaseActivity.class.getSimpleName();
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        initBundleExtra(savedInstanceState);
        initViews();
        mPresenter = onCreatePresenter();
        if (null != mPresenter) {
            mPresenter.onPresenterView(getPresenterView());
            getLifecycle().addObserver(mPresenter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mPresenter) {
            getLifecycle().removeObserver(mPresenter);
            mPresenter = null;
        }
    }

    @NonNull
    protected V getPresenterView() {
        return (V) this;
    }

    @NonNull
    protected abstract P onCreatePresenter();

    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 初始化UI view
     */
    protected abstract void initViews();

    /**
     * 1.获取get Intent数据 2.状态保存数据读取
     */
    protected void initBundleExtra(Bundle savedInstanceState) {
    }

}
