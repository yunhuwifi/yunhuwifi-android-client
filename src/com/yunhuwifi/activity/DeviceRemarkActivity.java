package com.yunhuwifi.activity;

import com.foxrouter.api.RouterModuleDevice;
import com.foxrouter.api.RouterResponseDeviceRemark;
import com.foxrouter.api.model.RouterDevice;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.handlers.JsonCallBack;
import com.yunhuwifi.view.ClearEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DeviceRemarkActivity extends HeaderActivity {
	private Button btnComplete;
	private ClearEditText txtRemark;
	private RouterDevice device;
	private String detail;
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_device_remark);

		this.setHeaderText("修改备注");
		this.setLeftButtonVisible(true);

		this.txtRemark = (ClearEditText) findViewById(R.id.txtRemark);
		this.btnComplete = (Button) findViewById(R.id.btnComplete);
		this.detail=getIntent().getExtras().getString(detail);
		this.btnComplete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				comitRemark();
				setResult(RESULT_OK,(new Intent()).setAction(detail));
			}
		});

		this.device = (RouterDevice) getIntent().getExtras().get("device");

		if (this.device.getRemark() == null
				|| this.device.getRemark().length() == 0) {
			this.txtRemark.setText(this.device.getHostname());
		} else {
			this.txtRemark.setText(this.device.getRemark());
		}
	}

	private void comitRemark() {
		RouterModuleDevice moduleDevice = new RouterModuleDevice(
				getApplicationContext().getRouterContext());
		String remark = txtRemark.getText().toString();
		moduleDevice.remark(remark, device.getMacaddr(),
				new JsonCallBack<RouterResponseDeviceRemark>(
						RouterResponseDeviceRemark.class) {

					@Override
					public void onJsonSuccess(RouterResponseDeviceRemark t) {
						showToast("修改成功", Toast.LENGTH_SHORT);
						finish();
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						showToast("修改失败", Toast.LENGTH_SHORT);
					}
				});

	}
	
	
	
}
