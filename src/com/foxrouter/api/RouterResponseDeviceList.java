package com.foxrouter.api;

import java.util.List;

import com.foxrouter.api.model.RouterDevice;

public class RouterResponseDeviceList extends RouterResponse {

	private List<RouterDevice> result;

	public List<RouterDevice> getResult() {
		return result;
	}

	public void setResult(List<RouterDevice> result) {
		this.result = result;
	}

}
