package com.wofang.demo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@SuppressWarnings("deprecation")
public class CommonUtil {
	private static float density;
	private static String mYear; // 当前年
	private static String mMonth; // 月
	private static String mDay;
	private static String mWay;

	/**
	 * 显示webview内容
	 * @param webView
	 * @param content
	 */
	public static void getShowWebViewContent(WebView webView, String content) {

		String notice1 = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>"
				+ "<style>img{width: 100%;}</style>" + "</head>" + "<body>" + content + "</body></html>";
		webView.loadDataWithBaseURL("about:blank", notice1, "text/html", "utf-8", null);
		
	}

	
	
	/**
	 * 只显示文字
	 * @param webView
	 * @param content
	 */
	public static void getShowWebViewNoteContent(WebView webView, String content) {
		String notice1="<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /><meta name='viewport' content='width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no'>"+content+"</head></body>";
		webView.loadDataWithBaseURL("about:blank", notice1, "text/html", "utf-8", null);
		webView.loadDataWithBaseURL("about:blank", notice1, "text/html", "utf-8", null);
	}


	/**
	 * 判断当前日期是星期几
	 * 
	 * @param pTime
	 *            设置的需要判断的时间 //格式如2012-09-08
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */
	// String pTime = "2012-03-12";
	public static String getWeek(String pTime) {
		String Week = "星期";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "天";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "六";
		}

		return Week;
	}

	public static String getWeek(String pTime, String Week) {
		// String Week = "星期";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "天";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "六";
		}

		return Week;
	}

	/**
	 * 检测sdcard是否可用
	 * 
	 * @return true为可用，否则为不可用
	 */
	public static boolean sdCardIsAvailable() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return true;
	}

	/**
	 * Checks if there is enough Space on SDCard
	 * 
	 * @param updateSize
	 *            Size to Check
	 * @return True if the Update will fit on SDCard, false if not enough space
	 *         on SDCard Will also return false, if the SDCard is not mounted as
	 *         read/write
	 */
	public static boolean enoughSpaceOnSdCard(long updateSize) {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return (updateSize < getRealSizeOnSdcard());
	}

	/**
	 * get the space is left over on sdcard
	 */
	public static long getRealSizeOnSdcard() {
		File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * Checks if there is enough Space on phone self
	 * 
	 */
	public static boolean enoughSpaceOnPhone(long updateSize) {
		return getRealSizeOnPhone() > updateSize;
	}

	/**
	 * get the space is left over on phone self
	 */
	public static long getRealSizeOnPhone() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		long realSize = blockSize * availableBlocks;
		return realSize;
	}

	/**
	 * 根据手机分辨率从dp转成px
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f) - 15;
	}

	/**
	 * 是否为空值
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isNull(String content) {
		if (content == null) {
			return true;
		}
		if ("".equals(content) || "null".equalsIgnoreCase(content)) {
			return true;
		}
		return false;
	}

	/**
	 * 格式化时间
	 * 
	 * @param oldtime
	 * @return
	 */
	public static String formatTime(long oldtime) {
		long newtime = System.currentTimeMillis() / 1000;
		long time = 0;
		if ((time = ((newtime - oldtime))) < 5) {
			return "刚刚";
		} else if ((time = ((newtime - oldtime))) < 60) {
			return time + "秒前";
		} else if ((time = ((newtime - oldtime))) < 3600) {
			return time / 60 + "分钟前";
		} else if ((time = ((newtime - oldtime))) < 3600 * 24) {
			return time / 3600 + "小时前";
		} else {
			return new SimpleDateFormat("yyyy-MM-dd").format(oldtime * 1000);
		}
	}
	/**
	 * 获取前n天日期、后n天日期
	 *
	 * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
	 * @return
	 */
	public static Date getOldDate(int distanceDay) {
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate = new Date();
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
		Date endDate = null;
		try {
			endDate = dft.parse(dft.format(date.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		LogUtil.d("前7天==" + dft.format(endDate.getTime()));
//		return dft.format(endDate.getTime());
		return endDate;
	}

	/**
	 * * 获取今天往后一周的日期（年-月-日）
	 * 
	 * @return
	 */
	public static List<String> get7date() {
		List<String> dates = new ArrayList<String>();
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		java.text.SimpleDateFormat sim = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String date = sim.format(c.getTime());
		dates.add(date);
		for (int i = 0; i < 6; i++) {
			c.add(java.util.Calendar.DAY_OF_MONTH, 1);
			date = sim.format(c.getTime());
			dates.add(date);

			return dates;
		}
		return dates;
	}

	public static List<String> getSevendate() {

		List<String> dates = new ArrayList<String>();
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

		for (int i = 0; i < 7; i++) {
			mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
			mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
			mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + i);// 获取当前日份的日期号码
			String date = mMonth + "月" + mDay + "日";
			dates.add(date);
		}
		return dates;
	}

	/**
	 * @param time
	 *            时间
	 * @param level
	 *            参考Calendar
	 * @return "yyyy-MM-dd kk:mm:ss" 格式的时间
	 */
	public static String longToTime(long time, int level) {
		// LogUtil.v(String.valueOf(time));
		String format = "yyyy-MM-dd kk:mm:ss";
		switch (level) {
		case Calendar.AM: {
			format = "MM-dd kk:mm";
		}
			break;
		case Calendar.MINUTE: {
			format = "yyyy-MM-dd kk:mm";
		}
			break;
		case Calendar.HOUR: {
			format = "yyyy-MM-dd kk";
		}
			break;
		case Calendar.DATE: {
			format = "yyyy-MM-dd";
		}
			break;
		case Calendar.HOUR_OF_DAY: {
			format = "yyyy-MM-dd HH:mm";
		}

			break;
		case Calendar.DAY_OF_WEEK: {
			format = "MM月dd日";
		}
			break;
		case Calendar.MONTH: {
			format = "yyyy-MM";
		}
			break;
		case Calendar.YEAR: {
			format = "yyyy";
		}
			break;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(time);
	}

	public static String getTime(String timeString, String Format) {
		String timeStamp = null;
		SimpleDateFormat sdf = new SimpleDateFormat(Format);
		Date d;
		try {
			d = sdf.parse(timeString);
			long l = d.getTime();
			timeStamp = String.valueOf(l);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timeStamp;
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param context
	 * @param view
	 */
	public static void hideInputMethod(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/**
	 * 判断本地是否安装了该包
	 * 
	 * @param context
	 * @param apkid
	 * @param @return
	 * @return boolean
	 */
	public static boolean isClientInstalled(Context context, String apkid) {
		if (apkid != null) {
			String lowAppLabel = apkid.trim().toLowerCase();
			if (lowAppLabel.length() > 0) {
				PackageManager pm = context.getPackageManager();
				List<PackageInfo> packs = pm.getInstalledPackages(0);
				for (PackageInfo pack : packs) {
					String appLabel = pack.applicationInfo.packageName.toLowerCase();
					if (appLabel.equals(lowAppLabel)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 打开本地apk
	 * 
	 * @param context
	 * @param apkid
	 * @param iscount
	 *            是否需要统计
	 * @return void
	 */
	public static void launchClient(Context context, String id, String apkid, boolean iscount) {
		if (apkid != null && apkid.length() > 0) {
			PackageManager pm = context.getPackageManager();
			Intent intent = pm.getLaunchIntentForPackage(apkid);
			if (intent != null) {
				context.startActivity(intent);
			}
		}
	}

	/**
	 * 显示金币
	 * 
	 * @param gold
	 * @return
	 */
	public static String showGold(int gold) {
		String strGold = String.valueOf(gold);
		if (gold >= 10000) {
			strGold = (gold / 10000d) + "万";
		}
		return strGold;
	}

	/**
	 * 显示剩余时间
	 * 
	 * @param time
	 * @return
	 */
	public static String showSurplustime(int time) {
		String strSurp = "还剩" + "5分钟";
		if (time < 10) {
			strSurp = "开奖中，暂停竞猜";
		} else if (time < 60) {
			strSurp = "还剩" + time + "秒";
		} else {
			strSurp = "还剩" + ((int) (time / 60)) + "分钟" + ((int) time % 60) + "秒";
		}
		return strSurp;
	}

	/**
	 * 将文本设置到剪切板中
	 * 
	 * @param context
	 * @param str
	 * @time 2011-6-27 下午02:53:41
	 * @author:linyg
	 */
	public static boolean setClipboard(Context context, String str) {
		try {
			ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
			clip.setText(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 从剪切板中读取文本
	 * 
	 * @param context
	 * @return
	 * @time 2011-6-27 下午02:53:56
	 * @author:linyg
	 */
	public static String getClipboard(Context context) {
		ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		return clip.getText().toString();
	}

	/**
	 * 将分享到新浪微博的配图，复制到Sdcard
	 * 
	 * @return 配图在Sdcard的路径
	 */
	public static String copyImgToSdcard(Context context) {
		File outFile = new File(new File(getSDPath()), "shengqian_weibo_share.jpg");
		if (!outFile.exists()) {

			InputStream in = null;
			OutputStream out = null;
			try {
				in = context.getAssets().open("shengqian_weibo_share.jpg");
				out = new FileOutputStream(outFile);

				byte[] buf = new byte[4 * 1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null)
						in.close();
					if (out != null)
						out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return outFile.getAbsolutePath();
	}

	/**
	 * 获取sd卡路径
	 * 
	 * @return
	 */
	public static String getSDPath() {
		String sdDir = "";
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory() + "/shengqian/";// 获取跟目录
		} else {
			sdDir = "/data/data/com.mile.zhuanqian/zhuanqian/";
		}
		File file = new File(sdDir);
		if (!file.exists()) {
			file.mkdir();
		}
		return sdDir;
	}

	/**
	 * 将数据写入到sd卡中
	 * 
	 * @param text
	 *            要保存的字符
	 */
	public static void writeToSd(String filename, String text) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File sdCardDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);// 获取SDCard目录
			File sdFile = new File(sdCardDir, filename);
			try {
				FileOutputStream fos = new FileOutputStream(sdFile);
				fos.write(text.getBytes());
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 检测SIM卡是否存在
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkSim(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			int status = tm.getSimState();
			if ((status == TelephonyManager.SIM_STATE_UNKNOWN) || (status == TelephonyManager.SIM_STATE_ABSENT)) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/** dip转px */
	public static int dipToPx(Context context, int dip) {
		if (density <= 0) {
			density = context.getResources().getDisplayMetrics().density;
		}
		return (int) (dip * density + 0.5f);
	}

	/** px转dip */
	public static int pxToDip(Context context, int px) {
		if (density <= 0) {
			density = context.getResources().getDisplayMetrics().density;
		}
		return (int) ((px - 0.5f) / density);
	}

	/**
	 * 调用拨号界面
	 * 
	 * @param phone
	 *            电话号码
	 */
	public static void call(Context context, String phone) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 获取客户端信息
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppInfo(Context context) {
		String appInfo = "";
		try {
			PackageManager pm = context.getPackageManager();
			List<PackageInfo> packs = pm.getInstalledPackages(0);
			for (PackageInfo pack : packs) {
				if ((pack.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					String appLabel = pack.applicationInfo.packageName;
					String name = pack.applicationInfo.loadLabel(pm).toString();
					;
					appInfo += appLabel + ";" + name + "@";
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return appInfo;
	}
	
	/**
	 * 价格计算*减法
	 * @param minuend 被减数
	 * @param subtrahend 减数
	 * @return float
	 */
	public static Float priceSub(String minuend, String subtrahend){
		BigDecimal b1 = new BigDecimal(minuend);
		BigDecimal b2 = new BigDecimal(subtrahend);
		
		return b1.subtract(b2).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * 保留小数点位数
	 * 
	 * @param decimal
	 * @param format
	 *            "0.00"
	 * @return
	 */
	public static String getDecimalFormat(String decimal, String format) {
		String dstr = "";
		if (TextUtils.isEmpty(decimal)) {
			return dstr;
		}
		if (TextUtils.isEmpty(format)) {
			format = "0";
		}

		int index1 = decimal.lastIndexOf(".");// 寻找小数点的索引位置，若不是小数，则为-1
		int index2 = format.lastIndexOf("."); // 寻找小数点的索引位置，若不是小数，则为-1
		int length1 = 0;
		int length2 = 0;
		if (index1 > -1) {
			length1 = decimal.substring(index1 + 1).length();// 取得小数点后的数值，不包括小数点
			if (index2 > -1) {
				length2 = format.substring(index2 + 1).length();// 取得小数点后的数值，不包括小数点
			}
		} else {
			return decimal;
		}
		if (length2 > 0 && length1 < length2) {
			return decimal;
		} else {
			DecimalFormat decimalFormat = new DecimalFormat(format);// 构造方法的字符格式这里如果小数不足2位,会以0补足.
			dstr = decimalFormat.format(Float.parseFloat(decimal));
		}

		return dstr;
	}
}
