package com.yunhuwifi.activity;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.view.PasswordEditText;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ChangeYunhuPwdActivity extends BaseActivity implements OnClickListener {
	private PasswordEditText oldpwd, newpwd;
	private String oldpass, newpass;
	private Button header_btnLeft,header_btnRight;
	private TextView header_txtView;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changeyunhupwd);
		header_txtView=(TextView) findViewById(R.id.header_txtView);
		header_txtView.setText("修改密码");
		header_btnLeft=(Button) findViewById(R.id.header_btnLeft);
		header_btnRight=(Button) findViewById(R.id.header_btnRight);
		this.header_btnLeft.setOnClickListener(this);
		this.header_btnRight.setVisibility(View.INVISIBLE);
		findViewById(R.id.btnchangeuser).setOnClickListener(this);
		oldpwd = (PasswordEditText) findViewById(R.id.oldpwd);
		newpwd = (PasswordEditText) findViewById(R.id.newpwd);
		oldpass = oldpwd.getText().toString();
		newpass = newpwd.getText().toString();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnchangeuser: {
		}
			break;
		case R.id.header_btnLeft: {
			finish();
		}
			break;
		}
	}

}
