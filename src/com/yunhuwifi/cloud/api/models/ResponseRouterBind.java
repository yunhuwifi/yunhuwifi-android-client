package com.yunhuwifi.cloud.api.models;

import com.google.gson.annotations.SerializedName;

public class ResponseRouterBind extends ApiResult {

	@SerializedName("Result")
	private boolean result;

	public boolean isResult() {
		return this.result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
}
