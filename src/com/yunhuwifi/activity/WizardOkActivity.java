package com.yunhuwifi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WizardOkActivity extends HeaderActivity implements OnClickListener{

	private Button btnbind,btnskip;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_wizard_ok);
		btnbind=(Button) findViewById(R.id.btnBindCount);
		btnskip=(Button) findViewById(R.id.btnSkip);
		this.setHeaderText("路由器配置");
		this.setLeftImageVisible(true);
		this.setRightImageVisible(false);
		this.ivLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		btnbind.setOnClickListener(this);
		btnskip.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnBindCount:
			startActivity(new Intent(WizardOkActivity.this,LoginActivity.class));
			finish();
			break;
		case R.id.btnSkip:
			startActivity(new Intent(WizardOkActivity.this,MainActivity.class));
			break;
		
		}
		
	}
	
	

}
