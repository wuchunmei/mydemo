package com.wofang.demo.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.wofang.demo.Dialog.MyCustomDialog;
import com.wofang.demo.R;
import com.wofang.demo.base.BaseActivity;
import com.wofang.demo.base.BaseView;
import com.wofang.demo.constant.PermissionCode;
import com.wofang.demo.fragment.EBookFragment;
import com.wofang.demo.fragment.HomeFragment;
import com.wofang.demo.fragment.MyFragment;

import com.wofang.demo.presenter.MainPresenter;
import com.wofang.demo.utils.LogUtil;
import com.wofang.demo.utils.ToastUtils;
import com.wofang.demo.utils.UpdateAPPUtils;
import com.wofang.demo.view.CustomViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity<MainPresenter, BaseView> {
    @BindView(R.id.main_view_pager)
    CustomViewPager mCustomViewPager;
    @BindView(R.id.tabs_rg)
    RadioGroup mRadioGroup;

    private Unbinder mUnbinder;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private static final int INSTALL_PERMISS_CODE = 1000; // 未知来源权限申请请求码
    private File mFile;


    @NonNull
    @Override
    protected MainPresenter onCreatePresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        mUnbinder = ButterKnife.bind(this);
        initFragment();
    }

    @Override
    protected void initBundleExtra(Bundle savedInstanceState) {
        super.initBundleExtra(savedInstanceState);
        /**
         * 以下两种方式都可以做android6.0以上的权限检查，checkPermissions()采用的是自己写的系统调用方式
         * useCodeUtilCheckPermission()采用的是用第三方库utilcode封装好的方式调用
         * 注意：不管采用哪种方式，都需要在AndroidManifest.xml中申请
         */
//        checkPermissions();
//        useCodeUtilCheckPermission();
    }

    private void useCodeUtilCheckPermission(){
        if(PermissionUtils.isGranted(PermissionConstants.CALENDAR)){
            LogUtil.d("已经有权限了");
        }else {
            PermissionUtils.permission(PermissionConstants.CALENDAR).rationale(new PermissionUtils.OnRationaleListener() {
                @Override
                public void rationale(ShouldRequest shouldRequest) {
                    shouldRequest.again(true);
                }
            }).callback(new PermissionUtils.FullCallback() {
                @Override
                public void onGranted(List<String> permissionsGranted) {
                    LogUtil.d("允许后的回调");
                }

                @Override
                public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                    LogUtil.d("permissionsDeniedForever》》" + permissionsDeniedForever  +  "    " + "permissionsDenied>>" + permissionsDenied);
                }
            }).request();
        }
    }


    /**
     * 检查权限
     */
    private void checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /**
             * 这个方法用来监测某个权限是否被配置,两个参数分别为：当前上下文，需要监测的权限信息,这个方法的返回值有两个0或-1 分别是已授权，和未授权
             */
            if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                /**
                 * 如果没有所需的权限。将会弹窗提醒用户配置权限,三个参数分别：当前上下文，需要申请的权限列表列表，请求码。请求码用于后面回调返回请求结果用
                 */
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionCode.ACCESS_LOCATION);
            }else {
                LogUtil.d("已经申请过权限");
            }
        }else {
             LogUtil.d("6.0以下不需要权限");
        }
    }

    /**
     * 这个方法会在用户配置完权限后回调
     * @param requestCode 请求码
     * @param permissions 请求的权限
     * @param grantResults 权限当前状态
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PermissionCode.ACCESS_LOCATION:{
                LogUtil.d("权限允许后的回调");
            }
        }
    }

    /**
     * 初始化页面
     */
    private void initFragment(){
        fragmentList.add(new HomeFragment());
        fragmentList.add(new EBookFragment());
        fragmentList.add(new MyFragment());
        // init view pager
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mCustomViewPager.setAdapter(mAdapter);
        // register listener
        mCustomViewPager.addOnPageChangeListener(mPageChangeListener);
        mCustomViewPager.setAdapter(mAdapter);
        mCustomViewPager.setScanScroll(false);
        mCustomViewPager.setOffscreenPageLimit(fragmentList.size());
        mRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) mRadioGroup.getChildAt(position);
            radioButton.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    mCustomViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    };

    @Override
    public void onPresenterEvent(int code, @Nullable Bundle bundle) {
        switch (code) {
            case MainPresenter.CODE_CHECK_UPDATE:
                String path = bundle.getString(MainPresenter.KEY_UPDATE_PATH);
                File file = new File(path);
                mFile = file;
                showDialog(file);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 接受权限配置结果
        // 拒绝时，没有获取到主要权限，无法运行，关闭页面
        if (requestCode == INSTALL_PERMISS_CODE) {
            // 未知来源权限申请
            UpdateAPPUtils.installAPK(this, mFile);
        }
    }

    /**
     * 安装提示框
     *
     * @param file
     */
    private void showDialog(File file) {
        Dialog mydialog = MyCustomDialog.CustomDialogOk(this, "安装包已下载", "", "立即安装", new MyCustomDialog.OnOkListener() {
            @Override
            public void onDismiss() {

            }

            @Override
            public void onOk() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    installApkForAndroid_0(file);
                } else {
                    UpdateAPPUtils.installAPK(MainActivity.this, file);
                }
            }
        });
        mydialog.setCanceledOnTouchOutside(false);
        mydialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 适配android_8.0 安装apk
     */
    private void installApkForAndroid_0(File file) {
        //先获取是否有安装未知来源应用的权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!getPackageManager().canRequestPackageInstalls()) {
                //权限没有打开则提示用户去手动打开
                MyCustomDialog.CustomDialogOkCancel(this, "需要打开允许来自此来源，请去设置中开启此权限", "", "取消", "去开启",
                        new MyCustomDialog.OnOkCancelListener() {
                            @Override
                            public void onOk() {
                                ToastUtils.showLongToast("开启未知来源安装取消，安装失败！");
                            }

                            @Override
                            public void onCancel() {
                                Uri packageURI = Uri.parse("package:" + getPackageName());
                                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                                startActivityForResult(intent, INSTALL_PERMISS_CODE);
                            }

                            @Override
                            public void onDismiss() {
                            }
                        });
            } else {
                UpdateAPPUtils.installAPK(this, file);
            }
        }
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return this.mList == null ? null : this.mList.get(position);
        }

        @Override
        public int getCount() {
            return this.mList == null ? 0 : this.mList.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCustomViewPager.removeOnPageChangeListener(mPageChangeListener);
        if (null != mUnbinder) {
            mUnbinder.unbind();
        }
    }
}
