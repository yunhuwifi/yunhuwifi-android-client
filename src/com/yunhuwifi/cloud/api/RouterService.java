package com.yunhuwifi.cloud.api;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.yunhuwifi.UserContext;
import com.yunhuwifi.cloud.api.models.RequestBindRouter;
import com.yunhuwifi.common.Constans;
import com.yunhuwifi.util.HttpUtility;

public class RouterService {

	private UserContext context;

	public RouterService(UserContext context) {
		this.context = context;
	}

	public void getRouters(AjaxCallBack<?> callback) {
		HttpUtility.post(ServiceBase.URL_PREFIX + "/router/list",
				new ArrayList<NameValuePair>(), context.getCookieStore(),
				callback);
	}

	public void bindRouter(RequestBindRouter model, AjaxCallBack<?> callback) {

		StringEntity entity = null;
		try {
			entity = new StringEntity(model.toJson(), HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpUtility.post(ServiceBase.URL_PREFIX + "/router/bind", entity,
				Constans.ContentType.Json, context.getCookieStore(), callback);
	}

	public void remove(String macaddr, AjaxCallBack<?> callback) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("macaddr", macaddr));

		HttpUtility.post(ServiceBase.URL_PREFIX + "/router/remove", params,
				context.getCookieStore(), callback);
	}

	public void isRouterBound(String macaddr, AjaxCallBack<?> callback) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("macaddr", macaddr));

		HttpUtility.post(ServiceBase.URL_PREFIX + "/router/isRouterBound",
				params, context.getCookieStore(), callback);
	}
}
