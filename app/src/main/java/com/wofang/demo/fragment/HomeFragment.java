package com.wofang.demo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.wofang.demo.mydemo.R;
import com.wofang.demo.base.BaseFragment;
import com.wofang.demo.base.BaseView;
import com.wofang.demo.presenter.HomeFragmentPresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Create by wcm
 * on 2019/5/7 0007
 */
public class HomeFragment extends BaseFragment<HomeFragmentPresenter,BaseView> {
    private Unbinder mUnbinder;

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment_layout;
    }

    @NonNull
    @Override
    protected HomeFragmentPresenter onCreatePresenter() {
        return new HomeFragmentPresenter(getContext());
    }

    @Override
    protected void initViews(@NonNull View view) {
        mUnbinder =  ButterKnife.bind(getActivity());
    }

    @Override
    public void onPresenterEvent(int code, @Nullable Bundle bundle) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(null != mUnbinder){
            mUnbinder.unbind();
        }
    }
}
