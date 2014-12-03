package com.yunhuwifi.activity;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.view.PasswordEditText;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SavePwdActivity extends Activity implements OnClickListener{
	private TextView header_txtView;
	private ImageView header_ivLeft,header_ivRight;
	private Button savepwd;
	private PasswordEditText newpwd,oldpwd;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_savepwd);
		 header_txtView = (TextView) findViewById(R.id.header_txtView);
		header_txtView.setText("忘记密码");
		findViewById(R.id.savepwd).setOnClickListener(this);
		header_ivLeft=(ImageView) findViewById(R.id.header_ivLeft);
		header_ivLeft.setOnClickListener(this);
		header_ivRight=(ImageView) findViewById(R.id.header_ivRight);
		header_ivRight.setVisibility(View.INVISIBLE);
		savepwd=(Button) findViewById(R.id.savepwd);
		savepwd.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.header_ivLeft:
			finish();
			break;
		}
		
	}

}
