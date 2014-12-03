package com.yunhuwifi.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.yunhuwifi.fragment.DownloadPagerAdapter;
import com.yunhuwifi.fragment.DownloadedFragment;


public class DownloadActivity extends FragmentActivity {

	private ViewPager vp_file; 
	private TextView tvdownloaded,tvnotdownload, header_txtView;
	private int selectID = 0;
	private ImageView header_ivLeft, header_ivRight;
	private String link="";
	public static boolean operateVisible=false;
	

	private String selected=DownloadedFragment.tvSelected;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		header_txtView = (TextView) findViewById(R.id.header_txtView);
		if(selected==""){
			header_txtView.setText("下载管理");
		}else{
			header_txtView.setText(selected);
		}
		header_ivLeft = (ImageView) findViewById(R.id.header_ivLeft);
		header_ivRight = (ImageView) findViewById(R.id.header_ivRight);
		header_ivRight.setImageResource(R.drawable.addicon);
		header_ivLeft.setOnClickListener(listener);
		header_ivRight.setOnClickListener(listener);
		findView();
		init();
	}

	private void findView() {
		vp_file = (ViewPager) findViewById(R.id.vp_file);
		tvdownloaded = (TextView) findViewById(R.id.tvdownloaded);
		tvnotdownload= (TextView) findViewById(R.id.tvnotdownload);
		tvdownloaded.setOnClickListener(listener);
		tvnotdownload.setOnClickListener(listener);
		
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tvdownloaded:
				if (selectID == 0) {
					return;
				} else {
					setCurrentPage(0);
					vp_file.setCurrentItem(0);
				}
				break;
			case R.id.tvnotdownload:
				if (selectID == 1) {
					return;
				} else {
					setCurrentPage(1);
					vp_file.setCurrentItem(1);
				}
				break;
			case R.id.header_ivLeft:
					finish();
				break;
			case R.id.header_ivRight:
				
					popupaddlink();
				break;
			}
		}
	};

	private void init() { 
		vp_file.setAdapter(new DownloadPagerAdapter(getSupportFragmentManager()));
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
			tvdownloaded.setBackgroundResource(R.drawable.pressedframetitlebg);
			tvdownloaded.setTextColor(getResources().getColor(
					R.color.frametitlecolor));
			tvnotdownload.setBackgroundResource(R.drawable.normalframetitlebg);
			tvnotdownload.setTextColor(getResources().getColor(R.color.black));
		} else {
			tvnotdownload.setBackgroundResource(R.drawable.pressedframetitlebg);
			tvnotdownload.setTextColor(getResources().getColor(
					R.color.frametitlecolor));
			tvdownloaded.setBackgroundResource(R.drawable.normalframetitlebg);
			tvdownloaded.setTextColor(getResources().getColor(R.color.black));
		}
		selectID = current;
	}

	private void popupaddlink() {
		final Dialog dlg = new Dialog(this);
		View dlgView = View.inflate(this,  R.layout.addlink_box,null);
		View oklink = dlgView.findViewById(R.id.oklink); 
		oklink.setTag(dlg); 
		final EditText tvlink = (EditText)dlgView.findViewById(R.id.tvlink); 
		oklink.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				link=tvlink.getText().toString();
				dlg.dismiss();
			} 
		});
		View cancellink = dlgView.findViewById(R.id.cancellink);
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
