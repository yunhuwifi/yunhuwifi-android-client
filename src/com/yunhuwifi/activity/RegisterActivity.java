package com.yunhuwifi.activity;

import com.yunhuwifi.activity.R;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RegisterActivity extends Activity implements OnClickListener {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		TextView headerText = (TextView) findViewById(R.id.header_txtView);
		headerText.setText("注册");
		findViewById(R.id.regcode);
		findViewById(R.id.register).setOnClickListener(this);

		// 短信验证服务 文档地址：
		// http://wiki.mob.com/Android_%E7%9F%AD%E4%BF%A1SDK%E9%9B%86%E6%88%90%E6%96%87%E6%A1%A3#.E7.AC.AC.E4.BA.8C.E6.AD.A5_.E5.AF.BC.E5.85.A5SDK
		// 初始化短信验证服务
		SMSSDK.initSDK(this.getApplicationContext(), "3174b3c75487",
				"d00c10142976a06f3179030f8c97f5f5");

		// 短信验证回调
		EventHandler handler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				switch (result) {
				case SMSSDK.EVENT_GET_CONTACTS: // 获取手机内部的通信录列表
					// ArrayList<HashMap<String,Object>>
					break;
				case SMSSDK.EVENT_GET_FRIENDS_IN_APP: // 获取手机通信录在当前应用内的用户列表
					// ArrayList<HashMap<String,Object>>
					break;
				case SMSSDK.EVENT_GET_NEW_FRIENDS_COUNT: // 获取手机通信录在当前应用内的新用户个数
					// integer
					break;
				case SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES: // 返回支持发送验证码的国家列表
					// ArrayList<HashMap<String,Object>>
					break;
				case SMSSDK.EVENT_GET_VERIFICATION_CODE: // 请求发送验证码，无返回
					break;
				case SMSSDK.EVENT_SUBMIT_USER_INFO: // 提交应用内的用户资料
					break;
				case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE: // 校验验证码，返回校验的手机和国家代码
					// HashMap<String,Object>
					break;
				case SMSSDK.RESULT_COMPLETE:
					break;
				case SMSSDK.RESULT_ERROR:
					break;
				}
			}
		};
		SMSSDK.registerEventHandler(handler);
		// 注销回调
		SMSSDK.unregisterEventHandler(handler);

		// 获取支持的所有国家列表
		SMSSDK.getSupportedCountries();

		// 请求获取短信验证码，在监听中返回
		SMSSDK.getVerificationCode("country", "手机号码");

		// 提交短信验证码，在监听中返回
		SMSSDK.submitVerificationCode("country", "手机号码", "code");
	}

	@Override
	public void onClick(View v) {
	}

}
