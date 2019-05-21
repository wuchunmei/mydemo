package com.wofang.demo.cache;

import java.io.Serializable;
import java.util.Date;

public final class EntityCacheject implements Serializable{
	private final String type;
	private final Date lastUpdateDate;
	private final Serializable cacheData;
	public EntityCacheject(String type, Date lastUpdateDate){
		this(type,lastUpdateDate,null);
	}
	public EntityCacheject(String type, Date lastUpdateDate, Serializable cacheData){
		this.type=type;
		this.lastUpdateDate=lastUpdateDate;
		this.cacheData=cacheData;
	}
	public String getType() {
		return type;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public Serializable getCacheData() {
		return cacheData;
	}
}
