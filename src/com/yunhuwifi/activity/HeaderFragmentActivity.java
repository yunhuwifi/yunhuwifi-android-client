package com.yunhuwifi.activity;

import com.yunhuwifi.activity.R;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HeaderFragmentActivity extends BaseFragmentActivity {
	protected ImageView ivLeft, ivRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.header);

		this.ivLeft = (ImageView) findViewById(R.id.header_ivLeft);
		this.ivRight = (ImageView) findViewById(R.id.header_ivRight);

		// 默认隐藏header上的2个按钮
		this.setLeftImageVisible(false);
		this.setRightImageVisible(false);
	}

	/**
	 * 设置主内容区域的layoutRes
	 * 
	 * @param layoutResId
	 */
	public void setContentLayout(int layoutResId) {
		LinearLayout llContent = (LinearLayout) findViewById(R.id.layoutMain);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(layoutResId, null);
		llContent.addView(v);
	}

	/**
	 * 隐藏左侧图标
	 */
	public void setLeftImageVisible(boolean visible) {
		if (null != this.ivLeft) {
			if (visible)
				this.ivLeft.setVisibility(View.VISIBLE);
			else
				this.ivLeft.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 隐藏右侧图标
	 */
	public void setRightImageVisible(boolean visible) {
		if (null != this.ivRight) {
			if (visible)
				this.ivRight.setVisibility(View.VISIBLE);
			else
				this.ivRight.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 设置模板上导航栏中间的标题文字
	 * 
	 * @param titleText
	 * @return 修改成功返回true，失败返回false
	 */
	public boolean setHeaderText(String titleText) {
		TextView tv = (TextView) findViewById(R.id.header_txtView);
		if (null != tv) {
			tv.setText(titleText);
			return true;
		}
		return false;
	}

}