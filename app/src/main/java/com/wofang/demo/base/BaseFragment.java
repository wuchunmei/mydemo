package com.wofang.demo.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Create by wcm
 * on 2019/5/7 0007
 */
public abstract class BaseFragment<P extends BaseFragmentPresenter<V>,V extends BaseView> extends Fragment implements BaseView {
    protected P mPresenter;
    //懒加载判断标识
    private boolean mInitialized;
    protected View mRootView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = onCreatePresenter();
        if(null != mPresenter){
            mPresenter.onPresenterView(getPresenterView());
            mPresenter.onAttach(context);
            getLifecycle().addObserver(mPresenter);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mRootView) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        if(null != mPresenter){
            mPresenter.onCreateView();
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (!mInitialized) {
            initBundleExtra(savedInstanceState);
            initViews(view);
            if (getUserVisibleHint()) {
                onLazyLoad();
            }
            mInitialized = true;
        }
        if(null != mPresenter){
            mPresenter.onViewCreated();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(null != mPresenter){
            mPresenter.onActivityCreated();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(null != mPresenter){
            mPresenter.onDestroyView();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(null != mPresenter){
            mPresenter.onDetach();
            getLifecycle().removeObserver(mPresenter);
            mPresenter = null;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mInitialized) {
            if (getUserVisibleHint()) {
                onLazyLoad();
            }
        }
    }


    /**
     * Override in case of fragment not implementing BasePresenter<View> interface
     */
    @NonNull
    protected V getPresenterView() {
        return (V) this;
    }

    @LayoutRes
    protected abstract int getLayoutId();

    @NonNull
    protected abstract P onCreatePresenter();

    /**
     * 懒加载回调
     */
    protected void onLazyLoad() {
    }

    /**
     * 1.获取get Intent数据 2.状态保存数据读取
     */
    protected void initBundleExtra(Bundle savedInstanceState) {
    }

    /**
     * 初始化UI view
     */
    protected abstract void initViews(@NonNull View view);

}
