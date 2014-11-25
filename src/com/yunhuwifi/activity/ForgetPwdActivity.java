package com.yunhuwifi.activity;

import com.yunhuwifi.activity.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ForgetPwdActivity extends BaseActivity implements OnClickListener {
	private TextView header_txtView;
	private Button header_btnLeft,header_btnRight;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpwd);
		header_txtView=(TextView) findViewById(R.id.header_txtView);
		header_txtView.setText("忘记密码");
		header_btnLeft=(Button) findViewById(R.id.header_btnLeft);
		header_btnRight=(Button) findViewById(R.id.header_btnRight);
		header_btnLeft.setOnClickListener(this);
		header_btnRight.setVisibility(View.INVISIBLE);
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
			
		case R.id.header_btnLeft:
			finish();
			break;
	}

	}
}
