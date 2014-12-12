package com.yunhuwifi.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaAuthorizationListener.AuthorizationListener;
import com.baidu.frontia.api.FrontiaAuthorizationListener.UserInfoListener;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.UserContext;
import com.yunhuwifi.cloud.api.models.User;
import com.yunhuwifi.handlers.JsonCallBack;
import com.yunhuwifi.service.AppKey;
import com.yunhuwifi.util.CustomDialog;
import com.yunhuwifi.util.DbUtility;
import com.yunhuwifi.view.ClearEditText;
import com.yunhuwifi.view.PasswordEditText;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler.Callback;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements Callback,
		OnClickListener, PlatformActionListener {
	private static final int MSG_USERID_FOUND = 1;
	private static final int MSG_LOGIN = 2;
	private static final int MSG_AUTH_CANCEL = 3;
	private static final int MSG_AUTH_ERROR = 4;
	private static final int MSG_AUTH_COMPLETE = 5;
	private static final String TAG = "LoginActivity";

	private FrontiaAuthorization mAuthorization;
	private final static String Scope_Basic = "basic";
	private final static String Scope_Netdisk = "netdisk";

	private PasswordEditText pwd;
	private ClearEditText user;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShareSDK.initSDK(this);
		Frontia.init(LoginActivity.this, AppKey.baiduAPPKEY);
		mAuthorization = Frontia.getAuthorization();

		setContentView(R.layout.activity_login);
		findViewById(R.id.signin).setOnClickListener(this);
		findViewById(R.id.ivqq).setOnClickListener(this);
		findViewById(R.id.ivsina).setOnClickListener(this);
		findViewById(R.id.register_link).setOnClickListener(this);
		findViewById(R.id.forgetPwd).setOnClickListener(this);
		findViewById(R.id.ivbaidu).setOnClickListener(this);
		user = (ClearEditText) findViewById(R.id.username);
		pwd = (PasswordEditText) findViewById(R.id.pwd);
	}

	protected void startBaiduUserInfo() {
		userinfo(MediaType.BAIDU.toString());

	}

	protected void startBaidu() {
		ArrayList<String> scope = new ArrayList<String>();
		scope.add(Scope_Basic);
		scope.add(Scope_Netdisk);
		mAuthorization.authorize(this,
				FrontiaAuthorization.MediaType.BAIDU.toString(), scope,
				new AuthorizationListener() {

					@Override
					public void onSuccess(FrontiaUser result) {
						String meg = result.getId() + result.getAccessToken()
								+ result.getExpiresIn();
						System.out.println(meg);
						showToast("授权成功", Toast.LENGTH_SHORT);
						startBaiduUserInfo();
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						showToast(errMsg, Toast.LENGTH_SHORT);
					}

					@Override
					public void onCancel() {
						showToast("授权操作已取消", Toast.LENGTH_SHORT);
					}

				});
	}

	private void userinfo(String accessToken) {
		mAuthorization.getUserInfo(accessToken, new UserInfoListener() {

			@Override
			public void onSuccess(FrontiaUser.FrontiaUserDetail result) {

				String resultStr = "username:" + result.getName() + "\n"
						+ "birthday:" + result.getBirthday() + "\n" + "city:"
						+ result.getCity() + "\n" + "province:"
						+ result.getProvince() + "\n" + "sex:"
						+ result.getSex() + "\n" + "pic url:"
						+ result.getHeadUrl() + "\n";
				System.out.println(resultStr);
			}

			@Override
			public void onFailure(int errCode, String errMsg) {

				showToast(errMsg, Toast.LENGTH_SHORT);
			}

		});
	}

	//百度
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (null != mAuthorization) {
			mAuthorization.onActivityResult(requestCode, resultCode, data);
		}
	}

	protected void saveLogin(User user) {
		User localUser = DbUtility.find(getApplicationContext(), User.class);
		if (localUser == null) {
			localUser = new User();
			localUser.setUid(user.getUid());
			localUser.setUsername(user.getUsername());
			localUser.setPassword(user.getPassword());
			localUser.setPhone(user.getPhone());
			localUser.setHasSameName(user.isHasSameName());
			localUser.setMail(user.getMail());
			DbUtility.save(getApplicationContext(), user);
		} else {
			localUser.setUid(user.getUid());
			localUser.setUsername(user.getUsername());
			localUser.setPassword(user.getPassword());
			localUser.setPhone(user.getPhone());
			localUser.setHasSameName(user.isHasSameName());
			localUser.setMail(user.getMail());
			DbUtility.save(getApplicationContext(), localUser);
		}
	}

	protected void onDestroy() {
		Log.i(TAG, "onDestroy执行了");
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}

	// 云狐账号登录
	public void loginAndReadCookie() {
		if (!checkInput()) {
			Toast.makeText(LoginActivity.this, "登录名和密码不能为空!",
					Toast.LENGTH_SHORT).show();
			return;
		}
		final Dialog loading = CustomDialog.createLoadingDialog(
				LoginActivity.this, "正在登录，请稍后...");
		final String consumer = user.getText().toString();
		final String password = pwd.getText().toString();
		UserContext.login(consumer, password, new JsonCallBack<UserContext>(
				UserContext.class) {
			@Override
			public void onJsonSuccess(UserContext t) {
				loading.dismiss();

				getApplicationContext().setUserContext(t);
				saveLogin(t.getUser());

				setResult(ACTIVITY_RESULT_SUCCESS);
				finish();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				loading.dismiss();
				showToast(strMsg, Toast.LENGTH_SHORT);
			}
		});
	}

	private boolean checkInput() {
		final String consumer = user.getText().toString();
		final String password = pwd.getText().toString();

		return !consumer.equals("") && !password.equals("");
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivbaidu:
			startBaidu();
			break;
		case R.id.signin: {
			Log.i(TAG, "onClick执行了");
			loginAndReadCookie();

		}
			break;
		case R.id.ivsina: {
			authorize(new SinaWeibo(this));
		}
			break;
		case R.id.ivqq: {
			authorize(new QZone(this));
		}
			break;

		case R.id.register_link: {
			Intent i = new Intent(LoginActivity.this,
					RegisterMobileActivity.class);
			startActivity(i);
		}
			break;
		case R.id.forgetPwd: {
			Intent i = new Intent(LoginActivity.this, ForgetPwdActivity.class);
			startActivity(i);
		}
			break;
		}
	}

	private void authorize(Platform plat) {
		Log.i(TAG, "authorize执行了");

		if (plat.isValid()) {
			String userId = plat.getDb().getUserId();
			if (!TextUtils.isEmpty(userId)) {
				UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
				login(plat.getName(), userId, null);

				Log.i(TAG, "id:" + userId);
				Log.i(TAG, "getExpiresIn:" + plat.getDb().getExpiresIn());
				Log.i(TAG, "getExpiresTime:" + plat.getDb().getExpiresTime());
				Log.i(TAG, "getPlatformNname:"
						+ plat.getDb().getPlatformNname());
				Log.i(TAG, "getPlatformVersion:"
						+ plat.getDb().getPlatformVersion());
				Log.i(TAG, "getToken:" + plat.getDb().getToken());
				Log.i(TAG, "getTokenSecret:" + plat.getDb().getTokenSecret());
				Log.i(TAG, "getUserIcon:" + plat.getDb().getUserIcon());
				Log.i(TAG, "getUserId:" + plat.getDb().getUserId());
				Log.i(TAG, "getUserName:" + plat.getDb().getUserName());

				return;
			}
		}
		plat.setPlatformActionListener(this);
		plat.SSOSetting(true);
		plat.showUser(null);
	}

	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		if (action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
			login(platform.getName(), platform.getDb().getUserId(), res);
		}
		System.out.println("授权成功....." + platform.getDb().getUserName());
	}

	public void onError(Platform platform, int action, Throwable t) {
		if (action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
		}
		t.printStackTrace();
	}

	public void onCancel(Platform platform, int action) {
		if (action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
		}
	}

	private void login(String plat, String userId,
			HashMap<String, Object> userInfo) {
		Message msg = new Message();
		msg.what = MSG_LOGIN;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}

	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_USERID_FOUND: {
			Toast.makeText(this, R.string.userid_found, Toast.LENGTH_SHORT)
					.show();
		}
			break;
		case MSG_LOGIN: {

			String text = getString(R.string.logining, msg.obj);
			Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

			Platform weibo = ShareSDK.getPlatform(this, QQ.NAME);
			if (weibo.isValid()) {
				weibo.removeAccount();
			}

			/*
			 * Builder builder = new Builder(this);
			 * builder.setTitle(R.string.if_register_needed);
			 * builder.setMessage(R.string.after_auth);
			 * builder.setPositiveButton(R.string.ok, null);
			 * builder.create().show();
			 */
		}
			break;
		case MSG_AUTH_CANCEL: {
			Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT)
					.show();
		}
			break;
		case MSG_AUTH_ERROR: {
			Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT)
					.show();
		}
			break;
		case MSG_AUTH_COMPLETE: {
			Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT)
					.show();
		}
			break;
		}
		return false;
	}

}
