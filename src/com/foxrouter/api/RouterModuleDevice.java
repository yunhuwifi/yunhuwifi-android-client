package com.foxrouter.api;

import com.yunhuwifi.RouterContext;
import com.yunhuwifi.handlers.JsonCallBack;

public class RouterModuleDevice extends RouterModule {

	public RouterModuleDevice(RouterContext context) {
		super(context);
		super.setModule("device");

	}

	public void list(JsonCallBack<? extends Object> callback) {
		super.execute("list", null, callback);
	}

	public void remark(String name, String macAddr,
			JsonCallBack<? extends Object> callback) {
		super.execute("remark", new Object[] { macAddr, name }, callback);
	}

	public void disconnect(String macAddr,
			JsonCallBack<? extends Object> callback) {
		super.execute("disconnect", new Object[] { macAddr }, callback);
	}
}
