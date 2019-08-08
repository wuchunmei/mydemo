package com.wofang.demo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.wofang.demo.R;
import com.wofang.demo.adapter.HomeHouseAdapter;
import com.wofang.demo.recycler.AutoSwipeRefreshLayout;
import com.wofang.demo.recycler.Footer;
import com.wofang.demo.recycler.RecyclerViewScrollListener;
import com.wofang.demo.bean.HouseListResult;
import com.wofang.demo.base.BaseFragment;
import com.wofang.demo.base.BaseView;
import com.wofang.demo.presenter.HomeFragmentPresenter;
import com.wofang.demo.constant.LoadMoreConstant;
import com.wofang.demo.recycler.LoadRecyclerView;
import com.wofang.demo.recycler.MyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Create by wcm
 * on 2019/5/7 0007
 */
public class HomeFragment extends BaseFragment<HomeFragmentPresenter,BaseView>implements RecyclerViewScrollListener.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
    private Unbinder mUnbinder;
    private List<HouseListResult.ListBean> listBeans = new ArrayList<>();
    @BindView(R.id.recycler_view)
    LoadRecyclerView mRecyclerView;
    @BindView(R.id.swipe_container)
    AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private HomeHouseAdapter mAdapter;
    private Footer mFooter;
    private int pageIndex = 1;
    private boolean isAbleLoading = true;

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment_layout;
    }

    @NonNull
    @Override
    protected HomeFragmentPresenter onCreatePresenter() {
        return  new HomeFragmentPresenter(getContext());
    }

    @Override
    protected void initViews(@NonNull View view) {
        mUnbinder = ButterKnife.bind(this, view);
        mAutoSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mAutoSwipeRefreshLayout.setOnRefreshListener(this);
        // 加载数据
        mAutoSwipeRefreshLayout.autoRefresh();
        MyLinearLayoutManager manager = new MyLinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setOnLoadListener(this);
        // 初始化底部数据
        mFooter = new Footer(false, false, getResources().getString(R.string.load_more_before));
    }

    @Override
    public void onPresenterEvent(int code, @Nullable Bundle bundle) {
        switch (code){
            case HomeFragmentPresenter.CODE_HOUSE_LIST:
                if(null != bundle){
                    List<HouseListResult.ListBean> list = (List<HouseListResult.ListBean>) bundle.getSerializable(HomeFragmentPresenter.KEY_HOUSE_LIST);
                    listBeans.clear();
                    listBeans.addAll(list);
                }
                setAdapter();
                break;
                case HomeFragmentPresenter.CODE_LOAD_NO_MORE:
                    isAbleLoading = false;
                    reflashFooterView(LoadMoreConstant.LOAD_MORE_COMPLETE);
                default:
                    break;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mUnbinder.unbind();
    }

    @Override
    public void onLoad() {
        if (listBeans.size() >= LoadMoreConstant.PAGE_SIZE && isAbleLoading ){
            if(null != mPresenter){
                pageIndex = pageIndex + 1;
                mPresenter.getHouseList(String.valueOf(pageIndex));
            }
            mAutoSwipeRefreshLayout.setRefreshing(false);
            reflashFooterView(LoadMoreConstant.LOAD_MORE);
        }else {
            reflashFooterView(LoadMoreConstant.LOAD_MORE_COMPLETE);
        }
    }

    @Override
    public void onRefresh() {
        // 获取最新数据
        isAbleLoading = true;
        if(null != mPresenter){
            pageIndex = 1;
            mPresenter.getHouseList(String.valueOf(pageIndex));
        }
        // 取消加载
        mAutoSwipeRefreshLayout.setRefreshing(false);
        reflashFooterView(LoadMoreConstant.LOAD_MORE_BEFORE);
    }

    // 自定义setAdapter
    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new HomeHouseAdapter(getContext(),listBeans , mFooter);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.reflushList(listBeans);
        }

        // 判断是否已经加载完成
        if (listBeans == null || listBeans.size() <= 0) {
            reflashFooterView(LoadMoreConstant.LOAD_MORE_HIDDEN);
        } else if (listBeans.size() < LoadMoreConstant.PAGE_SIZE || !isAbleLoading) {
            reflashFooterView(LoadMoreConstant.LOAD_MORE_COMPLETE);
        } else {
            reflashFooterView(LoadMoreConstant.LOAD_MORE_BEFORE);
        }
    }

    // 刷新底部
    private void reflashFooterView(int index) {
        // 重构底部数据
        switch (index) {
            case LoadMoreConstant.LOAD_MORE_BEFORE:// 加载前/后
                mRecyclerView.setLoading(false);
                mFooter.setShowProgressBar(false);
                mFooter.setShowFooter(true);
                mFooter.setTitle(getResources().getString(R.string.load_more_before));
                break;
            case LoadMoreConstant.LOAD_MORE:// 加载中
                mRecyclerView.setLoading(false);
                mFooter.setShowProgressBar(true);
                mFooter.setShowFooter(true);
                mFooter.setTitle(getResources().getString(R.string.load_more));
                break;
            case LoadMoreConstant.LOAD_MORE_COMPLETE:// 不允许加载
                mRecyclerView.setLoading(false);
                mFooter.setShowProgressBar(false);
                mFooter.setShowFooter(true);
                mFooter.setTitle(getResources().getString(R.string.load_more_complete));
                break;
            case LoadMoreConstant.LOAD_MORE_HIDDEN:// 隐藏
                mRecyclerView.setLoading(false);
                mFooter.setShowProgressBar(false);
                mFooter.setShowFooter(false);
                mFooter.setTitle(getResources().getString(R.string.load_more_complete));
                break;
        }
        // 刷新底部
        if (mAdapter != null)
            mAdapter.reflushFooterData(mFooter);
    }

}
