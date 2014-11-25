package com.yunhuwifi.activity;

import com.yunhuwifi.activity.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountManageActivity extends BaseActivity implements
		OnClickListener {
	private TextView accountname;
	private ImageView nameicon;
	private TextView header_txtView;
	private RelativeLayout layoutchange;
	private Button btnexit ,header_btnLeft,header_btnRight;
	private String name ;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_manage);
		header_txtView=(TextView) findViewById(R.id.header_txtView);
		header_txtView.setText("账号管理");
		header_btnLeft=(Button) findViewById(R.id.header_btnLeft);
		header_btnRight=(Button) findViewById(R.id.header_btnRight);
		header_btnLeft.setOnClickListener(this);
		this.header_btnRight.setVisibility(View.INVISIBLE);
		accountname = (TextView) findViewById(R.id.accountname);
		nameicon = (ImageView) findViewById(R.id.nameicon);
		layoutchange = (RelativeLayout) findViewById(R.id.layoutchange);
		 btnexit = (Button) findViewById(R.id.btnexit);
		layoutchange.setOnClickListener(this);
		btnexit.setOnClickListener(this);
		name=(String) getIntent().getExtras().getSerializable(name);
		accountname.setText(name);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.layoutchange: {
			startActivity(new Intent(AccountManageActivity.this,
					ChangeYunhuPwdActivity.class));
		}
			break;

		case R.id.btnexit: {
			Intent intent = new Intent();
			intent.setClass(AccountManageActivity.this, ExitActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.header_btnLeft: {
			this.finish();
		}
			break;
		}
	}

}
