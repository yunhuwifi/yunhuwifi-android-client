package com.yunhuwifi.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BaiDuLoginActivity extends BaseActivity implements OnClickListener{
	private Button btnbaidu;
	private TextView tvbaiduuser,tvbaidupwd;
	private String user,pwd;
	private ImageView ivback;
	public void onCreate(Bundle SaveedInstanceState){
		super.onCreate(SaveedInstanceState);
		setContentView(R.layout.activity_baidulogin);
		btnbaidu=(Button) findViewById(R.id.btnbaidu);
		tvbaiduuser=(TextView) findViewById(R.id.tvbaiduuser);
		tvbaidupwd=(TextView) findViewById(R.id.tvbaidupwd);
		ivback=(ImageView) findViewById(R.id.ivback);
		ivback.setOnClickListener(this);
		btnbaidu.setOnClickListener(this);
		user=tvbaiduuser.getText().toString();
		pwd=tvbaidupwd.getText().toString();
		
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.ivback:
			this.finish();
			break;
		}
		
	}

}
