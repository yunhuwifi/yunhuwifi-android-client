package com.foxrouter.api;

import com.foxrouter.api.model.RouterInfo;

public class RouterResponseRouterInfo extends RouterResponse {

	private RouterInfo result;

	public RouterInfo getResult() {
		return result;
	}

	public void setResult(RouterInfo result) {
		this.result = result;
	}
}
