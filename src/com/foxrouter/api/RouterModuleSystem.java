package com.foxrouter.api;

import com.yunhuwifi.RouterContext;
import com.yunhuwifi.handlers.JsonCallBack;

public class RouterModuleSystem extends RouterModule {
	public RouterModuleSystem(RouterContext context) {
		super(context);
		super.setModule("system");
	}

	public void info(JsonCallBack<? extends Object> callback) {
		super.execute("info", null, callback);
	}

	public void reset(JsonCallBack<? extends Object> callback) {
		super.execute("reset", null, callback);
	}

	public void reboot(JsonCallBack<? extends Object> callback) {
		super.execute("reboot", null, callback);
	}
}
