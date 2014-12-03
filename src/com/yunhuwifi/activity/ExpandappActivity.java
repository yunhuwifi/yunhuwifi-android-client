package com.yunhuwifi.activity;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.fragment.AppPagerAdapter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandappActivity extends FragmentActivity {

	private ViewPager vp_content;
	private TextView tv_install, tv_notinstall, header_txtView;
	private int selectID = 0;
	private ImageView header_ivLeft, header_ivRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appmain);
		findView();
		init();
		header_txtView = (TextView) findViewById(R.id.header_txtView);
		header_txtView.setText("扩展应用");
		header_ivLeft = (ImageView) findViewById(R.id.header_ivLeft);
		header_ivRight = (ImageView) findViewById(R.id.header_ivRight);
		header_ivLeft.setVisibility(View.INVISIBLE);
		header_ivRight.setVisibility(View.INVISIBLE);
	}

	private void findView() {
		vp_content = (ViewPager) findViewById(R.id.vp_content);
		tv_install = (TextView) findViewById(R.id.tv_install);
		tv_notinstall = (TextView) findViewById(R.id.tv_notinstall);
		tv_install.setOnClickListener(listener);
		tv_notinstall.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_install:
				if (selectID == 0) {
					return;
				} else {
					setCurrentPage(0);
					vp_content.setCurrentItem(0);
				}
				break;
			case R.id.tv_notinstall:
				if (selectID == 1) {
					return;
				} else {
					setCurrentPage(1);
					vp_content.setCurrentItem(1);
				}
				break;
			}
		}
	};

	private void init() {
		vp_content.setAdapter(new AppPagerAdapter(getSupportFragmentManager()));
		vp_content.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				setCurrentPage(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// ignore
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// ignore
			}
		});

	}

	private void setCurrentPage(int current) {
		if (current == 0) {
			tv_install.setBackgroundResource(R.drawable.pressedframetitlebg);
			tv_install.setTextColor(getResources().getColor(
					R.color.frametitlecolor));
			tv_notinstall.setBackgroundResource(R.drawable.normalframetitlebg);
			tv_notinstall.setTextColor(getResources().getColor(R.color.black));
		} else {
			tv_notinstall.setBackgroundResource(R.drawable.pressedframetitlebg);
			tv_notinstall.setTextColor(getResources().getColor(
					R.color.frametitlecolor));
			tv_install.setBackgroundResource(R.drawable.normalframetitlebg);
			tv_install.setTextColor(getResources().getColor(R.color.black));
		}
		selectID = current;
	}

	public void goback(View v) {
		this.finish();

	}

	// 再按一次返回键退出
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(ExpandappActivity.this, "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
