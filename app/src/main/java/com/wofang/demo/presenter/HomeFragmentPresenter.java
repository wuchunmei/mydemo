package com.wofang.demo.presenter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.wofang.demo.base.BaseFragmentPresenter;
import com.wofang.demo.base.BaseView;
import com.wofang.demo.bean.HouseListResult;
import com.wofang.demo.network.request.RetrofitWrapper;
import com.wofang.demo.network.response.ResponseResult;
import com.wofang.demo.network.schedulers.SchedulerProvider;
import com.wofang.demo.network.util.LoadingTransformer;
import com.wofang.demo.utils.LogUtil;
import com.wofang.demo.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by wcm
 * on 2019/5/7 0007
 */
public class HomeFragmentPresenter extends BaseFragmentPresenter<BaseView> {

    private List<Disposable> mDisposables = new ArrayList<>();
    private Context mContext;
    public static final int CODE_HOUSE_LIST = 102;
    public static final int CODE_LOAD_NO_MORE = 103;
    public static final String KEY_HOUSE_LIST  = "key_house_list_key";
    private List<HouseListResult.ListBean> mListBean = new ArrayList<>();

    public HomeFragmentPresenter(Context context){
        this.mContext = context;
    }

    @Override
    public void onCreate() {
        getHouseList("1");
    }

    public void getHouseList(String pageIndex){
        Map<String, String> map = new HashMap<>(16);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", "2");
        map.put("latitude", "20.003863");
        map.put("longitude", "110.344452");
        map.put("distance", "");
        map.put("positionValue", "");
        map.put("ptype", "");
        map.put("sortFlag", "");
        map.put("endCode", "82bacd16504211e99af600e0706116b0");
        map.put("name", "");
        map.put("beginDate", "2019-05-15");
        map.put("endDate", "2019-05-16");
        map.put("bedroom", "");
        map.put("lowPrice", "");
        map.put("highPrice", "");
        map.put("flag", "1");
        RetrofitWrapper.getRequest().getHouseList(map).compose(LoadingTransformer.applyLoading(mContext,"正在加載..."))
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<ResponseResult<HouseListResult>>() {
                    @Override
                    public void accept(ResponseResult<HouseListResult> houseListBeanResponseResult) throws Exception {
                        if(null != houseListBeanResponseResult){
                            HouseListResult houseListBean = houseListBeanResponseResult.getData();
                            if(null != houseListBean ){
                                List<HouseListResult.ListBean> listBeans = houseListBean.getList();
                                if (null != listBeans && listBeans.size() != 0) {
                                    if (Integer.parseInt(pageIndex) == 1) {
                                        mListBean.clear();
                                        mListBean.addAll(listBeans);
                                    } else {
                                        mListBean.addAll(listBeans);
                                    }
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(KEY_HOUSE_LIST, (Serializable) mListBean);
                                    onPresenterEvent(CODE_HOUSE_LIST, bundle);
                                }
                            }else {
                                if(Integer.parseInt(pageIndex) != 1){
                                    onPresenterEvent(CODE_LOAD_NO_MORE,null);
                                }
                            }
                        }else {
                            if(!TextUtils.isEmpty(houseListBeanResponseResult.getMsg())){
                                ToastUtils.showLongToast(houseListBeanResponseResult.getMsg());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showLongToast(throwable.toString());
                    }
                });

    }

}
