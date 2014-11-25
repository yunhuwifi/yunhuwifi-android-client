package com.foxrouter.api;

import java.util.List;

import com.foxrouter.api.model.RouterWifiDetails;

public class RouterResponseWifi extends RouterResponse {

	private List<RouterWifiDetails> result;

	public List<RouterWifiDetails> getResult() {
		return result;
	}

	public void setResult(List<RouterWifiDetails> result) {
		this.result = result;
	}
}
