package com.yunhuwifi.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestActivity extends BaseActivity implements OnClickListener{
	
	private Button btnTestNext;
	private Button btnTestPPOE;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testing);
		btnTestPPOE=(Button) findViewById(R.id.btnTestNext);
		btnTestNext=(Button) findViewById(R.id.btnTestPPOE);
		btnTestNext.setOnClickListener(this);
		btnTestPPOE.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.btnTestNext:
			startActivity(new Intent(TestActivity.this,TestWifiSetActivity.class));
			break;
		case R.id.btnTestPPOE:
			startActivity(new Intent(TestActivity.this,TestPPOEActivity.class));
			break;
		
		}
	}

}
