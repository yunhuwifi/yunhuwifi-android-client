package com.yunhuwifi;

import org.apache.http.client.CookieStore;

import com.foxrouter.api.RouterModulePassport;
import com.foxrouter.api.RouterModuleSystem;
import com.foxrouter.api.RouterResponseLogin;
import com.foxrouter.api.RouterResponseRouterInfo;
import com.foxrouter.api.model.RouterInfo;
import com.yunhuwifi.handlers.JsonCallBack;

public class RouterContext {

	private int requestId;
	private boolean logined;
	private CookieStore cookieStore;
	private String token;
	private String ipAddress;
	private int port;
	private RouterInfo router;
	private String username;
	private String password;

	public boolean isLogined() {
		return logined;
	}

	public CookieStore getCookieStore() {
		return cookieStore;
	}

	public void setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getNextRequestId() {
		return this.requestId++;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setIPAndPort(String ipAddress, int port) {
		this.ipAddress = ipAddress;
		this.port = port;
	}

	public RouterInfo getRouter() {
		return router;
	}

	public void setRouter(RouterInfo router) {
		this.router = router;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static void login(String username, String password, String ipaddr,
			int port, final JsonCallBack<RouterContext> callback) {
		final RouterContext context = new RouterContext();
		context.setIpAddress(ipaddr);
		context.setPort(port);
		context.setUsername(username);
		context.setPassword(password);
		RouterModulePassport passport = new RouterModulePassport(context);
		passport.login(new JsonCallBack<RouterResponseLogin>(
				RouterResponseLogin.class) {

			@Override
			public void onJsonSuccess(final RouterResponseLogin loginresult) {
				if (loginresult != null && loginresult.getResult() != null) {
					context.logined = true;
					context.setToken(loginresult.getResult());
					
					RouterModuleSystem system = new RouterModuleSystem(context);
					system.info(new JsonCallBack<RouterResponseRouterInfo>(
							RouterResponseRouterInfo.class) {

						@Override
						public void onJsonSuccess(RouterResponseRouterInfo resp) {
							if (resp != null && resp.getResult() != null) {
								context.setRouter(resp.getResult());

								callback.onJsonSuccess(context);
							} else {
								callback.onFailure(null, 1001, "获取路由器信息失败。");
							}
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							callback.onFailure(t, errorNo, strMsg);
						}
					});
				} else {
					callback.onFailure(null, 1000, "账号或密码错误。");
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				callback.onFailure(t, errorNo, strMsg);
			}
		});
	}
}
