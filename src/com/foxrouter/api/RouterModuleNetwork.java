package com.foxrouter.api;

import com.foxrouter.api.model.RouterNetworkSetting;
import com.foxrouter.api.model.RouterLanDetails;
import com.yunhuwifi.RouterContext;
import com.yunhuwifi.handlers.JsonCallBack;

public class RouterModuleNetwork extends RouterModule {
	public RouterModuleNetwork(RouterContext context) {
		super(context);
		super.setModule("network");
	}

	public void interfaces(JsonCallBack<? extends Object> callback) {
		super.execute("get_interfaces", null, callback);
	}

	public void lanDetails(JsonCallBack<? extends Object> callback) {
		super.execute("get_lan", null, callback);
	}

	public void editLan(RouterLanDetails details,
			JsonCallBack<? extends Object> callback) {
		super.execute("lan_edit", details.toArray(), callback);
	}

	public void wanDetails(JsonCallBack<? extends Object> callback) {
		super.execute("get_wan", null, callback);
	}

	public void editWan(RouterNetworkSetting setting,
			JsonCallBack<? extends Object> callback) {
		super.execute("wan_edit", setting.toArray(), callback);
	}

	public void netDetect(JsonCallBack<? extends Object> callback) {
		super.execute("netdetect", null, callback);
	}

	public void speedStatus(JsonCallBack<? extends Object> callback) {
		super.execute("speed_status", null, callback);
	}
}
