package com.yunhuwifi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.yunhuwifi.view.PasswordEditText;

public class WizardManagePwdSetActivity extends HeaderActivity implements OnClickListener{

		private PasswordEditText txtPassword;
		private Button btnLogin,btnback;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentLayout(R.layout.activity_router_login);
			this.setHeaderText("设置路由器管理密码");
			this.setLeftImageVisible(true);
			this.setRightImageVisible(false);
			this.ivLeft.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			txtPassword = (PasswordEditText) findViewById(R.id.txtPassword);
			btnLogin = (Button) findViewById(R.id.btnLogin);
			btnLogin.setBackgroundResource(R.drawable.btnnextbg);
			btnLogin.setText("下一步");
			btnback=(Button) findViewById(R.id.btnback);
			btnback.setVisibility(View.VISIBLE);
			btnback.setOnClickListener(this);
			btnLogin.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.btnLogin:
				startActivity(new Intent(WizardManagePwdSetActivity.this,WizardOkActivity.class));
				break;
			case R.id.btnback:
				 finish();
				break;
				
			}
			
		}
	
}
