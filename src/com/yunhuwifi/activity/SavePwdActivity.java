package com.yunhuwifi.activity;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.view.PasswordEditText;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SavePwdActivity extends HeaderActivity implements OnClickListener{
	private Button savepwd;
	private PasswordEditText newpwd,oldpwd;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_savepwd);
		this.setHeaderText("保存密码");
		this.setLeftImageVisible(true);
		this.setRightImageVisible(false);
		this.ivLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		savepwd=(Button) findViewById(R.id.savepwd);
		savepwd.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		
	}


}
