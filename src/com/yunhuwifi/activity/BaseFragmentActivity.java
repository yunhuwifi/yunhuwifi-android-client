package com.yunhuwifi.activity;

import com.yunhuwifi.YunhuApplication;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class BaseFragmentActivity extends FragmentActivity {

	public final static int ACTIVITY_RESULT_SUCCESS = 0;
	public final static int ACTIVITY_RESULT_FAILURE = 1;

	@Override
	public YunhuApplication getApplicationContext() {
		return (YunhuApplication) super.getApplicationContext();
	}

	protected void showToast(String message, int duration) {
		Toast.makeText(this, message, duration).show();
	}
}
