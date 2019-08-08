package com.wofang.demo.zxing.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.Result;
import com.wofang.demo.R;
import com.wofang.demo.base.BaseActivity;
import com.wofang.demo.base.BasePresenter;
import com.wofang.demo.zxing.bean.ZxingConfig;
import com.wofang.demo.zxing.camera.CameraManager;
import com.wofang.demo.zxing.decode.DecodeImgCallback;
import com.wofang.demo.zxing.decode.DecodeImgThread;
import com.wofang.demo.zxing.decode.ImageUtil;
import com.wofang.demo.zxing.view.ViewfinderView;

import java.io.IOException;

import cn.simonlee.xcodescanner.core.GraphicDecoder;
import cn.simonlee.xcodescanner.core.ZBarDecoder;


/**
 * @author: wuchunmei
 * @date: 2019/8/5
 * @declare :扫一扫
 */

public class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener,GraphicDecoder.DecodeListener {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    public ZxingConfig config;
    private SurfaceView previewView;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private SurfaceHolder surfaceHolder;
    private ImageView mBackImg;
    private TextView mCameraTv;
    private static final int REQUEST_CODE = 0; // 请求码
    // 请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    // 请求相册
    private static final int REQUEST_PICK = 101;
    private GraphicDecoder mGraphicDecoder;

    private int[] mCodeTypeArray;


    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 保持Activity处于唤醒状态
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.BLACK);
        }
        /*先获取配置信息*/
        try {
            config = (ZxingConfig) getIntent().getExtras().get(Constants.INTENT_ZXING_CONFIG);
        } catch (Exception e) {

            Log.i("config", e.toString());
        }
        if (config == null) {
            config = new ZxingConfig();
        }
        setContentView(R.layout.activity_capture);
        initView();
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        beepManager.setPlayBeep(config.isPlayBeep());
        beepManager.setVibrate(config.isShake());
    }


    private void initView() {
        previewView = findViewById(R.id.preview_view);
        previewView.setOnClickListener(this);
        viewfinderView = findViewById(R.id.viewfinder_view);
        viewfinderView.setZxingConfig(config);
        mBackImg = (ImageView)findViewById(R.id.back);
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mCameraTv = (TextView)findViewById(R.id.camera_tv);
        mCameraTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                /*打开相册*/
                //权限判断
                if (ContextCompat.checkSelfPermission(CaptureActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(CaptureActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到相册
                    gotoPhoto();
                }

            }
        });

    }

    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        // 跳转到调用系统图库*****************打开图库********************
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode){
            case REQUEST_PICK: // 调用系统相册返回
                if (resultCode == RESULT_OK) {
                    zxingDecode(intent);
                }
                break;


        }

    }

    /**
     * zbar解析
     */
    private void zbarDecode(Intent intent){
        if (mGraphicDecoder == null) {
            mGraphicDecoder = new ZBarDecoder(this, getCodeType());//使用带参构造方法可指定条码识别的类型
        } else {
            mGraphicDecoder.setCodeTypes(getCodeType());//指定条码识别的类型
        }
        mGraphicDecoder.decodeForResult(this, intent.getData(), 999);
    }

    private int[] getCodeType() {
        mCodeTypeArray = new int[]{ZBarDecoder.CODABAR, ZBarDecoder.CODE39, ZBarDecoder.CODE93, ZBarDecoder.CODE128, ZBarDecoder.DATABAR, ZBarDecoder.DATABAR_EXP
                , ZBarDecoder.EAN8, ZBarDecoder.EAN13, ZBarDecoder.I25, ZBarDecoder.ISBN10, ZBarDecoder.ISBN13, ZBarDecoder.PDF417, ZBarDecoder.QRCODE
                , ZBarDecoder.UPCA, ZBarDecoder.UPCE};
        return mCodeTypeArray;
    }

    @Override
    public void decodeComplete(String result, int type, int quality, int requestCode) {
        if (result == null) {
            Toast.makeText(CaptureActivity.this, R.string.scan_failed_tip, Toast.LENGTH_SHORT).show();
        } else {
            Log.d("wu","result>>"+ "zbar解析>>" + result);
            Toast.makeText(CaptureActivity.this,"zbar解析"+ result, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * zxing 解析
     * @param intent
     */
    private void zxingDecode(Intent intent){
        Uri uri = intent.getData();
        String cropImagePath = ImageUtil.getImageAbsolutePath(getApplicationContext(), uri);
        new DecodeImgThread(cropImagePath, new DecodeImgCallback() {
            @Override
            public void onImageDecodeSuccess(Result result) {
                handleDecode(result);
            }

            @Override
            public void onImageDecodeFailed() {
//                Toast.makeText(CaptureActivity.this, R.string.scan_failed_tip, Toast.LENGTH_SHORT).show();
                zbarDecode(intent);
            }
        }).run();
    }

    /**
     * @param rawResult 返回的扫描结果
     */
    public void handleDecode(Result rawResult) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        Intent intent = getIntent();
        intent.putExtra(Constants.CODED_CONTENT, rawResult.getText());
        setResult(RESULT_OK, intent);
        this.finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

        cameraManager = new CameraManager(getApplication(), config);
        viewfinderView.setCameraManager(cameraManager);
        handler = null;

        surfaceHolder = previewView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            // 重置callback，等待surfaceCreated()来初始化camera
            surfaceHolder.addCallback(this);
        }

        beepManager.updatePrefs();
        inactivityTimer.onResume();

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            // 打开Camera硬件设备
            cameraManager.openDriver(surfaceHolder);
            // 创建一个handler来打开预览，并抛出一个运行时异常
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("扫一扫");
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    @Override
    protected void onPause() {

        Log.i("CaptureActivity","onPause");
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();

        if (!hasSurface) {

            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        viewfinderView.stopAnimator();
        if (mGraphicDecoder != null) {
            mGraphicDecoder.setDecodeListener(null);
            mGraphicDecoder.detach();
        }
        super.onDestroy();
    }

    @NonNull
    @Override
    protected BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initViews() {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();


    }

    @Override
    public void onPresenterEvent(int code, @Nullable Bundle bundle) {

    }
}
