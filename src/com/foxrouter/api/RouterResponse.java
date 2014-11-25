package com.foxrouter.api;

import java.lang.reflect.Type;

import com.google.gson.Gson;

public abstract class RouterResponse {
	private int id;
	private RouterError error;
	private String value;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RouterError getError() {
		return error;
	}

	public void setError(RouterError error) {
		this.error = error;
	}

	public <T> T getObject(Type clazz) {
		Gson gson = new Gson();
		return gson.fromJson(getValue(), clazz);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
