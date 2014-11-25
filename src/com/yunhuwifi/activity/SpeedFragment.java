package com.yunhuwifi.activity;

import java.io.File;
import java.text.DecimalFormat;

import com.foxrouter.api.RouterModuleNetwork;
import com.foxrouter.api.RouterResponseSpeedStatus;
import com.foxrouter.api.model.RouterSpeedStatus;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.YunhuApplication;
import com.yunhuwifi.handlers.JsonCallBack;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SpeedFragment extends Fragment {
	private final static int DELAY_TIME = 3000;
	private TextView tvDownloadSpeed, tvDownloadUnit, tvUploadSpeed,
			tvUploadUnit,tvstorage,tvSpeed;
	private Handler speedHandler;
	private Runnable runnable;
	private Button storage,detector;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_speed, container, false);
		tvstorage=(TextView) view.findViewById(R.id.tvstorage);
		tvSpeed=(TextView) view.findViewById(R.id.tvSpeed);
		storage=(Button) view.findViewById(R.id.storage);
		detector=(Button) view.findViewById(R.id.detector);
		detector.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),TestSpeedActivity.class));
			}
		});
		storage.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),FileActivity.class));
				
			}
		});
		this.tvDownloadSpeed = (TextView) view.findViewById(R.id.tvDownloadSpeed);
		this.tvDownloadUnit = (TextView) view.findViewById(R.id.tvDownloadUnit);
		this.tvUploadSpeed = (TextView) view.findViewById(R.id.tvUploadSpeed);
		this.tvUploadUnit = (TextView) view.findViewById(R.id.tvUploadUnit);
		Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"font/font.otf");
		tvUploadSpeed.setTypeface(typeface);
		tvDownloadSpeed.setTypeface(typeface);
		tvDownloadUnit.setTypeface(typeface);
		tvUploadUnit.setTypeface(typeface);
		tvstorage.setTypeface(typeface);
		tvSpeed.setTypeface(typeface);
		tvUploadSpeed.setText(getMemorySize()+getSDSize()+"");
		this.speedHandler = new Handler();

		this.runnable = new Runnable() {

			@Override
			public void run() {
				loadSpeed();
			}

		};

		return view;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.speedHandler.post(this.runnable);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.speedHandler.removeCallbacks(this.runnable);
	}

	public float getMemorySize()
	{
	File path = Environment.getDataDirectory();
	StatFs stat = new StatFs(path.getPath());
	long blockSize = stat.getBlockSize();
	long availableBlocks = stat.getAvailableBlocks();
	return (availableBlocks * blockSize)*1.0f/(1024*1024*1024);
	}

	public float getSDSize()
	{
	String state = Environment.getExternalStorageState();
	//SD卡不可用
	if (!Environment.MEDIA_MOUNTED.equals(state))
	{
	   return 0;

	}

	File path = Environment.getExternalStorageDirectory();
	StatFs stat = new StatFs(path.getPath());
	long blockSize = stat.getBlockSize();
	long availableBlocks = stat.getAvailableBlocks();
	return (availableBlocks * blockSize)*1.0f/(1024*1024*1024);
	 
	}


	
	private void loadSpeed() {
		YunhuApplication context = (YunhuApplication) super.getActivity()
				.getApplicationContext();
		RouterModuleNetwork network = new RouterModuleNetwork(
				context.getRouterContext());
		if (context.getRouterContext() != null) {
			network.speedStatus(new JsonCallBack<RouterResponseSpeedStatus>(
					RouterResponseSpeedStatus.class) {

				@Override
				public void onJsonSuccess(RouterResponseSpeedStatus t) {
					if (t.getResult() != null) {
						showSpeed(t.getResult());
					}

					speedHandler.postDelayed(runnable, DELAY_TIME);

				}

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					speedHandler.postDelayed(runnable, DELAY_TIME);
				}
			});
		}
	}

	private void showSpeed(RouterSpeedStatus result) {
		showSpeedText(result.getTxs(), this.tvDownloadSpeed,
				this.tvDownloadUnit);
//		showSpeedText(result.getRxs(), this.tvUploadSpeed, this.tvUploadUnit);
	}

	private void showSpeedText(double speed, TextView speedView,
			TextView unitView) {
		DecimalFormat df = new DecimalFormat("0.0");

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
}
