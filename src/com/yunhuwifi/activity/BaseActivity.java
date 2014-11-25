package com.yunhuwifi.activity;

import com.yunhuwifi.YunhuApplication;

import android.app.Activity;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {

	public final static int ACTIVITY_RESULT_SUCCESS = 1;
	public final static int ACTIVITY_RESULT_FAILURE = 0;

	@Override
	public YunhuApplication getApplicationContext() {
		return (YunhuApplication) super.getApplicationContext();
	}

	protected void showToast(String message, int duration) {
		Toast.makeText(this, message, duration).show();
	}
}
