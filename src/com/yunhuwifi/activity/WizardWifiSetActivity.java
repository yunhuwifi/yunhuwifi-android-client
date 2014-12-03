package com.yunhuwifi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WizardWifiSetActivity extends HeaderActivity implements OnClickListener{
	private Button btnWifiBack;
	private Button btnWifiNext;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_wizard_wifiset);
		this.setLeftImageVisible(true);
		this.setHeaderText("WIFI设置");
		this.setLeftImageVisible(true);
		this.setRightImageVisible(false);
		this.ivLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
				
			}
		});
		this.setRightImageVisible(false);
		btnWifiBack=(Button) findViewById(R.id.btnwifiback);
		btnWifiNext=(Button) findViewById(R.id.btnwifinext);
		btnWifiBack.setOnClickListener(this);
		btnWifiNext.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {

		switch(v.getId()){
		
		case R.id.btnwifinext:
			startActivity(new Intent(WizardWifiSetActivity.this,WizardManagePwdSetActivity.class));
			break;
		case R.id.btnwifiback:
			finish();
			break;
			
		
		}
	}
}
