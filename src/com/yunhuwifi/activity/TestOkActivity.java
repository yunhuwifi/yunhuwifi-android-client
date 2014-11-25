package com.yunhuwifi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestOkActivity extends BaseActivity implements OnClickListener{

	private Button btnbind,btncancel;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_ok);
		btnbind=(Button) findViewById(R.id.btnBindCount);
//		btncancel=(Button) findViewById(R.id.btnCancelNext);
		btnbind.setOnClickListener(this);
//		btncancel.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnBind:
			startActivity(new Intent(TestOkActivity.this,RouterLoginActivity.class));
			finish();
			break;
		/*case R.id.btnCancelNext:
			startActivity(new Intent(TestOkActivity.this,MainActivity.class));
			break;*/
		
		}
		
	}
	
	

}
