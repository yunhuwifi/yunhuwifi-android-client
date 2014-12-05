package com.yunhuwifi.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yunhuwifi.fragment.FilePagerAdapter;


public class FileActivity extends HeaderFragmentActivity {

	private ViewPager vp_file; 
	private TextView tv_storage, tv_skydrive;
	private int selectID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_file);
		
		this.setHeaderText("存储管理");
		this.setLeftImageVisible(true);
		this.setRightImageVisible(true);
		this.ivRight.setImageResource(R.drawable.downicon);
		this.ivLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		this.ivRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(FileActivity.this,DownloadActivity.class));
			}
		});
		
		
		findView();
		init();
	}

	
	
	private void findView() {
		vp_file = (ViewPager) findViewById(R.id.vp_file);
		tv_storage = (TextView) findViewById(R.id.tv_storage);
		tv_skydrive= (TextView) findViewById(R.id.tv_skydrive);
		tv_skydrive.setOnClickListener(listener);
		tv_storage.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_storage:
				if (selectID == 0) {
					return;
				} else {
					setCurrentPage(0);
					vp_file.setCurrentItem(0);
				}
				break;
			case R.id.tv_skydrive:
				if (selectID == 1) {
					return;
				} else {
					setCurrentPage(1);
					vp_file.setCurrentItem(1);
				}
				break;
			}
		}
	};

	private void init() { 
		vp_file.setAdapter(new FilePagerAdapter(getSupportFragmentManager()));
		vp_file.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				setCurrentPage(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

	}

	private void setCurrentPage(int current) {
		if (current == 0) {
			tv_storage.setBackgroundResource(R.drawable.pressedframetitlebg);
			tv_storage.setTextColor(getResources().getColor(
					R.color.frametitlecolor));
			tv_skydrive.setBackgroundResource(R.drawable.normalframetitlebg);
			tv_skydrive.setTextColor(getResources().getColor(R.color.black));
		} else {
			tv_skydrive.setBackgroundResource(R.drawable.pressedframetitlebg);
			tv_skydrive.setTextColor(getResources().getColor(
					R.color.frametitlecolor));
			tv_storage.setBackgroundResource(R.drawable.normalframetitlebg);
			tv_storage.setTextColor(getResources().getColor(R.color.black));
			popupBind();
		}
		selectID = current;
	}

	
	
	private void popupBind() {
		final Dialog dlg = new Dialog(this);
		View dlgView = View.inflate(this,  R.layout.confirm_box,null);
		View oklink = dlgView.findViewById(R.id.ok); 
		oklink.setTag(dlg); 
		TextView dialogtitle = (TextView)dlgView.findViewById(R.id.dialogtitle); 
		dialogtitle.setText("提示");
		TextView dialogmessage = (TextView)dlgView.findViewById(R.id.dialogmessage); 
		dialogmessage.setText("您需要设置账户才能使用百度网盘！");
		
		oklink.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(FileActivity.this,BaiDuLoginActivity.class));
				dlg.dismiss();
			} 
		});
		View cancellink = dlgView.findViewById(R.id.cancel);
		cancellink.setTag(dlg);
		cancellink.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dlg.dismiss();
			}
		});
		
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(dlgView);
		dlg.show();
	
	}
}
