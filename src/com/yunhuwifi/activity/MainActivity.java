package com.yunhuwifi.activity;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.RouterContext;
import com.yunhuwifi.UserContext;
import com.yunhuwifi.YunhuApplication;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

@SuppressLint("ResourceAsColor")
@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	public static TabHost mTabHost;
	private RadioGroup main_radio;
	private RadioButton tab_icon_state, tab_icon_device, tab_icon_app,
			tab_icon_setting;
	private ImageView header_ivLeft, header_ivRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		header_ivLeft = (ImageView) findViewById(R.id.header_ivLeft);
		header_ivRight = (ImageView) findViewById(R.id.header_ivRight);
		header_ivLeft.setVisibility(View.INVISIBLE);
		header_ivRight.setVisibility(View.INVISIBLE);

		mTabHost = this.getTabHost();
		final TabWidget tabWidget = mTabHost.getTabWidget();

		// 添加n个tab选项卡，定义他们的tab名，指示名，目标屏对应的类
		mTabHost.addTab(mTabHost.newTabSpec("TAG2").setIndicator("0")
				.setContent(new Intent(this, StateActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("TAG3").setIndicator("1")
				.setContent(new Intent(this, DeviceActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("TAG1").setIndicator("2")
				.setContent(new Intent(this, ExpandappActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("TAG4").setIndicator("3")
				.setContent(new Intent(this, SettingActivity.class)));

		// 视觉上,用单选按钮替代TabWidget
		main_radio = (RadioGroup) findViewById(R.id.main_radio);
		tab_icon_state = (RadioButton) findViewById(R.id.tab_icon_state);
		tab_icon_device = (RadioButton) findViewById(R.id.tab_icon_device);
		tab_icon_app = (RadioButton) findViewById(R.id.tab_icon_app);
		tab_icon_setting = (RadioButton) findViewById(R.id.tab_icon_setting);
		main_radio
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int id) {
						if (id == tab_icon_state.getId()) {
							mTabHost.setCurrentTab(0);
						} else if (id == tab_icon_device.getId()) {
							mTabHost.setCurrentTab(1);
						} else if (id == tab_icon_app.getId()) {
							mTabHost.setCurrentTab(2);
						} else if (id == tab_icon_setting.getId()) {
							mTabHost.setCurrentTab(3);
						}
					}

				});

		// 设置当前显示哪一个标签
		mTabHost.setCurrentTab(0);
		// 遍历tabWidget每个标签，设置背景图片 无
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			View vv = tabWidget.getChildAt(i);
			vv.getLayoutParams().height = 45;
			vv.setBackgroundDrawable(null);
		}
	}

	private void promptBind() {
		// 提示用户绑定路由器
		YunhuApplication ctx = (YunhuApplication) getApplicationContext();
		UserContext uct = ctx.getUserContext();
		if (uct == null) {
			// 用户未登录云狐账号
		} else {
			RouterContext rtx = ctx.getRouterContext();
			// 判断当前连接的Wifi是不是routerContext里的路由器

		}
	}


}
