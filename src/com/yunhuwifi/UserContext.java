package com.yunhuwifi;

import org.apache.http.client.CookieStore;

import com.yunhuwifi.cloud.api.AccountService;
import com.yunhuwifi.cloud.api.models.User;
import com.yunhuwifi.handlers.JsonCallBack;

public class UserContext {
	private CookieStore cookieStore;
	private boolean logined;
	private String loginName;
	private String password;
	private User user;

	public CookieStore getCookieStore() {
		return cookieStore;
	}

	public void setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}

	public boolean isLogined() {
		return logined;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static void login(String username, String password,
			JsonCallBack<UserContext> callback) {
		UserContext context = new UserContext();
		context.setLoginName(username);
		context.setPassword(password);

		AccountService accountService = new AccountService(context);
		accountService.login(context, callback);
	}
}
