package com.mobile.api.model;

import java.io.Serializable;

public class RsToken implements Serializable {

	private static final long serialVersionUID = 4679983358734049010L;
	private String token;
	private RsError error;

	public RsToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return this.token;
	}

	public RsError getError() {
		return error;
	}

	public void setError(RsError error) {
		this.error = error;
	}
	
	
}
