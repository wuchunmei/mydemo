package com.wofang.demo.base;

import android.content.Context;

/**
 * Create by wcm
 * on 2019/5/7 0007
 */
public abstract class BaseFragmentPresenter<V extends BaseView> extends BasePresenter {
    public void onAttach(Context context) {}
    public void onCreateView() {}
    public void onViewCreated() {}
    public void onActivityCreated() {}
    public void onDestroyView() {}
    public void onDetach() {}
}
