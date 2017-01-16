package com.app.model;

public class User{

	private boolean defaultPhoneType=true;//是否启动默认扫描thimfone：true；G0550：false

	public boolean isDefaultPhoneType() {
		return defaultPhoneType;
	}

	public void setDefaultPhoneType(boolean defaultPhoneType) {
		this.defaultPhoneType = defaultPhoneType;
	}
	
	
}
