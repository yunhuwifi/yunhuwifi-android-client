package com.yunhuwifi.activity;

import com.foxrouter.api.RouterModuleDevice;
import com.foxrouter.api.RouterResponseResult;
import com.foxrouter.api.model.RouterDevice;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.handlers.JsonCallBack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceDetailActivity extends BaseActivity implements
		OnClickListener {

	private RouterDevice device;
	private TextView devicename, ipadress, macadress, brand;
	private Button alterdisconnect, btnRemark;
	private String did;
	static final private int RESULT=0;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_detail);
		this.btnRemark = (Button) findViewById(R.id.btnRemark);
		this.btnRemark.setOnClickListener(this);
		alterdisconnect = (Button) findViewById(R.id.alterdisconnect);
		alterdisconnect.setOnClickListener(this);
		devicename = (TextView) findViewById(R.id.devicename);
		ipadress = (TextView) findViewById(R.id.ipadress);
		macadress = (TextView) findViewById(R.id.macadress);
		brand = (TextView) findViewById(R.id.brand);
		initdetails();
	}

	private void initdetails() {
		this.device = (RouterDevice) getIntent().getExtras().getSerializable(
				"device");
		this.did= getIntent().getExtras().getString(did);
		devicename.setText(device.getRemark()!=null?device.getRemark():device.getHostname());
		ipadress.setText(device.getIpaddr());
		macadress.setText(device.getMacaddr());
		brand.setText(device.getBrand());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRemark:
			setResult(RESULT_OK,(new Intent()).setAction(did)); 
			Intent intent = new Intent(DeviceDetailActivity.this,
					DeviceRemarkActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("device", this.device);
			  bundle.putString("detail", DeviceDetailActivity.this.toString());
			intent.putExtras(bundle);
			startActivityForResult(intent, RESULT);
			break;
		case R.id.alterdisconnect:
			disconnect();
			break;
		}
	}

	public void disconnect() {
		RouterModuleDevice disdevice = new RouterModuleDevice(
				getApplicationContext().getRouterContext());
		disdevice.disconnect(device.getMacaddr(),
				new JsonCallBack<RouterResponseResult>(
						RouterResponseResult.class) {

					@Override
					public void onJsonSuccess(RouterResponseResult t) {
						if (t.isResult()) {
							showToast("断开成功", Toast.LENGTH_SHORT);
							finish();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						showToast(strMsg, Toast.LENGTH_SHORT);
					}
				});

	}
	   protected void onActivityResult(int requestCode, int resultCode, Intent data) {   
	    if(requestCode == RESULT)
	    {   
	     if(resultCode == RESULT_OK)
	      {   
	    	initdetails();
	      } 
	       
	    } 
	   }
	
}
