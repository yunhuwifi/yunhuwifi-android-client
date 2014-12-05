package com.yunhuwifi.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foxrouter.api.RouterModulePassport;
import com.foxrouter.api.RouterResponseLogin;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.handlers.JsonCallBack;
import com.yunhuwifi.view.PasswordEditText;

public class ChangeRouterPwdActivty extends HeaderActivity implements
		OnClickListener {
	private PasswordEditText oldpwd, newpwd;
	private String oldpass, newpass;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_changerouterpwd);
		this.setHeaderText("修改管理密码");
		this.setLeftImageVisible(true);
		this.setRightImageVisible(false);
		this.ivLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
		case R.id.header_ivLeft: {
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
