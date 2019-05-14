package com.wofang.demo.utils;

import android.annotation.SuppressLint;


import com.wofang.demo.network.util.ParamAlias;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MD5Utils {

	private static String sign = "naliwan";
	
	/**
	 * 单个请求参数md5加密
	 * @param params
	 * @return
	 */
	public static String getMD5(String params){
		String paramsString = "";
		if (params == null) {
			paramsString = sign;
		}else{
			paramsString = params + sign;
		}
		
		return getMd5(paramsString);
	}

	/**
	 * 获取请求参数md5串
	 * @return
	 */
	public static String getMD5(Object paramsObj) {
		String paramsString = "";
		if (paramsObj != null) {
			// 将对象转成集合
			Map<String, Object> paramsMap = restructParamsObj(paramsObj);
			// 对key排序，获取value字符串
			String params = sortMap(paramsMap) + sign;
			// Md5加密 32位大写
			paramsString = getMd5(params);
			// 输出请求参数串
			String request = "";
			for (String key : paramsMap.keySet()) {
				request += key + "=" + paramsMap.get(key).toString() + ",";
			}

			System.out.println(request + "sign=" + paramsString);
			return paramsString;
		}

		return getMd5(sign);
	}

	public static String getPasswordMD5(String password) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = password.getBytes();
			// 使用MD5创建MessageDigest对象
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = md[i];
				// System.out.println((int)b);
				// 将没个数(int)b进行双字节加密
				str[k++] = hexDigits[b >> 4 & 0xf];
				str[k++] = hexDigits[b & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	private static String sortMap(Map<String, Object> map) {
		String str = "";
		Map<String, Object> map1 = new TreeMap<String, Object>();
		map1 = map;
		Map<String, Object> resultMap = sortMapByKey(map1); // 按Key进行排序
		for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
			str += entry.getValue();
		}

		return str;
	}

	// 排序
	private static Map<String, Object> sortMapByKey(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, Object> sortMap = new TreeMap<String, Object>(new MapKeyComparator());
		sortMap.putAll(map);
		return sortMap;
	}

	// 比较器类
	private static class MapKeyComparator implements Comparator<String> {
		public int compare(String str1, String str2) {
			return str1.compareTo(str2);
		}
	}

	@SuppressLint("DefaultLocale")
	private static Map<String, Object> restructParamsObj(Object paramsObj) {
		Map<String, Object> map = new HashMap<String, Object>();

		for (Field field : paramsObj.getClass().getDeclaredFields()) {
			String paramKey;
			ParamAlias paramAlias = field.getAnnotation(ParamAlias.class);
			if (paramAlias != null) {
				paramKey = paramAlias.value();
			} else {
				paramKey = field.getName();
			}

			Object paramValue = null;
			String getterName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
			try {
				Method method = paramsObj.getClass().getDeclaredMethod(getterName, (Class<?>[]) null);
				paramValue = method.invoke(paramsObj);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (paramValue == null) {
				map.put(paramKey, "");
			} else {
				map.put(paramKey, paramValue);
			}
		}

		return map;
	}

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	private static String toHexString(byte[] b) { // String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	private static String getMd5(String s) {
		try { // Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();
			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
}
