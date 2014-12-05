package com.yunhuwifi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ForgetPwdActivity extends HeaderActivity implements OnClickListener {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_forgetpwd);
		
		this.setHeaderText("忘记密码");
		this.setLeftImageVisible(true);
		this.setRightImageVisible(false);
		this.ivLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		findViewById(R.id.backpwd).setOnClickListener(this);
		findViewById(R.id.txtchange).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backpwd:
			Intent i = new Intent(ForgetPwdActivity.this, SavePwdActivity.class);
			startActivity(i);
			break;

		case R.id.txtchange:
			break;
			
		}

	}
}
