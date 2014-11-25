package com.yunhuwifi.activity;

import com.yunhuwifi.activity.R;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HeaderFragmentActivity extends BaseFragmentActivity {
	protected Button btnLeft;
	protected Button btnRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.header);

		this.btnLeft = (Button) findViewById(R.id.header_btnLeft);
		this.btnRight = (Button) findViewById(R.id.header_btnRight);

		// 默认隐藏header上的2个按钮
		this.setLeftButtonVisible(false);
		this.setRightButtonVisible(false);
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
	 * 隐藏左侧按钮
	 */
	public void setLeftButtonVisible(boolean visible) {
		if (null != this.btnLeft) {
			if (visible)
				this.btnLeft.setVisibility(View.VISIBLE);
			else
				this.btnLeft.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 隐藏右侧按钮
	 */
	public void setRightButtonVisible(boolean visible) {
		if (null != this.btnRight) {
			if (visible)
				this.btnRight.setVisibility(View.VISIBLE);
			else
				this.btnRight.setVisibility(View.INVISIBLE);
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

	public void goback(View v) { // 标题栏 返回按钮
		this.finish();
	}
}