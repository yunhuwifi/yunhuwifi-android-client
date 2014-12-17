package com.foxrouter.api;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.google.gson.Gson;
import com.yunhuwifi.RouterContext;
import com.yunhuwifi.handlers.JsonCallBack;
import com.yunhuwifi.util.HttpUtility;

public abstract class RouterModule {

	private String module;

	protected RouterContext routerContext;

	public RouterModule(RouterContext context) {
		this.routerContext = context;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	protected void execute(String method, Object[] params,
			JsonCallBack<? extends Object> callback) {

		RouterRequest request = new RouterRequest();
		request.setId(this.routerContext.getNextRequestId());
		request.setMethod(method);
		request.setParams(params);

		String url = "http://" + routerContext.getIpAddress() + ":"
				+ routerContext.getPort();
		url += "/cgi-bin/luci/api/" + module;
		if (routerContext.getToken() != null) {
			url += "?auth=" + routerContext.getToken();
		}

		String json = request.toJSONString();
		HttpEntity entity = null;
		try {
			entity = new StringEntity(json, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpUtility.post(url, entity, this.routerContext.getCookieStore(),
				callback);
	}
}
