package com.yunhuwifi.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.models.NetWorkSpeedInfo;
import com.yunhuwifi.service.ReadFile;
import com.yunhuwifi.view.CircleProgressBar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class TestSpeedActivity extends BaseActivity {
	private String url = "http://www.51eoc.com:8080/itravel/36.7-1.7.8.apk";
	private int downloadPercent = 0; 
	byte[] imageData = null;
	NetWorkSpeedInfo netWorkSpeedInfo = null;
	private final int UPDATE_SPEED = 1;
	private final int UPDATE_DNOE = 0;
	private ProgressBar horizontalPB;
	private CircleProgressBar circlePb; 
	private LinearLayout lay_speed;
	private Button startButton;
	private ImageView ivmainback;
	private TextView nowSpeed, avageSpeed, min_speed,tvunit,tvspeed,tvpbtxt,tvmaxtxt;
	private double speedMax,speedMin;
	long tem = 0;
	long falg = 0;
	long numberTotal = 0;
	List<Long> list = new ArrayList<Long>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_speed);
		startButton = (Button) findViewById(R.id.start_button);
		ivmainback = (ImageView) findViewById(R.id.ivmainback);
		nowSpeed = (TextView) findViewById(R.id.now_speed);
		min_speed = (TextView) findViewById(R.id.min_speed);
		lay_speed = (LinearLayout) findViewById(R.id.lay_speed);
		avageSpeed = (TextView) findViewById(R.id.average_speed);
		tvspeed = (TextView) findViewById(R.id.tvspeed);
		tvunit = (TextView) findViewById(R.id.tvunit);
		tvpbtxt = (TextView) findViewById(R.id.tvpbtxt);
		tvmaxtxt = (TextView) findViewById(R.id.tvmaxtxt);
		horizontalPB=(ProgressBar) findViewById(R.id.horizontalPB);
		circlePb=(CircleProgressBar) findViewById(R.id.circlePB);
		netWorkSpeedInfo = new NetWorkSpeedInfo();
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				list.clear();
				tem = 0;
				falg = 0;
				numberTotal = 0;
				lay_speed.setVisibility(View.VISIBLE);
				new Thread() {
					@Override
					public void run() {
						imageData = ReadFile.getFileFromUrl(url,
								netWorkSpeedInfo);
					}
				}.start();

				new Thread() {
					@Override
					public void run() {
						while (netWorkSpeedInfo.hadFinishedBytes < netWorkSpeedInfo.totalBytes) {
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							handler.sendEmptyMessage(UPDATE_SPEED);
						}
						if (netWorkSpeedInfo.hadFinishedBytes == netWorkSpeedInfo.totalBytes) {
							handler.sendEmptyMessage(UPDATE_DNOE);
							netWorkSpeedInfo.hadFinishedBytes = 0;
						}

					}
				}.start();
			}
		});

		ivmainback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}


	DecimalFormat df = new DecimalFormat("0.0");
	public int broadband(double num){
		double b = 0;
		int a=12;
		if(num>=0&&num<=256){
			b=num/256*a;
		}else if(num>256&&num<=512){
			b=num/512*a+a;
		}else if(num>512&&num<=1024){
			b=num/1024*a+2*a;
		}else if(num>1024&&num<=1536){
			b=num/1536*a+3*a;
		}else if(num>1536&&num<2560){
			b=num/2560*a+4*a;
		}else if(num>2560&&num<=3840){
			b=num/3840*a+5*a;
		}else if(num>3840&&num<=6400){
			b=num/6400*a+6*a;
		}else if(num>6400&&num<12800){
			b=num/10240*a+7*a;
		}else if(num>=12800){
			b=100;
		}
		return (int) b;
		
	}

	private void downSpeedText(double speed, TextView speedView,
			TextView unitView) {
		if (speed >= (1024 * 1024 * 1000)) { // GB
			unitView.setText("GB/s");
			speedView.setText(df.format((speed / (1024 * 1024 * 1024))));
		} else if (speed >= (1024 * 1000)) { // MB
			unitView.setText("MB/s");
			speedView.setText(df.format((speed / (1024 * 1024))));
		} else { // KB
			unitView.setText("KB/s");
			speedView.setText(df.format(speed / 1024));
		}
	}
	private void downSpeedText(double speed, TextView speedView) {
		DecimalFormat df = new DecimalFormat("0.0");
		
		if (speed >= (1024 * 1024 * 1000)) { // GB
			speedView.setText(df.format((speed / (1024 * 1024 * 1024))));
		} else if (speed >= (1024 * 1000)) { // MB
			speedView.setText(df.format((speed / (1024 * 1024))));
		} else { // KB
			speedView.setText(df.format(speed / 1024));
		}
	}
	
	private Handler handler = new Handler() {
		long tem = 0;
		long falg = 0;
		long numberTotal = 0;
		List<Long> list = new ArrayList<Long>();

		@Override
		public void handleMessage(Message msg) {
			int value = msg.what;
			switch (value) {
			case UPDATE_SPEED:
				tvmaxtxt.setVisibility(View.GONE);
//				tvpbtxt.setVisibility(View.GONE);
				tvspeed.setVisibility(View.GONE);
				tvunit.setVisibility(View.GONE);
//				layscale.setVisibility(View.GONE);
				downloadPercent=(int) (netWorkSpeedInfo.hadFinishedBytes *100/netWorkSpeedInfo.totalBytes);
				circlePb.setProgressNotInUiThread(downloadPercent,"");
				tem = netWorkSpeedInfo.speed ;
				list.add(tem);
				int j = 0;
				speedMax = list.get(j);
				speedMin=list.get(j);
				for (int i = 0; i < list.size(); i++) {
					numberTotal += list.get(i);
					if (list.get(i) > speedMax) {
						speedMax = list.get(i);
						j = i;
					}
				}
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) < speedMin) {
						speedMin = list.get(i);
						j = i;
					}
				}
				falg = numberTotal / list.size();
				numberTotal = 0;
				downSpeedText(tem,nowSpeed);
				downSpeedText(falg,avageSpeed);
				downSpeedText(speedMin,min_speed);
				startButton.setText("正在检测...");
				
				int pg=broadband(speedMax/1024);
				horizontalPB.setProgress(pg);
				int x=pg+tvpbtxt.getWidth();
				TranslateAnimation animation=new TranslateAnimation(0, 0, x, 0);
				tvpbtxt.setAnimation(animation);
				break;
			case UPDATE_DNOE:
				downSpeedText(speedMax,tvspeed,tvunit);
				tvspeed.setVisibility(View.VISIBLE);
				tvunit.setVisibility(View.VISIBLE);
				circlePb.setProgressNotInUiThread(100,"");
//				layscale.setVisibility(View.VISIBLE);
//				RelativeLayout.LayoutParams pa=(RelativeLayout.LayoutParams) tvpbtxt.getLayoutParams();
//				pa.setMargins(x,0,0,0);
//				tvpbtxt.setLayoutParams(pa);
//				tvpbtxt.setVisibility(View.VISIBLE);
				tvmaxtxt.setVisibility(View.VISIBLE);
				startButton.setText("重新检测");
				showToast("检测完成", Toast.LENGTH_SHORT);
//				tvpbtxt.setText("下载速度："+"\n"+df.format(speedMax/1024/128)+"Mb/s"+pg);
				tvpbtxt.setText(df.format(speedMax/1024/128)+"Mb/s");
				break;
			default:
				break;
			}
		}
	};

}
