package com.wofang.demo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;


import com.wofang.demo.R;
import com.wofang.demo.base.BaseFragment;
import com.wofang.demo.base.BaseView;
import com.wofang.demo.presenter.MyFragmentPresenter;

/**
 * Create by wcm
 * on 2019/5/7 0007
 */
public class MyFragment extends BaseFragment<MyFragmentPresenter,BaseView> {
    @Override
    protected int getLayoutId() {
        return R.layout.my_fragment_layout;
    }

    @NonNull
    @Override
    protected MyFragmentPresenter onCreatePresenter() {
        return new MyFragmentPresenter();
    }

    @Override
    protected void initViews(@NonNull View view) {

    }

    @Override
    public void onPresenterEvent(int code, @Nullable Bundle bundle) {

    }
}
