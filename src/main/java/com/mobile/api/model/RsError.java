package com.mobile.api.model;

import java.io.Serializable;

public class RsError implements Serializable {

	private static final long serialVersionUID = 3072091106767085326L;
	
	private String errorCode;
	private String errorDesc;
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	
	
	
}
