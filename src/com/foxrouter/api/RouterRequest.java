package com.foxrouter.api;

import com.google.gson.Gson;
import com.yunhuwifi.YunhuApplication;

public class RouterRequest {

	private transient YunhuApplication context;
	private int id;
	private String method;
	private Object[] params;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public String toJSONString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public YunhuApplication getContext() {
		return context;
	}

	public void setContext(YunhuApplication context) {
		this.context = context;
	}

}
