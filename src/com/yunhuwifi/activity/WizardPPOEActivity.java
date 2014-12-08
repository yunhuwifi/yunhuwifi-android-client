package com.yunhuwifi.activity;

import com.yunhuwifi.view.ClearEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WizardPPOEActivity extends HeaderActivity implements OnClickListener{
	
		private Button btnppoe,btnppoeback;
		private ClearEditText wanuser,wanpwd;
		protected void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentLayout(R.layout.fragment_pppoe_setting);
			this.setHeaderText("宽带拨号");
			this.setLeftImageVisible(true);
			this.setRightImageVisible(false);
			this.ivLeft.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			wanuser=(ClearEditText) findViewById(R.id.wanUser);
			wanpwd=(ClearEditText) findViewById(R.id.wanPwd);
			btnppoe=(Button) findViewById(R.id.btnpppoe);
			btnppoeback=(Button) findViewById(R.id.btnppoeback);
			btnppoe.setBackgroundResource(R.drawable.btnnextbg);
			btnppoe.setText("下一步");
			btnppoe.setOnClickListener(this);
			btnppoeback.setVisibility(View.VISIBLE);
			btnppoeback.setOnClickListener(this);
		}
		@Override
		public void onClick(View v) {
			switch(v.getId()){
		
			case R.id.btnpppoe:
				startActivity(new Intent(WizardPPOEActivity.this,WizardWifiSetActivity.class));
			case R.id.btnppoeback:
				finish();
			
		}
	}
}
