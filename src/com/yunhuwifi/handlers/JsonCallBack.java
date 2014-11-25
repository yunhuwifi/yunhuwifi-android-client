package com.yunhuwifi.handlers;

import android.util.Log;

import com.google.gson.Gson;

import net.tsz.afinal.http.AjaxCallBack;

public abstract class JsonCallBack<T> extends AjaxCallBack<String> {
	private Class<T> type;

	public JsonCallBack(Class<T> clazz) {
		this.type = clazz;
	}

	@Override
	public void onSuccess(String result) {
		Log.d("http success", result);
		try {
			Gson gson = new Gson();
			T t = (T) gson.fromJson(result, type);
			onJsonSuccess(t);
		} catch (Exception e) {
			e.printStackTrace();
			onFailure(e, 0, e.getMessage());
		}
	}

	@Override
	public void onFailure(Throwable t, int errorNo, String strMsg) {
		Log.d("http failure", strMsg);
		// super.onFailure(t, errorNo, strMsg);

		t.printStackTrace();
	}

	public abstract void onJsonSuccess(T t);
}
