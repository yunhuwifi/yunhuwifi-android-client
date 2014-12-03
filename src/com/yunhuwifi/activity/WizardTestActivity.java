package com.yunhuwifi.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WizardTestActivity extends HeaderActivity implements OnClickListener{
	
	private Button btnTestNext; 
	private TextView tvTestPPOE;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_wizard_test);
		tvTestPPOE=(TextView) findViewById(R.id.tvTestNext);
		btnTestNext=(Button) findViewById(R.id.btnTestPPOE);
		this.setHeaderText("检测上网方式");
		this.setRightImageVisible(false);
		this.setLeftImageVisible(true);
		this.ivLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnTestNext.setOnClickListener(this);
		tvTestPPOE.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.tvTestNext:
			startActivity(new Intent(WizardTestActivity.this,WizardWifiSetActivity.class));
			break;
		case R.id.btnTestPPOE:
			startActivity(new Intent(WizardTestActivity.this,WizardPPOEActivity.class));
			break;
		
		}
	}

}
