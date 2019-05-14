package com.wofang.demo.presenter;

import android.content.Context;

import com.wofang.demo.base.BaseFragmentPresenter;
import com.wofang.demo.base.BaseView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Create by wcm
 * on 2019/5/7 0007
 */
public class HomeFragmentPresenter extends BaseFragmentPresenter<BaseView> {

    private List<Disposable> mDisposables = new ArrayList<>();
    private Context mContext;

    public HomeFragmentPresenter(Context context){
        this.mContext = context;
    }

    @Override
    public void onCreate() {
    }

}
