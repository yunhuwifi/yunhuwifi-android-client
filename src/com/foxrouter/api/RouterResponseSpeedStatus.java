package com.foxrouter.api;

import com.foxrouter.api.model.RouterSpeedStatus;

public class RouterResponseSpeedStatus extends RouterResponse {

	private RouterSpeedStatus result;

	public RouterSpeedStatus getResult() {
		return result;
	}

	public void setResult(RouterSpeedStatus result) {
		this.result = result;
	}
}
