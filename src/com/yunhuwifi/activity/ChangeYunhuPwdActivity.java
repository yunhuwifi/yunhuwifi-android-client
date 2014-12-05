package com.yunhuwifi.activity;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.view.PasswordEditText;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ChangeYunhuPwdActivity extends HeaderActivity implements OnClickListener {
	private PasswordEditText oldpwd, newpwd;
	private String oldpass, newpass;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_changeyunhupwd);
		this.setHeaderText("");
		this.setLeftImageVisible(true);
		this.setRightImageVisible(false);
		this.ivLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
		case R.id.header_ivLeft: {
			finish();
		}
			break;
		}
	}

}
