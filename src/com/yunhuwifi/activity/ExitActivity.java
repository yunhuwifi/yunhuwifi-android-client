package com.yunhuwifi.activity;

import com.foxrouter.api.RouterModulePassport;
import com.foxrouter.api.RouterResponseLogin;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.handlers.JsonCallBack;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class ExitActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_box);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	private void doLogout() {
		RouterModulePassport passport = new RouterModulePassport(
				getApplicationContext().getRouterContext());
		passport.logout(new JsonCallBack<RouterResponseLogin>(
				RouterResponseLogin.class) {

			@Override
			public void onJsonSuccess(RouterResponseLogin t) {
				finish();
				// android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				showToast(strMsg, Toast.LENGTH_SHORT);
				finish();
			}
		});
	}

	public void ok(View v) {
		doLogout();
	}

	public void cancel(View v) {
		this.finish();
	}
}
