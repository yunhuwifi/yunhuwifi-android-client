package com.yunhuwifi.activity;

import com.foxrouter.api.RouterModuleDetect;
import com.foxrouter.api.RouterModulePassport;
import com.foxrouter.api.RouterResponseBootResult;
import com.foxrouter.api.RouterResponseResult;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.RouterContext;
import com.yunhuwifi.UserContext;
import com.yunhuwifi.cloud.api.AccountService;
import com.yunhuwifi.cloud.api.models.ResponseLogin;
import com.yunhuwifi.cloud.api.models.User;
import com.yunhuwifi.handlers.JsonCallBack;
import com.yunhuwifi.models.RouterLocalAccount;
import com.yunhuwifi.util.DbUtility;

import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class WelcomeActivity extends BaseActivity implements AnimationListener {

	private final static int LOGIN_REQUEST_CODE = 0;
	private Animation alphaAnimation;
	private ImageView img;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			User user = DbUtility.find(getApplicationContext(), User.class);
			Log.d("user", user == null ? "null" : user.getUsername());
			if (user == null) {
				detectRouter();
			} else {
				loginWithYunhuAccount(user);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		img = (ImageView) findViewById(R.id.iv);
		alphaAnimation = AnimationUtils.loadAnimation(this,
				R.anim.welcome_alpha);
		alphaAnimation.setFillEnabled(true); 
		alphaAnimation.setFillAfter(true); 
		img.setAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener(this); // 为动画设置监听
	}

	private void detectRouter() {
		RouterModuleDetect detect = new RouterModuleDetect(
				getApplicationContext().getRouterContext());

		try {
			detect.isSupportedRouter(getApplicationContext(),
					new JsonCallBack<RouterResponseBootResult>(
							RouterResponseBootResult.class) {

						@Override
						public void onJsonSuccess(RouterResponseBootResult t) {
							Log.d("result", t.toString());
							if (t.isResult()) {
								if(t.isBootResult()){
									startActivity(new Intent(WelcomeActivity.this,WizardTestActivity.class));
									finish();
								}else{
									loginRouterAccount();
								}
							} else {
								loginWithYunhuAccount(null);
							}
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							startActivity(new Intent(WelcomeActivity.this,NotConnectActivity.class));
							finish();
						}

					});
		} catch (NetworkErrorException e) {
			startActivity(new Intent(WelcomeActivity.this,NotConnectActivity.class));
			finish();
		}
	}

	// 使用云狐账号登录
	private void loginWithYunhuAccount(User user) {
		if (user == null) {
			startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
			finish();
		} else {
			final UserContext userContext = new UserContext();
			userContext.setLoginName(user.getUsername());
			userContext.setPassword(user.getPassword());
			AccountService accountService = new AccountService(userContext);
			accountService.login(userContext, new JsonCallBack<ResponseLogin>(
					ResponseLogin.class) {
				@Override
				public void onJsonSuccess(ResponseLogin t) {
					if (t.isSuccess()) {
						userContext.setUser(t.getResult());
						getApplicationContext().setUserContext(userContext);
						startActivity(new Intent(WelcomeActivity.this,
								MainActivity.class));
						finish();
					} else {
						startActivityForResult(new Intent(WelcomeActivity.this,
								LoginActivity.class), LOGIN_REQUEST_CODE);
					}
				}

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					Log.d("result", strMsg);
					startActivityForResult(new Intent(WelcomeActivity.this,
							LoginActivity.class), LOGIN_REQUEST_CODE);
				}
			});
		}
	}

	// 使用路由器管理账号登陆
	private void loginRouterAccount() {
		WifiManager wifiManager = (WifiManager) this
				.getSystemService(WIFI_SERVICE);

		if (wifiManager.isWifiEnabled()) {
			String macAddress = wifiManager.getConnectionInfo().getBSSID();
			String ipAddress = Formatter.formatIpAddress(wifiManager
					.getDhcpInfo().serverAddress);
			RouterLocalAccount account = DbUtility.findByWhere(
					getApplicationContext(), RouterLocalAccount.class,
					"macAddress='" + macAddress + "'");

			if (account != null) {
				RouterContext.login(account.getUsername(),
						account.getPassword(), ipAddress,
						RouterModulePassport.DEFAULT_PORT,
						new JsonCallBack<RouterContext>(RouterContext.class) {

							@Override
							public void onJsonSuccess(RouterContext t) {
								getApplicationContext().setRouterContext(t);

								Intent intent = new Intent(
										WelcomeActivity.this,
										MainActivity.class);
								startActivity(intent);
								finish();
							}

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								startActivityForResult(new Intent(
										WelcomeActivity.this,
										RouterLoginActivity.class),
										LOGIN_REQUEST_CODE);
							}
						});

			} else {
				startActivityForResult(new Intent(WelcomeActivity.this,
						RouterLoginActivity.class), LOGIN_REQUEST_CODE);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == LOGIN_REQUEST_CODE
				&& resultCode == ACTIVITY_RESULT_SUCCESS) {
			Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		mHandler.sendEmptyMessage(0);

	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}
}
