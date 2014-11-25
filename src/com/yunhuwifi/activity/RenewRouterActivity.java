package com.yunhuwifi.activity;

import com.foxrouter.api.RouterModuleSystem;
import com.foxrouter.api.RouterResponseResult;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.handlers.JsonCallBack;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RenewRouterActivity extends BaseActivity {

	private TextView dialogtitle, dialogmessage;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_box);
		dialogtitle = (TextView) findViewById(R.id.dialogtitle);
		dialogmessage = (TextView) findViewById(R.id.dialogmessage);
		dialogtitle.setText("恢复出厂设置");
		dialogmessage.setText("将使路由器的所有设置\n恢复到出厂时的默认状态！");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	public void ok(View v) {
		RouterModuleSystem system = new RouterModuleSystem(
				getApplicationContext().getRouterContext());
		system.reset(new JsonCallBack<RouterResponseResult>(
				RouterResponseResult.class) {

			@Override
			public void onJsonSuccess(RouterResponseResult t) {
				if (t.getError() == null && t.isResult()) {
					showToast("路由器正在恢复出厂设置", Toast.LENGTH_SHORT);
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				showToast("恢复出厂设置失败", Toast.LENGTH_SHORT);
			}
		});
	}

	public void cancel(View v) {
		this.finish();
	}

}
