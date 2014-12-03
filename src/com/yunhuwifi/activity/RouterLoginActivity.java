package com.yunhuwifi.activity;

import com.foxrouter.api.RouterModulePassport;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.RouterContext;
import com.yunhuwifi.handlers.JsonCallBack;
import com.yunhuwifi.models.RouterLocalAccount;
import com.yunhuwifi.util.CustomDialog;
import com.yunhuwifi.util.DbUtility;
import com.yunhuwifi.view.PasswordEditText;

import android.app.Dialog;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class RouterLoginActivity extends HeaderActivity {

	private PasswordEditText txtPassword;
	private Button btnLogin;
	private Dialog loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_router_login);
		this.setHeaderText("管理密码登陆");
		this.setLeftImageVisible(false);

		txtPassword = (PasswordEditText) findViewById(R.id.txtPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);

		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String password = txtPassword.getText().toString();
				doLogin(password);
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();

		// 尝试自动登录
		final WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			String macAddr = wifiManager.getConnectionInfo().getBSSID();
			RouterLocalAccount localAccount = DbUtility.findByWhere(
					getApplicationContext(), RouterLocalAccount.class,
					"macaddress='" + macAddr + "'");
			if (localAccount != null) {
				// 自动登录
				String ipAddress = Formatter.formatIpAddress(wifiManager
						.getDhcpInfo().serverAddress);

				loginContext(localAccount.getUsername(),
						localAccount.getPassword(), ipAddress,
						RouterModulePassport.DEFAULT_PORT, macAddr);
			}
		}
	}

	private void loginContext(String username, String password, String ipaddr,
			int port, final String macAddress) {

		loading = CustomDialog.createLoadingDialog(RouterLoginActivity.this,
				"正在登录，请稍后...");
		loading.show();

		RouterContext.login(username, password, ipaddr, port,
				new JsonCallBack<RouterContext>(RouterContext.class) {

					@Override
					public void onJsonSuccess(RouterContext t) {
						loading.dismiss();
						getApplicationContext().setRouterContext(t);
						saveLoginAccount(macAddress, t.getUsername(),
								t.getPassword());
						setResult(ACTIVITY_RESULT_SUCCESS);
						finish();
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						loading.dismiss();
						showToast(strMsg + "(" + errorNo + ")",
								Toast.LENGTH_SHORT);
					}
				});
	}

	private void doLogin(final String password) {
		final WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			String ipAddress = Formatter.formatIpAddress(wifiManager
					.getDhcpInfo().serverAddress);
			String macAddres = wifiManager.getConnectionInfo().getBSSID();

			loginContext(RouterModulePassport.DEFAULT_USER, password,
					ipAddress, RouterModulePassport.DEFAULT_PORT, macAddres);
		} else {
			showToast("请连接Wifi", Toast.LENGTH_SHORT);
		}
	}

	private void saveLoginAccount(String bssid, String username, String password) {
		RouterLocalAccount account = DbUtility.findByWhere(
				getApplicationContext(), RouterLocalAccount.class,
				"macAddress='" + bssid + "'");

		if (account == null) {
			account = new RouterLocalAccount();
			account.setMacAddress(bssid);
			account.setUsername(username);
			account.setPassword(password);
			DbUtility.save(getApplicationContext(), account);
		} else {
			account.setMacAddress(bssid);
			account.setUsername(username);
			account.setPassword(password);
			DbUtility.update(getApplicationContext(), account);
		}
	}

}
