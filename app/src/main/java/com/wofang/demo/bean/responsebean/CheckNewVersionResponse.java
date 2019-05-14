package com.wofang.demo.bean.responsebean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class CheckNewVersionResponse implements Serializable {

	private String lastVersion; // 最新版本号
	private String lastVersionUrl; // 下载地址
	private String isForce; // "1"强制更新，"0"不强制更新
	private String desc; // 版本描述
	private String errCode;//错误码00000 成功
	private String errMsg;//错误信息
	private Object data;//返回的数据
	private String window;//是否弹出强制更新弹窗 0:否,1:是
	
	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getWindow() {
		return window;
	}

	public void setWindow(String window) {
		this.window = window;
	}

	public String getLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(String lastVersion) {
		this.lastVersion = lastVersion;
	}

	public String getLastVersionUrl() {
		return lastVersionUrl;
	}

	public void setLastVersionUrl(String lastVersionUrl) {
		this.lastVersionUrl = lastVersionUrl;
	}

	public String getIsForce() {
		return isForce;
	}

	public void setIsForce(String isForce) {
		this.isForce = isForce;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
