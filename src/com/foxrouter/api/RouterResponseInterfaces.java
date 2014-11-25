package com.foxrouter.api;

import java.util.List;

import com.foxrouter.api.model.RouterInterface;

public class RouterResponseInterfaces extends RouterResponse {

	private List<RouterInterface> result;

	public List<RouterInterface> getResult() {
		return result;
	}

	public void setResult(List<RouterInterface> result) {
		this.result = result;
	}
}
