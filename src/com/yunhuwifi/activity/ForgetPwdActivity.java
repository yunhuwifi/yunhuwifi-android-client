package com.yunhuwifi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ForgetPwdActivity extends HeaderActivity implements OnClickListener {
	
	private Button btnbackpwd,btncode;
	private Chronography time;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_forgetpwd);
		time=new Chronography(60000,1000);
		this.setHeaderText("忘记密码");
		this.setLeftImageVisible(true);
		this.setRightImageVisible(false);
		this.ivLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnbackpwd=(Button) findViewById(R.id.btnbackpwd);
		btnbackpwd.setOnClickListener(this);
		btncode=(Button) findViewById(R.id.btncode);
		btncode.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnbackpwd:
			Intent i = new Intent(ForgetPwdActivity.this, SavePwdActivity.class);
			startActivity(i);
			break;

		case R.id.btncode:
			time.start();
			break;
			
		}

	}
	
	class Chronography extends CountDownTimer{
			public Chronography (long ms,long cd){
				super(ms, cd);
			}
	
			@Override
			public void onTick(long millisUntilFinished) {
				btncode.setClickable(true);
				btncode.setText(millisUntilFinished/1000+"s");
			}
	
			@Override
			public void onFinish() {
				btncode.setClickable(true);
				btncode.setText("重新验证");
			}
	}
}
