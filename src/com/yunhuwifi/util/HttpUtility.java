package com.yunhuwifi.util;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.yunhuwifi.common.Constans;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

public class HttpUtility {
	public static final int DID_START = 0;
	public static final int DID_ERROR = 1;
	public static final int DID_SUCCEED = 2;

	public static void post(String url, List<NameValuePair> params,
			CookieStore cookieStore, AjaxCallBack<? extends Object> callBack) {
		UrlEncodedFormEntity entity = null;

		if (params != null) {
			try {
				entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			} catch (UnsupportedEncodingException e) {
			}
		}
		post(url, entity, cookieStore, callBack);

	}

	public static void post(String url, HttpEntity entity,
			CookieStore cookieStore, AjaxCallBack<? extends Object> callBack) {

		post(url, entity, Constans.ContentType.FormUrlEncoded, cookieStore,
				callBack);
	}

	public static void post(String url, HttpEntity entity, String contentType,
			CookieStore cookieStore, AjaxCallBack<? extends Object> callBack) {
		FinalHttp http = new FinalHttp();
		http.configTimeout(15000);
		http.configCharset(HTTP.UTF_8);
		http.configCookieStore(cookieStore);
		http.configUserAgent(Constans.UserAgent.Android);

		String paramsString;
		try {
			paramsString = EntityUtils.toString(entity, HTTP.UTF_8);

			Log.d("http", "url:" + url + " params:" + paramsString);
			http.post(url, entity, contentType, callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
