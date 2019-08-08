package com.wofang.demo.presenter;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import com.wofang.demo.Dialog.CheckNewAppDialog;
import com.wofang.demo.Dialog.ProgressDialog;
import com.wofang.demo.R;
import com.wofang.demo.base.BasePresenter;
import com.wofang.demo.base.BaseView;
import com.wofang.demo.bean.CheckNewVersionResponse;
import com.wofang.demo.network.download.DownLoadRetrofitUtils;
import com.wofang.demo.network.download.DownloadListener;
import com.wofang.demo.network.request.RetrofitWrapper;
import com.wofang.demo.network.response.ResponseResult;
import com.wofang.demo.network.schedulers.SchedulerProvider;
import com.wofang.demo.network.util.DefaultDisposableObserver;
import com.wofang.demo.utils.LogUtil;
import com.wofang.demo.utils.ToastUtils;
import com.wofang.demo.utils.UpdateAPPUtils;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.disposables.Disposable;


/**
 * Create by wcm
 * on 2019/5/7 0007
 */
public class MainPresenter extends BasePresenter<BaseView> {
    private static final String TAG = MainPresenter.class.getSimpleName();
    private Context mContext;
    public static final int CODE_CHECK_UPDATE = 101;
    public static final String KEY_UPDATE_PATH = "key_update_path";
    private ProgressDialog mProgressDialog;

    private List<Disposable> mDisposables = new ArrayList<>();

    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "naliwan.apk";

    public MainPresenter(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {
        //检查版本更新
//        checkUpdate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Disposable disposable : mDisposables) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }
    private void checkUpdate() {
        RetrofitWrapper.getRequest().checkUpdate("0")
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new DefaultDisposableObserver<ResponseResult<CheckNewVersionResponse>>() {
                    @Override
                    public void onNext(ResponseResult<CheckNewVersionResponse> responseResponseResult) {
                        super.onNext(responseResponseResult);
                        CheckNewVersionResponse checkNewVersionResponse = responseResponseResult.getData();
                        if (null != checkNewVersionResponse) {
                            // 更新提示窗
                            if (UpdateAPPUtils.isUpdateVersoin(mContext, checkNewVersionResponse.getLastVersion())) {
                                showUpdateDialog(checkNewVersionResponse);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogUtil.d("mes==" + e.getMessage());
                    }
                });

    }

    private void showUpdateDialog(CheckNewVersionResponse response) {
        CheckNewAppDialog dialog = new CheckNewAppDialog(mContext, R.style.dialogCommonStyle);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setTitle("有新版本");
        dialog.setWarning(response.getDesc(), false);
        dialog.setDialogResult(new CheckNewAppDialog.OnCheckNewAppDialogResult() {
            @Override
            public void finish(boolean bIsConfirmed) {
                if (null != response) {
                    downloadNewVersion(response.getLastVersionUrl());
                }

            }
        });
    }

    /**
     * 下载更新包
     */
    private void downloadNewVersion(String url) {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressNumberVisibility(false);
        downloadAPk(url);
    }

    /**
     * 下载APK
     *
     * @param url
     */
    public void downloadAPk(String url) {
        DownLoadRetrofitUtils downLoadRetrofitUtils = new DownLoadRetrofitUtils(listener);
        downLoadRetrofitUtils.downLoad(url, path);
    }

    private final DownloadListener listener = new DownloadListener() {
        @Override
        public void onStartDownload() {
            ToastUtils.showLongToast("正在下载");
        }

        @Override
        public void onProgress(int progress) {
            mProgressDialog.setProgress(progress);
        }

        @Override
        public void onFinishDownload() {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_UPDATE_PATH,path);
            onPresenterEvent(CODE_CHECK_UPDATE,bundle);
        }


        @Override
        public void onFail(String errorInfo) {
            LogUtil.d("onFail: " + errorInfo);
        }
    };
}
