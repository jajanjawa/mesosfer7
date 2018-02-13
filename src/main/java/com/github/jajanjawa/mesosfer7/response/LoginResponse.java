package com.github.jajanjawa.mesosfer7.response;

import java.util.concurrent.TimeUnit;


public class LoginResponse {

	private String accessToken;
	private Long expiresIn;
	private String objectId;
	private String tokenType;

	public String getAccessToken() {
		return accessToken;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public long getExpiresIn(TimeUnit timeUnit) {
		return timeUnit.convert(expiresIn, TimeUnit.SECONDS);
	}

	public String getObjectId() {
		return objectId;
	}
	
	public String getTokenType() {
		return tokenType;
	}

}
