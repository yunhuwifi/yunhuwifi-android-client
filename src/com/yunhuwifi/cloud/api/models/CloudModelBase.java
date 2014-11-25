package com.yunhuwifi.cloud.api.models;

import com.google.gson.Gson;

public abstract class CloudModelBase {

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
