package com.yunhuwifi.activity;

import com.foxrouter.api.RouterModuleNetwork;
import com.foxrouter.api.RouterResponseNetworkSetting;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.YunhuApplication;
import com.yunhuwifi.fragment.DHCPFrament;
import com.yunhuwifi.fragment.PPPoEFrament;
import com.yunhuwifi.fragment.StaticIPFrament;
import com.yunhuwifi.handlers.JsonCallBack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WanActivity extends HeaderFragmentActivity implements OnClickListener {

	private RelativeLayout layoutpppoe, layoutstaticip, layoutdhcp;
	private ImageView imageView1, imageView2, imageView3;
	private TextView textView1, textView2, textView3;
	private ViewPager viewPager;
	private int[] selectList;
	private ImageView[] imageViewList;
	private TextView[] textViewList;
	private int selectID = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_wan_setting);
		this.setLeftImageVisible(true);
		this.setRightImageVisible(false);
		this.setHeaderText("外网设置");
		this.ivLeft.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();

			}
		});
		initLayout();
		initData();
		initwan();
	}

	private void initwan() {
		YunhuApplication context = (YunhuApplication) getApplicationContext();
		RouterModuleNetwork wanNet = new RouterModuleNetwork(
				context.getRouterContext());
		wanNet.wanDetails(new JsonCallBack<RouterResponseNetworkSetting>(
				RouterResponseNetworkSetting.class) {

			@Override
			public void onJsonSuccess(RouterResponseNetworkSetting t) {
				if (t.getResult() != null) {
					String proto = t.getResult().getProto();
				}
			}

			public void onFailure(Throwable t, int errorNo, String strMsg) {
				Toast.makeText(WanActivity.this, strMsg, Toast.LENGTH_SHORT)
						.show();
			};
		});
	}

	private void initLayout() {
		layoutpppoe = (RelativeLayout) findViewById(R.id.layoutpppoe);
		layoutdhcp = (RelativeLayout) findViewById(R.id.layoutdhcp);
		layoutstaticip = (RelativeLayout) findViewById(R.id.layoutstaticip);

		imageView1 = (ImageView) findViewById(R.id.myImageView1);
		imageView2 = (ImageView) findViewById(R.id.myImageView2);
		imageView3 = (ImageView) findViewById(R.id.myImageView3);

		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);

		viewPager = (ViewPager) findViewById(R.id.viewPager);
	}

	private void initData() {
		selectList = new int[] { 0, 1, 1 };// 0表示选中，1表示未选中(默认第一个选中)
		imageViewList = new ImageView[] { imageView1, imageView2, imageView3 };
		textViewList = new TextView[] { textView1, textView2, textView3 };
		layoutpppoe.setOnClickListener(this);
		layoutstaticip.setOnClickListener(this);
		layoutdhcp.setOnClickListener(this);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(changeListener);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layoutpppoe:
			if (selectID == 0) {
				return;
			} else {
				setSelectedTitle(0);
				viewPager.setCurrentItem(0);
			}
			break;
		case R.id.layoutdhcp:
			if (selectID == 1) {
				return;
			} else {
				setSelectedTitle(1);
				viewPager.setCurrentItem(1);
			}
			break;
		case R.id.layoutstaticip:
			if (selectID == 2) {
				return;
			} else {
				setSelectedTitle(2);
				viewPager.setCurrentItem(2);
			}
			break;
		}
	}

	private FragmentPagerAdapter adapter = new FragmentPagerAdapter(
			getSupportFragmentManager()) {

		public int getCount() {
			return selectList.length;
		}

		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new PPPoEFrament();
				break;
			case 1:
				fragment = new DHCPFrament();
				break;
			case 2:
				fragment = new StaticIPFrament();
				break;
			}
			return fragment;
		}
	};

	private SimpleOnPageChangeListener changeListener = new SimpleOnPageChangeListener() {
		public void onPageSelected(int position) {
			setSelectedTitle(position);
		}
	};

	/**
	 * 当前UI改变时，修改TITLE选中项
	 * 
	 * @param position
	 *            0 1 2
	 */
	private void setSelectedTitle(int position) {
		for (int i = 0; i < selectList.length; i++) {
			if (selectList[i] == 0) {
				selectList[i] = 1;
				imageViewList[i].setVisibility(View.INVISIBLE);
				textViewList[i].setTextColor(getResources().getColor(
						R.color.black));
			}
		}
		selectList[position] = 0;
		imageViewList[position].setVisibility(View.VISIBLE);
		textViewList[position].setTextColor(getResources().getColor(
				R.color.frametitlecolor));
		selectID = position;
	}

}
