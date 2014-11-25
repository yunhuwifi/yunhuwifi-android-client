package com.foxrouter.api;

import com.foxrouter.api.model.RouterWifiDetails;
import com.yunhuwifi.RouterContext;
import com.yunhuwifi.handlers.JsonCallBack;

public class RouterModuleWifi extends RouterModule {

	public RouterModuleWifi(RouterContext context) {
		super(context);
		super.setModule("wifi");
	}

	public void list(JsonCallBack<? extends Object> callback) {
		super.execute("list", null, callback);
	}

	public void wifiDetails(String netid,
			JsonCallBack<? extends Object> callback) {
		super.execute("get_wifi", null, callback);
	}

	public void editWifi(RouterWifiDetails details,
			JsonCallBack<? extends Object> callback) {
		super.execute("wifi_edit", null, callback);
	}

	public void openWifi(String netid, JsonCallBack<? extends Object> callback) {
		super.execute("wifi_open", new String[] { netid }, callback);
	}

	public void closeWifi(String netid, JsonCallBack<? extends Object> callback) {
		super.execute("wifi_close", new String[] { netid }, callback);
	}
}
