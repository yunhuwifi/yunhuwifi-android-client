package com.yunhuwifi.cloud.api;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.NameValuePair;

import com.yunhuwifi.UserContext;
import com.yunhuwifi.util.HttpUtility;

public class AppService {

	private UserContext context;

	public AppService(UserContext context) {
		this.context = context;
	}

	public void list(String keyword, AjaxCallBack<?> callback) {
		HttpUtility.post(ServiceBase.URL_PREFIX + "/app/list",
				new ArrayList<NameValuePair>(), context.getCookieStore(),
				callback);
	}
}
