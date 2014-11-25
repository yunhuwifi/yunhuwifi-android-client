package com.yunhuwifi.activity;

import com.foxrouter.api.RouterModuleDevice;
import com.foxrouter.api.RouterResponseResult;
import com.foxrouter.api.model.RouterDevice;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.handlers.JsonCallBack;
import com.yunhuwifi.util.CustomDialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DeviceLongPressActivity extends BaseActivity implements
		OnClickListener {
	private RouterDevice device;
	private RelativeLayout laychangeremark, laydisconnect,layaddblack;
	private String longDevice;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_longpress_box);
		laychangeremark = (RelativeLayout) findViewById(R.id.laychangeremark);
		laydisconnect = (RelativeLayout) findViewById(R.id.laydisconnect);
		layaddblack = (RelativeLayout) findViewById(R.id.layaddblack);
		laychangeremark.setOnClickListener(this);
		laydisconnect.setOnClickListener(this);
		layaddblack.setOnClickListener(this);
		this.device = (RouterDevice) getIntent().getExtras().getSerializable(
				"device");
		this.longDevice=getIntent().getExtras().getString(longDevice);
	}

	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.laychangeremark:
			Intent t = new Intent(DeviceLongPressActivity.this,
					DeviceRemarkActivity.class);
			Bundle b = new Bundle();
			b.putSerializable("device", device);
			t.putExtras(b);
			startActivity(t);
			finish();
			break;

		case R.id.laydisconnect:
			disconnect();
			setResult(RESULT_OK, (new Intent()).setAction(longDevice));
			break;
		default:
			break;
		}

	}

	private  void disconnect() {
		final Dialog dialog=CustomDialog.createLoadingDialog(getApplicationContext(), "正在断开，请稍后...");
		dialog.show();
		RouterModuleDevice disdevice = new RouterModuleDevice(
				getApplicationContext().getRouterContext());
		disdevice.disconnect(device.getMacaddr(),
				new JsonCallBack<RouterResponseResult>(
						RouterResponseResult.class) {

					@Override
					public void onJsonSuccess(RouterResponseResult t) {
						dialog.dismiss();
						if (t.isResult()) {
							showToast("断开成功", Toast.LENGTH_SHORT);
							finish();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						dialog.dismiss();
						showToast(strMsg, Toast.LENGTH_SHORT);
					}
				});

	}
	

	
}
