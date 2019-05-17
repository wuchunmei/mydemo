package com.wofang.demo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.wofang.demo.mydemo.R;
import com.wofang.demo.base.BaseFragment;
import com.wofang.demo.base.BaseView;
import com.wofang.demo.presenter.EBookFragmentPresenter;
import com.wofang.demo.utils.LogUtil;
import com.wofang.demo.view.ADBannerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Create by wcm
 * on 2019/5/7 0007
 */
public class EBookFragment extends BaseFragment<EBookFragmentPresenter,BaseView> {
    private Unbinder mUnbinder;
    @BindView(R.id.banner)
    ADBannerView mBannerView;
    private List<String> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.ebook_fragment_layout;
    }

    @NonNull
    @Override
    protected EBookFragmentPresenter onCreatePresenter() {
        return new EBookFragmentPresenter();
    }

    @Override
    protected void initViews(@NonNull View view) {
        mUnbinder = ButterKnife.bind(this, view);
        showBanner(mList);
    }

    @Override
    public void onPresenterEvent(int code, @Nullable Bundle bundle) {
        switch (code){
            case EBookFragmentPresenter.DADA_CODE:
                List<String> list = (List<String>) bundle.get(EBookFragmentPresenter.LIST_KEY);
               mList.clear();
               mList.addAll(list);
        }
    }

    private void showBanner(List<String> bannerList){
        if (mBannerView != null) {
            mBannerView.setBannerBeanList(bannerList)
                    .setDefaultImageResId(R.mipmap.icon_img_default)
                    .setIndexPosition(ADBannerView.INDEX_POSITION_BOTTOM)
                    .setIndexColor(getResources().getColor(R.color.colorPrimary))
                    .setIntervalTime(3)
                    .setOnItemClickListener(new ADBannerView.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            LogUtil.d("position = " + position);
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mUnbinder.unbind();
    }
}
