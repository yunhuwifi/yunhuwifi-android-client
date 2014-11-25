package com.yunhuwifi.activity;

import java.util.regex.Pattern;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.util.CustomDialog;
import com.yunhuwifi.view.ClearEditText;
import com.yunhuwifi.view.PasswordEditText;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterMobileActivity extends HeaderActivity {

	private final static String DEFAULT_COUNTRY = "86";

	private ClearEditText txtPhone;
	private PasswordEditText txtPassword;
	private Button btnNext,btnLeft;
	private CheckBox cbagreen;
	private Dialog loading;
	private TextView txtlink;
	private Handler verifyHandler = new Handler() {
		public void handleMessage(Message msg) {
			SMSSDK.unregisterEventHandler(verifyEventHandler);
			loading.dismiss();
			if (msg.arg2 == SMSSDK.RESULT_COMPLETE
					&& msg.arg1 == SMSSDK.EVENT_GET_VERIFICATION_CODE) {

				String phone = txtPhone.getText().toString();
				String password = txtPassword.getText().toString();
				redirectVerifyActivity(phone, password);

			} else {
				Toast.makeText(RegisterMobileActivity.this, "验证码发送失败",
						Toast.LENGTH_SHORT).show();
			}
		}
	};
	private EventHandler verifyEventHandler = new EventHandler() {
		public void afterEvent(int event, int result, Object data) {
			Message msg = verifyHandler.obtainMessage();
			msg.arg1 = event;
			msg.arg2 = result;
			msg.obj = data;
			msg.sendToTarget();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_register_mobile);
		super.setHeaderText("注册");
		txtlink=(TextView) findViewById(R.id.txtlink);
//		 txtlink.setText(Html.fromHtml("我已阅读并同意" +"<a href=\"http://www.baidu.com\">link</a>" + 
//				 "<<云狐路由器协议>>")); 
		txtlink.setMovementMethod(LinkMovementMethod.getInstance());
		txtPhone = (ClearEditText) findViewById(R.id.txtPhone);
		txtPassword = (PasswordEditText) findViewById(R.id.txtPassword);
		btnNext = (Button) findViewById(R.id.btnNext);
		cbagreen=(CheckBox) findViewById(R.id.cbagree);
		setLeftButtonVisible(true);
		btnLeft=(Button) findViewById(R.id.header_btnLeft);
		btnLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		cbagreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                if(isChecked){ 
                    btnNext.setVisibility(View.VISIBLE);
                }else{ 
                	showToast( "请同意云狐WiFi协议", Toast.LENGTH_SHORT);
                } 
            } 
        }); 

		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String phone = txtPhone.getText().toString();

				if (verifyInput()&&cbagreen.isChecked()) {
					loading =CustomDialog.createLoadingDialog(
							RegisterMobileActivity.this, "正在发送验证码,请稍后");
					loading.show();

					Runnable runnable = new Runnable() {
						@Override
						public void run() {
							SMSSDK.registerEventHandler(verifyEventHandler);
							SMSSDK.getVerificationCode(DEFAULT_COUNTRY, phone);
						}
					};

					verifyHandler.post(runnable);
				}
			}
		});
	}

	public static boolean judgeMobile(String phone) {
		String regex = "^1[3|5|7|8|][0-9]{9}$";
		return Pattern.matches(regex, phone);
	}

	private boolean verifyInput() {
		String phone = txtPhone.getText().toString();

		String password = txtPassword.getText().toString();

		if (!judgeMobile(phone)) {
			txtPhone.setShakeAnimation();
			showToast("请输入正确的手机号码", Toast.LENGTH_SHORT);
			return false;
		}
		if (password.length() < 6 || password.length() > 18) {
			txtPassword.setShakeAnimation();
			showToast("密码必须为6-18位字符", Toast.LENGTH_SHORT);
			return false;
		}
		return true;
	}

	private void redirectVerifyActivity(String phone, String password) {
		Intent intent = new Intent(RegisterMobileActivity.this,
				RegisterMobileVerifyActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("country", DEFAULT_COUNTRY);
		bundle.putString("phone", phone);
		bundle.putString("password", password);
		intent.putExtras(bundle);
		startActivityForResult(intent, 1);
	}
}
