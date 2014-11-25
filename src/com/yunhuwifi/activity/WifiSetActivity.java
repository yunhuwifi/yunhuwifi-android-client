package com.yunhuwifi.activity;

import com.foxrouter.api.RouterModuleWifi;
import com.foxrouter.api.RouterResponseResult;
import com.foxrouter.api.RouterResponseWifiList;
import com.foxrouter.api.model.RouterWifiDetails;
import com.foxrouter.api.model.RouterWifiViewModel;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.handlers.JsonCallBack;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class WifiSetActivity extends BaseActivity implements
		OnCheckedChangeListener, OnClickListener {
	private ToggleButton wifiswitch;
	private EditText txtSsid;
	private EditText txtPassword;
	private Button btnWifi, header_btnLeft, header_btnRight;
	private TextView txtEncryption, header_txtView,txthigh,txtlow,txtmiddle;
	private RouterWifiViewModel model;
	private RouterModuleWifi wifiModule;
	private LinearLayout safe;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_setting);
		initView();
		Bundle bundle = getIntent().getExtras();
		model = (RouterWifiViewModel) bundle.getSerializable("model");
		this.wifiModule = new RouterModuleWifi(getApplicationContext()
				.getRouterContext());
		wifiDetails();
	}

	private void initView() {
		header_txtView = (TextView) findViewById(R.id.header_txtView);
		header_txtView.setText("无线设置");
		header_btnLeft = (Button) findViewById(R.id.header_btnLeft);
		header_btnRight = (Button) findViewById(R.id.header_btnRight);
		header_btnLeft.setOnClickListener(this);
		header_btnRight.setVisibility(View.INVISIBLE);
		wifiswitch = (ToggleButton) findViewById(R.id.wifiswitch); // 获取到控件
		wifiswitch.setOnCheckedChangeListener(this);// 添加监听事件
		txtSsid = (EditText) findViewById(R.id.txtSsid);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		btnWifi = (Button) findViewById(R.id.btnWifi);
		btnWifi.setOnClickListener(this);
		txtEncryption = (TextView) findViewById(R.id.txtEncryption);
		safe=(LinearLayout) findViewById(R.id.safe);
		safe.setOnClickListener(this);
		txtlow=(TextView) findViewById(R.id.txtlow);
		txthigh=(TextView) findViewById(R.id.txthigh);
		txtmiddle=(TextView) findViewById(R.id.txtmiddle);
	}

	private void popupEncrayption() {
		final Dialog dlg = new Dialog(this);
		View dlgView = View.inflate(this, R.layout.encryption_box, null);
		View layhigh = dlgView.findViewById(R.id.layhigh); 
		layhigh.setTag(dlg);
		layhigh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dlg.dismiss();
				txtEncryption.setText("强加密");
			}
		});
		View laymindle = dlgView.findViewById(R.id.laymindle);
		laymindle.setTag(dlg);
		laymindle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				txtEncryption.setText("混合加密");
				dlg.dismiss();
			}
		});
		View laylow = dlgView.findViewById(R.id.laylow);
		laylow.setTag(dlg); 
		laylow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dlg.dismiss();
				txtEncryption.setText("无加密");
			}
		});
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(dlgView);
		dlg.show();
	}
	public void wifiDetails() {
		wifiModule.wifiDetails(model.getNetid(),
				new JsonCallBack<RouterResponseWifiList>(
						RouterResponseWifiList.class) {

					@Override
					public void onJsonSuccess(RouterResponseWifiList t) {
						if (t.getResult() != null && !t.getResult().equals("")) {
							txtSsid.setText(model.getSsid());
							wifiswitch.setChecked(model.isEnable());
							txtPassword.setText(model.getKey());
							txtEncryption.setText(model.getEncryption());
						}

					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						showToast(strMsg, Toast.LENGTH_SHORT);
					}
				});
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			openwifi();
		} else {
			closewifi();
		}
	}

	 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnWifi:
			editwifi();
			break;
		case R.id.header_btnLeft:
			finish();
			break;
		case R.id.safe:
			popupEncrayption();
			break;
			
		}
		
	}
	
	public void editwifi(){
		RouterWifiDetails edit = new RouterWifiDetails();
		wifiModule.editWifi(edit, new JsonCallBack<RouterResponseResult>(
				RouterResponseResult.class) {

			@Override
			public void onJsonSuccess(RouterResponseResult t) {
				if (t.isResult()) {
					Toast.makeText(WifiSetActivity.this, "保存成功",
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(WifiSetActivity.this, "保存失败",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				showToast(strMsg, Toast.LENGTH_SHORT);
			}
		});
	}

	public void closewifi() {
		wifiModule.closeWifi(model.getNetid(),
				new JsonCallBack<RouterResponseResult>(
						RouterResponseResult.class) {

					@Override
					public void onJsonSuccess(RouterResponseResult t) {
						if (t.isResult()) {
							wifiswitch.setChecked(model.isHidden());
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

	public void openwifi() {
		wifiModule.openWifi(model.getNetid(),
				new JsonCallBack<RouterResponseResult>(
						RouterResponseResult.class) {

					@Override
					public void onJsonSuccess(RouterResponseResult t) {
						if (t.isResult()) {
							wifiswitch.setChecked(model.isEnable());
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
}
