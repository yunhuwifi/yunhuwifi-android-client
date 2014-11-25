package com.yunhuwifi.activity;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.view.PasswordEditText;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SavePwdActivity extends Activity implements OnClickListener{
	private TextView header_txtView;
	private Button header_btnLeft,header_btnRight,savepwd;
	private PasswordEditText newpwd,oldpwd;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_savepwd);
		 header_txtView = (TextView) findViewById(R.id.header_txtView);
		header_txtView.setText("忘记密码");
		findViewById(R.id.savepwd).setOnClickListener(this);
		header_btnLeft=(Button) findViewById(R.id.header_btnLeft);
		header_btnLeft.setOnClickListener(this);
		header_btnRight=(Button) findViewById(R.id.header_btnRight);
		header_btnRight.setVisibility(View.INVISIBLE);
		savepwd=(Button) findViewById(R.id.savepwd);
		savepwd.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.header_btnLeft:
			finish();
			break;
		}
		
	}

}
