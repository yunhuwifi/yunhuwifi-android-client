package com.yunhuwifi;

import cn.smssdk.SMSSDK;

import com.baidu.frontia.FrontiaApplication;
import com.umeng.update.UmengUpdateAgent;
import com.yunhuwifi.service.AppKey;

import android.app.Application;

public class YunhuApplication extends Application {

	private RouterContext routerContext;
	private UserContext userContext;

	@Override
	public void onCreate() {
		super.onCreate();
		// 自动更新
		UmengUpdateAgent.setAppkey(AppKey.umengAPPKEY);
		UmengUpdateAgent.setChannel(null);
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.update(this, AppKey.umengAPPKEY, "");
		// 初始化短信服务
		SMSSDK.initSDK(this, AppKey.captchaAPPKEY, AppKey.captchaAPPSECRET);
		
		FrontiaApplication. 
        initFrontiaApplication(getApplicationContext());
	}

	public RouterContext getRouterContext() {
		return routerContext;
	}

	public void setRouterContext(RouterContext routerContext) {
		this.routerContext = routerContext;
	}

	public UserContext getUserContext() {
		return userContext;
	}

	public void setUserContext(UserContext userContext) {
		this.userContext = userContext;
	}
}
