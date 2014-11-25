package com.foxrouter.api;

import com.yunhuwifi.RouterContext;
import com.yunhuwifi.handlers.JsonCallBack;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

public class RouterModuleDetect extends RouterModule {

	public RouterModuleDetect(RouterContext context) {
		super(context);
		super.setModule("detect");

	}

	public void isSupportedRouter(Context context,
			JsonCallBack<? extends Object> callback)
			throws NetworkErrorException {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		if (wifiManager.isWifiEnabled()) {
			String ipaddr = Formatter
					.formatIpAddress(wifiManager.getDhcpInfo().serverAddress);

			if (routerContext == null) {
				routerContext = new RouterContext();
			}
			routerContext.setIPAndPort(ipaddr, 80);

			super.execute("status", null, callback);
		} else {
			throw new NetworkErrorException("未开启手机Wifi.");
		}
	}

	public void autoDetect(Context context,
			JsonCallBack<? extends Object> callback)
			throws NetworkErrorException {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		if (wifiManager.isWifiEnabled()) {
			String ipaddr = Formatter
					.formatIpAddress(wifiManager.getDhcpInfo().serverAddress);

			if (routerContext == null) {
				routerContext = new RouterContext();
			}
			routerContext.setIPAndPort(ipaddr, 80);

			super.execute("status", null, callback);
		} else {
			throw new NetworkErrorException("未开启手机Wifi.");
		}
	}
}
