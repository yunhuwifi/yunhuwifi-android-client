package com.foxrouter.api;

import com.yunhuwifi.RouterContext;
import com.yunhuwifi.handlers.JsonCallBack;

public class RouterModuleApp extends RouterModule {

	public RouterModuleApp(RouterContext context) {
		super(context);
		super.setModule("app");
	}

	public void list(JsonCallBack<? extends Object> callback) {
		super.execute("list", null, callback);
	}

	public void install(String id, JsonCallBack<? extends Object> callback) {
		super.execute("install", new Object[] { id }, callback);
	}

	public void remove(String id, JsonCallBack<? extends Object> callback) {
		super.execute("remove", new Object[] { id }, callback);
	}

	public void config(String id, JsonCallBack<? extends Object> callback) {
		super.execute("config", new Object[] { id }, callback);
	}
}
