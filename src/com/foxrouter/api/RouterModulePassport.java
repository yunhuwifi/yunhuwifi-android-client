package com.foxrouter.api;

import com.yunhuwifi.RouterContext;
import com.yunhuwifi.handlers.JsonCallBack;

public class RouterModulePassport extends RouterModule {

	public final static String DEFAULT_USER = "root";
	public final static int DEFAULT_PORT = 80;

	public RouterModulePassport(RouterContext context) {
		super(context);
		super.setModule("passport");
	}

	public void login(JsonCallBack<? extends Object> callback) {
		super.execute("login", new String[] { this.routerContext.getUsername(),
				this.routerContext.getPassword() }, callback);
	}

	public void logout(JsonCallBack<? extends Object> callback) {
		super.execute("logout", null, callback);
	}

	public void changePassword(String oldPwd, String newPwd,
			JsonCallBack<? extends Object> callback) {
		super.execute("changepassword", new Object[] { oldPwd, newPwd },
				callback);
	}
}
