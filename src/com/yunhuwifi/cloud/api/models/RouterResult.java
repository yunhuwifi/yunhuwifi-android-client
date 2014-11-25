package com.yunhuwifi.cloud.api.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class RouterResult extends ApiResult {
	@SerializedName("Result")
	private List<RouterModel> result;

	public List<RouterModel> getResult() {
		return this.result;
	}

	public void setresult(List<RouterModel> model) {
		this.result = model;
	}
}
