package com.yunhuwifi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestWifiSetActivity extends HeaderActivity implements OnClickListener{
	private Button btnWifiBack;
	private Button btnWifiNext;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_wifiset);
		this.btnLeft.setVisibility(View.VISIBLE);
		this.setHeaderText("WIFI设置");
		this.btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
				
			}
		});
		this.btnRight.setVisibility(View.GONE);
		btnWifiBack=(Button) findViewById(R.id.btnwifiback);
		btnWifiNext=(Button) findViewById(R.id.btnwifinext);
		btnWifiBack.setOnClickListener(this);
		btnWifiNext.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {

		switch(v.getId()){
		
		case R.id.btnwifinext:
			startActivity(new Intent(TestWifiSetActivity.this,TestOkActivity.class));
			break;
		case R.id.btnwifiback:
			finish();
			break;
			
		
		}
	}
}
