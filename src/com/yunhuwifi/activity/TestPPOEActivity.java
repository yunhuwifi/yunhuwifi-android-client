package com.yunhuwifi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestPPOEActivity extends HeaderActivity implements OnClickListener{

	private Button btnppoe;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_pppoe_setting);
		btnppoe=(Button) findViewById(R.id.btnpppoe);
		btnppoe.setOnClickListener(this);
		this.btnLeft.setVisibility(View.VISIBLE);
		this.setHeaderText("宽带拨号");
		this.btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
				
			}
		});
		this.btnRight.setVisibility(View.GONE);
		
	}
	@Override
	public void onClick(View v) {

		if(v.getId()==R.id.btnpppoe){
			startActivity(new Intent(TestPPOEActivity.this,TestWifiSetActivity.class));
		}
	}
}
