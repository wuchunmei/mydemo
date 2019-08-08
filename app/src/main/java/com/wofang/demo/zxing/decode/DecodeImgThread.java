package com.wofang.demo.zxing.decode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.Hashtable;


public class DecodeImgThread extends Thread {


    /*图片路径*/
    private String imgPath;
    /*回调*/
    private DecodeImgCallback callback;


    public DecodeImgThread(String imgPath, DecodeImgCallback callback) {

        this.imgPath = imgPath;
        this.callback = callback;
    }


    @Override
    public void run() {
        super.run();
        if (TextUtils.isEmpty(imgPath) || callback == null) {
            return;
        }
        Result result = scanningImage(imgPath);
        if (result != null) {
            callback.onImageDecodeSuccess(result);
        } else {
            callback.onImageDecodeFailed();
        }
    }

    /**
     * 第二种方式扫描
     *
     * @param path
     * @return
     */
    protected Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;

        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;

        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        byte[] data = getYUV420sp(scanBitmap.getWidth(), scanBitmap.getHeight(), scanBitmap);

        Hashtable<DecodeHintType, Object> hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8"); // 设置二维码内容的编码
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);
        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data,
                scanBitmap.getWidth(),
                scanBitmap.getHeight(),
                0, 0,
                scanBitmap.getWidth(),
                scanBitmap.getHeight(),
                false);

        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader2 = new QRCodeReader();
        Result result = null;
        try {
            result = reader2.decode(bitmap1, hints);
            Log.e("wu", result.getText());
        } catch (NotFoundException e) {
            Log.e("wu", "NotFoundException");
        } catch (ChecksumException e) {
            Log.e("wu", "ChecksumException");
        } catch (FormatException e) {
            Log.e("wu", "FormatException");
        }
        return result;

    }


    public byte[] getYUV420sp(int inputWidth, int inputHeight,
                              Bitmap scaled) {
        int[] argb = new int[inputWidth * inputHeight];

        scaled.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight);

        byte[] yuv = new byte[inputWidth * inputHeight * 3 / 2];

        encodeYUV420SP(yuv, argb, inputWidth, inputHeight);

        scaled.recycle();

        return yuv;
    }

    private void encodeYUV420SP(byte[] yuv420sp, int[] argb, int width, int height) {
        // 帧图片的像素大小
        final int frameSize = width * height;
        // Y的index从0开始
        int yIndex = 0;
        // UV的index从frameSize开始
        int uvIndex = frameSize;
        // YUV数据, ARGB数据
        int Y, U, V, a, R, G, B;
        ;
        int argbIndex = 0;
        // ---循环所有像素点，RGB转YUV---
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {

                // a is not used obviously
                a = (argb[argbIndex] & 0xff000000) >> 24;
                R = (argb[argbIndex] & 0xff0000) >> 16;
                G = (argb[argbIndex] & 0xff00) >> 8;
                B = (argb[argbIndex] & 0xff);
                argbIndex++;

                // well known RGB to YUV algorithm
                Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
                U = ((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128;
                V = ((112 * R - 94 * G - 18 * B + 128) >> 8) + 128;

                Y = Math.max(0, Math.min(Y, 255));
                U = Math.max(0, Math.min(U, 255));
                V = Math.max(0, Math.min(V, 255));

                // NV21 has a plane of Y and interleaved planes of VU each
                // sampled by a factor of 2
                // meaning for every 4 Y pixels there are 1 V and 1 U. Note the
                // sampling is every other
                // pixel AND every other scanline.
                // ---Y---
                yuv420sp[yIndex++] = (byte) Y;
                // ---UV---
//                if ((j % 2 == 0) && (i % 2 == 0)) {
//                    yuv420sp[uvIndex++] = (byte) V;
//                    yuv420sp[uvIndex++] = (byte) U;
//                }
            }
        }
    }





    /**
     * 生成一张空白的图片
     * @param width
     * @param height
     * @return
     */

    public static Bitmap createWhiteBitMap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.parseColor("#ffffff"));//填充颜色
        return bitmap;
    }


    /**
     * 根据路径获取图片
     *
     * @param filePath  文件路径
     * @param maxWidth  图片最大宽度
     * @param maxHeight 图片最大高度
     * @return bitmap
     */
    private static Bitmap getBitmap(final String filePath, final int maxWidth, final int maxHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 将两个Bitmap合并成一个
     *
     * @param first
     * @param second
     * @param fromPoint 第二个Bitmap开始绘制的起始位置（相对于第一个Bitmap）
     * @return
     */
    public static Bitmap mixtureBitmap(Bitmap first, Bitmap second,
                                       PointF fromPoint) {
        if (first == null || second == null || fromPoint == null) {
            return null;
        }
        Bitmap newBitmap = Bitmap.createBitmap(
                first.getWidth(),
                first.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, 0, 0, null);
        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();

        return newBitmap;
    }



    /**
     * Return the sample size.
     *
     * @param options   The options.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return the sample size
     */
    private static int calculateInSampleSize(final BitmapFactory.Options options,
                                             final int maxWidth,
                                             final int maxHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while ((width >>= 1) >= maxWidth && (height >>= 1) >= maxHeight) {
            inSampleSize <<= 1;
        }
        return inSampleSize;
    }


    //    private void scanImage(String imgPath){
//        Bitmap scanBitmap = getBitmap(imgPath, 400, 400);
//        if(scanBitmap == null){
//            Log.d("wu","scanBitmap>>"+scanBitmap);
//            return;
//        }
//        Bitmap bitmap1 = mixtureBitmap(createWhiteBitMap(400,400),scanBitmap,new PointF(0,0));
//        MultiFormatReader multiFormatReader = new MultiFormatReader();
//        // 解码的参数
//        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
//        // 可以解析的编码类型
//        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
//
//
//        // 扫描的类型  一维码和二维码
//        decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
//        decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
//        decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
//        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
//        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
//        //复杂模式，开启PURE_BARCODE模式
//        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
//        // 设置解析的字符编码格式为UTF8
//        hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
//        // 设置解析配置参数
//        multiFormatReader.setHints(hints);
//        // 开始对图像资源解码
//        Result rawResult = null;
//        try {
//            rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(bitmap1))));
//
//            Log.i("解析结果", rawResult.getText());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            //  Log.i("解析的图片结果","失败");
//        }
//
//        if (rawResult != null) {
//            callback.onImageDecodeSuccess(rawResult);
//        } else {
//            callback.onImageDecodeFailed();
//        }
//    }



    }
