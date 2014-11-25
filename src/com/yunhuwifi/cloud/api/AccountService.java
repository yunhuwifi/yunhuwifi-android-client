package com.yunhuwifi.cloud.api;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yunhuwifi.UserContext;
import com.yunhuwifi.util.HttpUtility;

public class AccountService {

	private UserContext context;

	public AccountService(UserContext context) {
		this.context = context;
	}

	public void login(UserContext userContext, AjaxCallBack<?> callback) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", userContext
				.getLoginName()));
		params.add(new BasicNameValuePair("password", userContext.getPassword()));
		HttpUtility.post(ServiceBase.URL_PREFIX + "/account/login", params,
				userContext.getCookieStore(), callback);
	}

	public void login(String username, String password, AjaxCallBack<?> callback) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		HttpUtility.post(ServiceBase.URL_PREFIX + "/account/login", params,
				context.getCookieStore(), callback);
	}

	public void register(String username, String password,
			AjaxCallBack<?> callback) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		HttpUtility.post(ServiceBase.URL_PREFIX + "/account/register", params,
				context.getCookieStore(), callback);
	}
}
