package com.yunhuwifi.activity;

import com.foxrouter.api.RouterModulePassport;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.RouterContext;
import com.yunhuwifi.handlers.JsonCallBack;
import com.yunhuwifi.models.RouterLocalAccount;
import com.yunhuwifi.util.CustomDialog;
import com.yunhuwifi.util.DbUtility;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NotConnectActivity extends BaseActivity implements OnClickListener {
	private Button btnreconnect, yunhuuserlogin;
	private final static int LOGIN_REQUEST_CODE = 1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notconnect);
		yunhuuserlogin = (Button) findViewById(R.id.yunhuuserlogin);
		btnreconnect = (Button) findViewById(R.id.btnreconnect);
		yunhuuserlogin.setOnClickListener(this);
		btnreconnect.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.yunhuuserlogin:
			startActivity(new Intent(NotConnectActivity.this,
					LoginActivity.class));
			break;
		case R.id.btnreconnect:
			if (android.os.Build.VERSION.SDK_INT > 10) {
				Intent intent = new Intent(
						android.provider.Settings.ACTION_WIFI_SETTINGS);
				startActivityForResult(intent, LOGIN_REQUEST_CODE);
			} else {
				Intent intent = new Intent(
						android.provider.Settings.ACTION_WIRELESS_SETTINGS);
				startActivityForResult(intent, LOGIN_REQUEST_CODE);
			}
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("request code", requestCode + "");
		Log.d("resultCode code", resultCode + "");
		if (requestCode == LOGIN_REQUEST_CODE
				&& resultCode == ACTIVITY_RESULT_FAILURE) {
			detectWifi();
		}
	}

	private void detectWifi() {
		WifiManager wifiManager = (WifiManager) this.getApplicationContext()
				.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			final Dialog dialog = CustomDialog.createLoadingDialog(
					NotConnectActivity.this, "");

			dialog.show();

			String macAddress = wifiManager.getConnectionInfo().getBSSID();
			String ipAddress = Formatter.formatIpAddress(wifiManager
					.getDhcpInfo().serverAddress);
			RouterLocalAccount account = DbUtility.findByWhere(
					getApplicationContext(), RouterLocalAccount.class,
					"macAddress='" + macAddress + "'");

			if (account != null) {
				RouterContext.login(account.getUsername(),
						account.getPassword(), ipAddress,
						RouterModulePassport.DEFAULT_PORT,
						new JsonCallBack<RouterContext>(RouterContext.class) {

							@Override
							public void onJsonSuccess(RouterContext t) {
								getApplicationContext().setRouterContext(t);
								Intent intent = new Intent(
										NotConnectActivity.this,
										MainActivity.class);
								startActivity(intent);
								finish();
							}

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								dialog.dismiss();
								startActivity(new Intent(
										NotConnectActivity.this,
										RouterLoginActivity.class));
								finish();
							}
						});
			} else {
				dialog.dismiss();
				startActivity(new Intent(NotConnectActivity.this,
						RouterLoginActivity.class));
				finish();
			}
		}
	}
}
