package com.yunhuwifi.activity;

import java.util.HashMap;

import net.tsz.afinal.http.AjaxCallBack;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import com.google.gson.Gson;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.cloud.api.AccountService;
import com.yunhuwifi.cloud.api.models.ResponseRegister;
import com.yunhuwifi.util.CustomDialog;
import com.yunhuwifi.view.ClearEditText;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class RegisterMobileVerifyActivity extends HeaderActivity {

	private Dialog loading;
	private ClearEditText txtVerifyCode;
	private Button btnRegister;
	private String country;
	private String phone;
	private String password;

	private EventHandler submitCodeHandler = new EventHandler() {
		public void afterEvent(int event, int result, Object data) {
			Message msg = verifyHandler.obtainMessage();
			msg.arg1 = event;
			msg.arg2 = result;
			msg.obj = data;
			msg.sendToTarget();
		}
	};

	private Handler verifyHandler = new Handler() {
		public void handleMessage(Message msg) {
			loading.dismiss();
			SMSSDK.unregisterEventHandler(submitCodeHandler);
			Log.d("event", msg.arg1 + "");
			Log.d("result", msg.arg2 + "");
			if (msg.arg2 == SMSSDK.RESULT_COMPLETE
					&& msg.arg1 == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
				HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
				Gson gson = new Gson();
				String json = gson.toJson(result);
				Log.d("json", json);

				// 提交云端注册新账号
				registerAccount(phone, password);
			} else {
				showToast("验证码错误", Toast.LENGTH_SHORT);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_register_mobile_verify);
		setHeaderText("验证手机");

		this.initView();
	}

	private void initView() {
		this.txtVerifyCode = (ClearEditText) findViewById(R.id.txtVerifyCode);
		this.btnRegister = (Button) findViewById(R.id.btnRegister);

		Bundle bundle = this.getIntent().getExtras();
		this.country = bundle.getString("country");
		this.phone = bundle.getString("phone");
		this.password = bundle.getString("password");

		this.btnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String code = txtVerifyCode.getText().toString();
				if (checkCode(code)) {
					SMSSDK.registerEventHandler(submitCodeHandler);

					loading =CustomDialog.createLoadingDialog(
							RegisterMobileVerifyActivity.this, 
							"正在校验手机验证码,请稍后...");
					loading.show();

					SMSSDK.submitVerificationCode(country, phone, code);

				} else {
					showToast("请输入正确的验证码。", Toast.LENGTH_SHORT);
				}
			}
		});
	}

	private void registerAccount(String phone, String password) {
		loading = CustomDialog.createLoadingDialog(RegisterMobileVerifyActivity.this,
				"正在注册,请稍后...");
		loading.show();
		AccountService accountService = new AccountService(
				getApplicationContext().getUserContext());
		accountService.register(phone, password, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				Gson gson = new Gson();
				ResponseRegister resp = gson.fromJson(t, ResponseRegister.class);

				switch (resp.getResult()) {
				case ResponseRegister.Success:
					// 注册成功 保存用户名和密码到本地
					finish();
					break;
				case ResponseRegister.IncorrectPhoneFormat:
					showToast("手机号码格式错误。", Toast.LENGTH_SHORT);
					break;
				case ResponseRegister.PhoneExists:
					showToast("手机号码已经注册。", Toast.LENGTH_SHORT);
					break;
				default:
					showToast("未知错误。", Toast.LENGTH_SHORT);
					break;
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				showToast(strMsg, Toast.LENGTH_SHORT);
			}
		});
	}

	private boolean checkCode(String code) {
		return code.length() >= 4;
	}
}
