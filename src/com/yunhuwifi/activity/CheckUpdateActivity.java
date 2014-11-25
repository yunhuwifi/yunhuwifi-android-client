package com.yunhuwifi.activity;

import com.foxrouter.api.RouterModuleSystem;
import com.foxrouter.api.RouterResponseRouterInfo;
import com.yunhuwifi.activity.R;
import com.umeng.update.UmengUpdateAgent;
import com.yunhuwifi.handlers.JsonCallBack;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class CheckUpdateActivity extends BaseActivity implements
		OnClickListener {
	private TextView hardware, firmware;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_updated);
		TextView headerText = (TextView) findViewById(R.id.tvHeaderText);
		headerText.setText("版本升级");
		hardware = (TextView) findViewById(R.id.hardware);
		firmware = (TextView) findViewById(R.id.firmware);
		findViewById(R.id.checkUpdate).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						UmengUpdateAgent.setDefault();
						UmengUpdateAgent.forceUpdate(getApplication());
					}
				});
		initCheck();
	}

	private void initCheck() {
		RouterModuleSystem sys = new RouterModuleSystem(getApplicationContext()
				.getRouterContext());
		sys.info(new JsonCallBack<RouterResponseRouterInfo>(
				RouterResponseRouterInfo.class) {

			@Override
			public void onJsonSuccess(RouterResponseRouterInfo t) {
				if (t.getError() != null && t.getResult() != null) {
					hardware.setText(t.getResult().getHardwareVersion());
					firmware.setText(t.getResult().getFirmwareVersion());
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				Toast.makeText(CheckUpdateActivity.this, strMsg,
						Toast.LENGTH_SHORT).show();
			}

		});

	}

	@Override
	public void onClick(View v) {
		/*
		 * Intent iw = new Intent(CheckUpdateActivity.this,
		 * VersionUpdateActivity.class); startActivity(iw);
		 */
		// UmengUpdateAgent.setDefault();
		// UmengUpdateAgent.forceUpdate(this);

	}

	public void goback(View v) {
		finish();

	}

}
