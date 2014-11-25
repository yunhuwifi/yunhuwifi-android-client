package com.yunhuwifi.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.foxrouter.api.RouterModulePassport;
import com.foxrouter.api.RouterResponseLogin;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.handlers.JsonCallBack;
import com.yunhuwifi.view.PasswordEditText;

public class ChangeRouterPwdActivty extends BaseActivity implements
		OnClickListener {
	private PasswordEditText oldpwd, newpwd;
	private String oldpass, newpass;
	private Button header_btnLeft, header_btnRight;
	private TextView header_txtView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changerouterpwd);
		header_txtView = (TextView) findViewById(R.id.header_txtView);
		header_txtView.setText("修改管理密码");
		header_btnLeft = (Button) findViewById(R.id.header_btnLeft);
		header_btnRight = (Button) findViewById(R.id.header_btnRight);
		this.header_btnLeft.setOnClickListener(this);
		this.header_btnRight.setVisibility(View.INVISIBLE);
		findViewById(R.id.btnchangeadmin).setOnClickListener(this);
		oldpwd = (PasswordEditText) findViewById(R.id.oldpwd);
		newpwd = (PasswordEditText) findViewById(R.id.newpwd);
		oldpass = oldpwd.getText().toString();
		newpass = newpwd.getText().toString();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnchangeadmin: {
			changePassword(oldpass, newpass);
		}
			break;
		case R.id.header_btnLeft: {
			finish();
		}
			break;
		}
	}

	private void changePassword(String oldpwd, String newpwd) {
		RouterModulePassport passport = new RouterModulePassport(
				getApplicationContext().getRouterContext());
		passport.changePassword(
				oldpwd,
				newpwd,
				new JsonCallBack<RouterResponseLogin>(RouterResponseLogin.class) {

					@Override
					public void onJsonSuccess(RouterResponseLogin resp) {
						Toast.makeText(ChangeRouterPwdActivty.this, "修改成功",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						Toast.makeText(ChangeRouterPwdActivty.this, "修改失败",
								Toast.LENGTH_SHORT).show();
					}

				});
	}

}
