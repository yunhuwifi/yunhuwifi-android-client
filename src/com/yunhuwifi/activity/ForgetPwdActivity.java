package com.yunhuwifi.activity;

import java.util.HashMap;

import com.google.gson.Gson;
import com.yunhuwifi.util.CustomDialog;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPwdActivity extends HeaderActivity implements OnClickListener {
	
	private final static String DEFAULT_COUNTRY = "86";
	private Button btnbackpwd,btncode;
	private Chronography time;
	private Dialog loading;
	private TextView tvcaptcha,tvnum;
	private String number,code;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_forgetpwd);
		time=new Chronography(60000,1000);
		tvcaptcha=(TextView) findViewById(R.id.captcha);
		tvnum=(TextView) findViewById(R.id.number);
		this.setHeaderText("忘记密码");
		this.setLeftImageVisible(true);
		this.setRightImageVisible(false);
		this.ivLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnbackpwd=(Button) findViewById(R.id.btnbackpwd);
		btnbackpwd.setOnClickListener(this);
		btncode=(Button) findViewById(R.id.btncode);
		btncode.setOnClickListener(this);

	}
	


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnbackpwd:
			 code = tvcaptcha.getText().toString();
			 number=tvnum.getText().toString();
			if (code.length()>=4) {
				SMSSDK.registerEventHandler(submitCodeHandler);

				loading =CustomDialog.createLoadingDialog(
						ForgetPwdActivity.this, 
						"正在校验手机验证码,请稍后...");
				loading.show();

				SMSSDK.submitVerificationCode(DEFAULT_COUNTRY, number, code);

			} else {
				showToast("请输入正确的验证码。", Toast.LENGTH_SHORT);
			}
			break;

		case R.id.btncode:
			if(RegisterMobileActivity.judgeMobile(tvnum.getText().toString())){
				getCode();
			}else{
				showToast("请输入正确的手机号!", Toast.LENGTH_SHORT);
			}
			break;
			
		}

	}
	
	private EventHandler submitCodeHandler = new EventHandler() {
		public void afterEvent(int event, int result, Object data) {
			Message msg = codeHandler.obtainMessage();
			msg.arg1 = event;
			msg.arg2 = result;
			msg.obj = data;
			msg.sendToTarget();
		}
	};
	
	private Handler codeHandler = new Handler() {
		public void handleMessage(Message msg) {
			loading.dismiss();
			SMSSDK.unregisterEventHandler(submitCodeHandler);
			Log.d("event", msg.arg1 + "");
			Log.d("result", msg.arg2 + "");
			if (msg.arg2 == SMSSDK.RESULT_COMPLETE
					&& msg.arg1 == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
				
				number=tvnum.getText().toString();
				redirectCodeActivity(number);
				
				HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
				Gson gson = new Gson();
				String json = gson.toJson(result);
				Log.d("json", json);
				
			} else {
				showToast("验证码错误", Toast.LENGTH_SHORT);
			}
		}
	};
	
	public void getCode(){
	
				 number=tvnum.getText().toString();
				loading =CustomDialog.createLoadingDialog(
						ForgetPwdActivity.this, "正在发送验证码,请稍后");
				loading.show();
				
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						SMSSDK.getVerificationCode(DEFAULT_COUNTRY, number);
					}
				};
				codeHandler.post(runnable);
				time.start();
			
		}
	class Chronography extends CountDownTimer{
			public Chronography (long ms,long cd){
				super(ms, cd);
			}
	
			@Override
			public void onTick(long millisUntilFinished) {
				btncode.setClickable(false);
				btncode.setBackgroundResource(R.drawable.installing);
				btncode.setText(millisUntilFinished/1000+"s");
			}
	
			@Override
			public void onFinish() {
				btncode.setClickable(true);
				btncode.setText("重新验证");
			}
	}
	
	private void redirectCodeActivity(String phone) {
		Intent intent = new Intent(ForgetPwdActivity.this,
				SavePwdActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("country", DEFAULT_COUNTRY);
		bundle.putString("numer", number);
		intent.putExtras(bundle);
		startActivityForResult(intent, 1);
	}
}
