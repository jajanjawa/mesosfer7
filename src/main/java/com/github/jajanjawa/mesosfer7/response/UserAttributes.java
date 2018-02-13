package com.github.jajanjawa.mesosfer7.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UserAttributes {

	private String accessToken;
	private String country;
	private Date expireAt;
	private Long expiresIn;
	private String firstname;
	private String lastname;
	private JsonObject metadata;
	private String objectId;
	private String phone;
	private String tokenType;
	private Date updatedAt;
	@SerializedName("validemail")
	private String validEmail;

	/**
	 * Akses token
	 * @return token
	 */
	public String getAccessToken() {
		return accessToken;
	}

	public String getCountry() {
		return country;
	}

	public Date getExpireAt() {
		return expireAt;
	}
	
	/**
	 * @return dalam detik
	 */
	public Long getExpiresIn() {
		return expiresIn;
	}

	public long getExpiresIn(TimeUnit timeUnit) {
		return timeUnit.convert(expiresIn, TimeUnit.SECONDS);
	}
	
	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public JsonObject getMetadata() {
		return metadata;
	}

	public JsonElement getField(String name) {
		return metadata.get(name);
	}

	public String getObjectId() {
		return objectId;
	}

	public String getPhone() {
		return phone;
	}

	public String getTokenType() {
		return tokenType;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public String getValidEmail() {
		return validEmail;
	}
}
