package com.yunhuwifi.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.yunhuwifi.fragment.DownloadPagerAdapter;
import com.yunhuwifi.fragment.DownloadedFragment;
import com.yunhuwifi.view.ListViewAdapter;


public class DownloadActivity extends FragmentActivity {

	private ViewPager vp_file; 
	private TextView tvdownloaded,tvnotdownload, header_txtView;
	private int selectID = 0;
	private Button header_btnLeft, header_btnRight;
	private String link="";
	private ListViewAdapter adapter;
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
		header_btnLeft = (Button) findViewById(R.id.header_btnLeft);
		header_btnRight = (Button) findViewById(R.id.header_btnRight);
		header_btnRight.setBackgroundResource(R.drawable.addlink);
		header_btnLeft.setOnClickListener(listener);
		header_btnRight.setOnClickListener(listener);
		findView();
		init();
	}

	private void findView() {
		vp_file = (ViewPager) findViewById(R.id.vp_file);
		tvdownloaded = (TextView) findViewById(R.id.tvdownloaded);
		tvnotdownload= (TextView) findViewById(R.id.tvnotdownload);
		tvdownloaded.setOnClickListener(listener);
		tvnotdownload.setOnClickListener(listener);
		if(operateVisible){
			header_btnLeft.setBackgroundResource(R.drawable.btndelbg);
			header_btnRight.setBackgroundResource(R.drawable.btndelbg);
			header_btnLeft.setText("取消");
			header_btnRight.setText("全选");
		}else{
			header_btnLeft.setBackgroundResource(R.drawable.back);
			header_btnRight.setBackgroundResource(R.drawable.addlink);
			header_btnLeft.setText("");
			header_btnRight.setText("");
			
		}
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
			case R.id.header_btnLeft:
				if(header_btnLeft.getText().toString()=="")
				{
					finish();
				}else{
				operateVisible=false;
				ListViewAdapter.visable = false;
				for (int i = 0; i < adapter.flags.size(); i++) {
					adapter.flags.set(i, false);
				}
				adapter.notifyDataSetChanged();
				}
				break;
			case R.id.header_btnRight:
				if(header_btnRight.getText().toString()=="全选"){
					header_btnRight.setText("全不选");
					adapter.flags.clear();
					for (int i = 0; i < adapter.getCount(); i++) {
						adapter.flags.add(true);
					}
					adapter.notifyDataSetChanged();
				}else if(header_btnRight.getText().toString()=="全不选"){
					header_btnRight.setText("全选");
					adapter.flags.clear();
					for (int i = 0; i < adapter.getCount(); i++) {
						adapter.flags.add(false);
					}
					adapter.notifyDataSetChanged();
				}else{
					popupaddlink();
				}
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
