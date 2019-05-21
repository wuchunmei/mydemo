package com.wofang.demo.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期操作函数
 *
 */
public class DateUtils {
	private static final String dateFormat = "yyyy-MM-dd HH:mm";
	private static final String fulldateFormat = "yyyy-MM-dd HH:mm:ss";
	private static final String dateFormat0 = "yy-MM-dd HH:mm";
	private static final String dateFormat1 = "yyyyMMddHHmm";
	private static final String dateFormat3 = "yyyy-MM-dd";
	private static final String dateFormat4 = "yyyy.MM.dd";
	private static final String dateFormat5 = "yyyy/MM/dd HH:mm";
	private static final String dayFormat = "yyyyMMdd";
	private static final String fullTimeStampFormat = "yyyyMMddHHmmss";

	private static final String sdf = "MM-dd HH:mm";
	private static final String hour_minite_sdf = "HH:mm";

	//消息对话列表显示时间格式
	private static final String messageDateFormat = "M月d日 aHH:mm";

	public static final long DAY_MINUTE=86400000; //一天的毫秒数
	/**
	 * FORMAT_01 20120331
	 */
	public static String FORMAT_01 = "yyyyMMdd";

	public static String FORMAT_02 = "yyyy-MM-dd";
	/**
	 *转换为yy-MM-dd HH:mm的格式
	 *@param date
	 *@return
	 */
	public static String convertDateToString0(Date date){
		return date2String(new SimpleDateFormat(dateFormat0),date);
	}

	/**
	 * 转换为yyyy-MM-dd HH:mm的格式
	 *
	 * @param date
	 * @return
	 */
	public static String convertDateToString(Date date) {
		return date2String(new SimpleDateFormat(dateFormat), date);
	}

	/**
	 * 转换为yyyy-MM-dd HH:mm:ss的格式
	 *
	 * @param date
	 * @return
	 */
	public static String convertDateToFullString(Date date) {
		return date2String(new SimpleDateFormat(fulldateFormat), date);
	}

	public static String getNow() {
		return convertDateToString(new Date());
	}

	public static String getDayValue() {
		return date2String(new SimpleDateFormat(dayFormat), new Date());
	}

    public static String convertDateToString5(Date date) {
        return date2String(new SimpleDateFormat(dateFormat5), date);
    }

    /**
	 * 转换为yyyy-MM-dd的格式
	 *
	 * @param date
	 * @return
	 */
    public static String convertDateToString3(Date date) {
        return date2String(new SimpleDateFormat(dateFormat3), date);
    }

    /**
	 * 转换为yyyy.MM.dd的格式
	 *
	 * @param date
	 * @return
	 */
    public static String convertDateToString4(Date date) {
        return date2String(new SimpleDateFormat(dateFormat4), date);
    }
    /**
     *MM-dd HH:mm
     *@param date
     *@return
     */
    public static String convertDateToString6(Date date){
        return date2String(new SimpleDateFormat(sdf),date);
    }

	/**
	 * 获取day
	 *
	 * @param days
	 *            偏移的天数，可为负数
	 * @return
	 */
	public static String getDayValue(int days) {
		if (days == 0) {
			return getDayValue();
		}
		Calendar cal = Calendar.getInstance();// 使用默认时区和语言环境获得一个日历。
		cal.add(Calendar.DAY_OF_YEAR, days);
		return date2String(new SimpleDateFormat(dayFormat), cal.getTime());
	}

	public static String getTimeFileName() {
		return date2String(new SimpleDateFormat(fullTimeStampFormat), new Date());
	}

	public static String getFullTimeStamp() {
		return date2String(new SimpleDateFormat(fullTimeStampFormat), new Date());
	}

	public static String getSimpleDateTime() {
		return date2String(new SimpleDateFormat(sdf), new Date());
	}

	public static String getHourMinuteTime() {
		return date2String(new SimpleDateFormat(hour_minite_sdf), new Date());
	}

	public static Date parseDate(String date, String format) {
		if (StringUtils.isEmpty(date) || StringUtils.isEmpty(format)) {
			return null;
		}

		SimpleDateFormat dateFormatMid = new SimpleDateFormat(format);

		try {
			return dateFormatMid.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 返回消息对话列表显示时间格式
	 * @param date
	 * @return
	 */
	public static String getMessageDateString(Date date) {
		return date2String(new SimpleDateFormat(messageDateFormat), date);
	}

	public static String getDateString(Date date, String format) {
		return date2String(new SimpleDateFormat(format), date);
	}

	/**
	 * 以yyyy-MM-dd HH:mm:ss的格式解析日期
	 *
	 * @param date
	 * @return
	 */
	public static Date parseFullDate(String date) {

		if (TextUtils.isEmpty(date)) {
			return null;
		}

		try {
			return new SimpleDateFormat(fulldateFormat).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 今年的日期显示为MM-dd HH:mm的格式，以前年份则为yyyy-MM-dd
	 *
	 * @param date
	 * @return
	 */
	public static String getCustomizedDateString(String date) {

		if (TextUtils.isEmpty(date)) {
			return null;
		}

		try {
			Date tempDate = new SimpleDateFormat(fulldateFormat).parse(date);
			int thisYear = (new Date()).getYear();
			if (tempDate.getYear() == thisYear) {
				return date2String(new SimpleDateFormat(sdf), tempDate);
			} else {
				return date2String(new SimpleDateFormat(dateFormat3), tempDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 今年的日期显示为MM-dd HH:mm的格式，以前年份则为yyyy-MM-dd
	 *
	 * @param date
	 * @return
	 */
	public static String getCustomizedDateString(Date date) {

		Date tempDate = date;
		int thisYear = (new Date()).getYear();
		if (tempDate.getYear() == thisYear) {
			return date2String(new SimpleDateFormat(sdf), tempDate);
		} else {
			return date2String(new SimpleDateFormat(dateFormat3), tempDate);
		}
	}

	/**
	 * 以yyyy-MM-dd HH:mm的格式解析日期
	 *
	 * @param date
	 * @return
	 */
	public static Date parseDate(String date) {

		if (TextUtils.isEmpty(date)) {
			return null;
		}

		try {
			return new SimpleDateFormat(dateFormat).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 以yyyy-MM-dd的格式解析日期
	 *
	 * @param date
	 * @return
	 */
	public static Date parseSimpleDate(String date) {

		if (TextUtils.isEmpty(date)) {
			return null;
		}

		try {
			return new SimpleDateFormat(dateFormat3).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int datediffDay(Date stime, Date etime) {
		try {
			Date mstime = new SimpleDateFormat(dateFormat3).parse(convertDateToString(stime));
			Date metime = new SimpleDateFormat(dateFormat3).parse(convertDateToString(etime));
			return (int) ((metime.getTime() - mstime.getTime()) / 1000 / 60 / 60 / 24);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -999;
	}

	public static int diff(Date stime, Date etime) {
		try {
			Date mstime = new SimpleDateFormat(dateFormat).parse(convertDateToString(stime));
			Date metime = new SimpleDateFormat(dateFormat).parse(convertDateToString(etime));
			return (int) ((metime.getTime() - mstime.getTime()) / 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 分转为毫秒时间
	 *
	 * @param minus
	 * @return
	 */
	public static int parseMinusToMs(int minus) {
		if (minus < 0) {
			return 0;
		}
		return minus * 60 * 1000;
	}

	public static String date2String(SimpleDateFormat format, Date date) {
		if (date == null || format == null) {
			return null;
		}
		try {
			return format.format(date);
		} catch (Exception ex) {

		}
		return null;
	}

	/**
	 * 检查记录是否已经过期
	 *
	 * @return 是否已经过期
	 */
	public static boolean checkExpire(Date date, int minute) {

		if (date == null) {
			return true;
		}
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.MINUTE, minute);
		if (c1.compareTo(c2) <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 检查记录是否已经过期
	 *
	 * @return 是否已经过期
	 */
	public static boolean checkExpireByHour(Date date, int hour) {

		if (date == null) {
			return true;
		}
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.HOUR, hour);
		if (c1.compareTo(c2) <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 检查记录是否已经过期
	 *
	 * @return 是否已经过期
	 */
	public static boolean checkExpireByDay(Date date, int day) {

		if (date == null) {
			return true;
		}
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.DATE, day);
		if (c1.compareTo(c2) <= 0) {
			return true;
		}
		return false;
	}

	/**
     * 计算两个日期之间相差的天数
     * @param calendar1 小一点的日期
     * @param calendar2 大一点的日期
     * @return
     */
    public static int getDayInterval(Calendar calendar1,Calendar calendar2){    	
    	int year1=calendar1.get(Calendar.YEAR);
    	int year2=calendar2.get(Calendar.YEAR);
    	int daysInYear1=calendar1.get(Calendar.DAY_OF_YEAR);
    	int daysInYear2=calendar2.get(Calendar.DAY_OF_YEAR);
    	if (year1==year2){
    		return daysInYear2-daysInYear1;
    	}
    	if (year2>year1){
    		int daysCount;
    		if (isLeapYear(year1)){
    			daysCount=366-daysInYear1;
    		}else{
    			daysCount=365-daysInYear1;
    		}
    		for (int index=year1+1;index<year2;index++){
    			if (isLeapYear(index)){
        			daysCount+=366;
        		}else{
        			daysCount+=365;
        		}	
    		}
    		daysCount+=daysInYear2;
    		return daysCount;
    	}
    	int daysCount;
		if (isLeapYear(year2)){
			daysCount=366-daysInYear2;
		}else{
			daysCount=365-daysInYear2;
		}
		for (int index=year2+1;index<year1;index++){
			if (isLeapYear(index)){
    			daysCount+=366;
    		}else{
    			daysCount+=365;
    		}	
		}
		daysCount+=daysInYear1;
		return 0-daysCount;
    }
    /**
     * 判断一年是否是闰年    
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year){
        return year % 400 == 0 || (year % 100 != 0 && year % 4 == 0);
    }
	/**
     * 计算两个日期之间相差的天数
     * @param date1   小一点的日期
     * @param date2   大一点的日期
     * @return
     */
    public static int getDayInterval(Date date1,Date date2){
        //return getDayInterval(date1.getTime(),date2.getTime());
    	Calendar calendar1=Calendar.getInstance(),calendar2=Calendar.getInstance();
    	calendar1.setTime(date1);
    	calendar2.setTime(date2);
    	return getDayInterval(calendar1,calendar2);
    }
	/**
     * 计算两个日期之间相差的天数
     * @param calendar1 小一点的日期
     * @param date2 大一点的日期
     * @return
     */
    public static int getDayInterval(Calendar calendar1,Date date2){
        //return getDayInterval(calendar1.getTimeInMillis(),date2.getTime());
    	Calendar calendar2=Calendar.getInstance();
    	calendar2.setTime(date2);
    	return getDayInterval(calendar1,calendar2);
    }
	/**
     * 计算两个日期之间相差的天数
     * @param date1   小一点的日期
     * @param calendar2   大一点的日期
     * @return
     */
    public static int getDayInterval(Date date1,Calendar calendar2){
        //return getDayInterval(date1.getTime(),calendar2.getTimeInMillis());
    	Calendar calendar1=Calendar.getInstance();
    	calendar1.setTime(date1);
    	return getDayInterval(calendar1,calendar2);
    }
    /*
    public static int getDayInterval(long time1,long time2){
    	long between_days=(time2-time1)/(DAY_MINUTE);
    	return (int)between_days;
    }*/

	/**
	 * V6.6短视频帖子发表时间需求：
	 * 1分钟内：刚刚
	 * 1小时内：xx分钟前
	 * 1小时-当天0点：x小时前
	 * 昨天：昨天
	 * 2-5天：星期x
	 * >5天：x月x日
	 */
	public static String getEventMsgTime(long time) {
		return getEventMsgTime(new Date(time));
	}

	public static String getEventMsgTime(Date publishTime) {
		String timeStr = null;
		if(publishTime == null){
			return "";
		}
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		long delay = curDate.getTime() - publishTime.getTime();
		if(delay < 60*1000){//1min
			timeStr = "刚刚";
		}else if(delay < 60*60*1000){//1h
			int min = (int)(delay/(1000*60));
			timeStr = min + "分钟前";
		}else{
			//        long EventMsgDayNum = time /1000/60/60/24; //计算到1970/01/01的天数 使用毫秒时间除法 会有四舍五入误差
			//        long CurTimeDayNum = curTimeMillis /1000/60/60/24;
			int msgYearInt = Integer.parseInt( new SimpleDateFormat("yyyy").format(publishTime));
			int msgMonInt = Integer.parseInt(new SimpleDateFormat("MM").format(publishTime));
			int msgDayInt = Integer.parseInt(new SimpleDateFormat("dd").format(publishTime));

			int curYearInt = Integer.parseInt(new SimpleDateFormat("yyyy").format(curDate));
			int curMonInt = Integer.parseInt(new SimpleDateFormat("MM").format(curDate));
			int curDayInt = Integer.parseInt(new SimpleDateFormat("dd").format(curDate));
			long EventMsgDayNum = DateUtils.getTotalDayNum(msgYearInt,msgMonInt,msgDayInt);//计算发帖时间到1970/01/01的天数
			long CurTimeDayNum = DateUtils.getTotalDayNum(curYearInt,curMonInt,curDayInt);//计算当前时间到1970/01/01的天数

			if(CurTimeDayNum == EventMsgDayNum) { // 同一天
				Calendar cal = Calendar.getInstance();// 当前日期
				int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
				Calendar cal2 = Calendar.getInstance();
				cal2.setTime(publishTime);
				int hour2 = cal2.get(Calendar.HOUR_OF_DAY);// 获取小时
				int delayHour = hour - hour2;
				timeStr = delayHour + "小时前";
			} else if (CurTimeDayNum - EventMsgDayNum == 1) { // 前一天
				timeStr = "昨天";
			} else if ( CurTimeDayNum - EventMsgDayNum >= 2 && CurTimeDayNum - EventMsgDayNum <= 5) { // 前2到5天
				SimpleDateFormat sdfWeek = new SimpleDateFormat("E");
				timeStr = sdfWeek.format(publishTime);timeStr = timeStr.replace("周","星期");

			} else { //>5天
				SimpleDateFormat sdfDate = new SimpleDateFormat("MM月dd日");
				timeStr = sdfDate.format(publishTime);
			}
		}
		return timeStr;
	}


	public static String getEventMsgTime(String publishDate){
		if(publishDate == null){
			return "";
		}
		Date publishTime = parseFullDate(publishDate);
		return getEventMsgTime(publishTime);
	}

	public static boolean isNotPrimeYear(int year) {//计算是否闰年
		return year % 4 == 0 && (year % 400 == 0 || year % 100 != 0);
	}

	public static int getDayOfMonth(int year, int month) {//计算每月天数
		int[] days = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		return isNotPrimeYear(year) && month == 2 ? days[month] + 1 : days[month];
	}

	public static long getTotalDayNum(int year, int month, int day) {//计算总天数
		long sum = 0;
		for (int i = 1970; i < year; i++) {
			sum += 365;
			if (isNotPrimeYear(i))
				sum++;
		}
		for (int j = 0; j < month; j++) {
			sum += getDayOfMonth(year, j);
		}
		return sum + day;
	}
}
