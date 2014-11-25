package com.yunhuwifi.cloud.api.models;

import com.google.gson.annotations.SerializedName;

public class ResponseLogin extends ApiResult {
	@SerializedName("Result")
	private User result;

	public User getResult() {
		return result;
	}

	public void setResult(User result) {
		this.result = result;
	}
}
