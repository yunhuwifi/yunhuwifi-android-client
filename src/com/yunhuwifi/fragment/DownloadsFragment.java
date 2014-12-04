package com.yunhuwifi.fragment;


import java.util.ArrayList;
import java.util.List;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.models.RouterDownload;
import com.yunhuwifi.view.ListViewAdapter;
import com.yunhuwifi.view.ListViewItem;

import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class DownloadsFragment extends Fragment {

	private ListView lvnotdownload;
	private List<ListViewItem> listData;
	private ListViewAdapter adapter;
	
	public Handler handler=new Handler();
	public void handleMessage(android.os.Message msg){
		if(msg.what==1){
			
		}
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_notdownload, container, false);
		lvnotdownload = (ListView) v.findViewById(R.id.lvnotdownload);
		init();
		return v;
	}
	
	private void  init(){
		listData=new ArrayList<ListViewItem>();
		
		RouterDownload download1=new RouterDownload();
		download1.setDownloadtitle("变形金刚4");
		download1.setDownloadpercent("86%");
		download1.setDownloadBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.item0));
		download1.setDownloadprogress(86);
		download1.setDownloadsize("653MB");
		download1.setDownloadspeed("312kB/s");
		download1.setDownloadstate("下载中...");
		listData.add(download1);
		
		RouterDownload download2=new RouterDownload();
		download2.setDownloadtitle("星球崛起2");
		download2.setDownloadpercent("65%");
		download2.setDownloadBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.item1));
		download2.setDownloadprogress(65);
		download2.setDownloadsize("594MB");
		download2.setDownloadspeed("212kB/s");
		download2.setDownloadstate("下载中...");
		listData.add(download2);
		
		RouterDownload download3=new RouterDownload();
		download3.setDownloadtitle("狂怒");
		download3.setDownloadpercent("58%");
		download3.setDownloadBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.item2));
		download3.setDownloadprogress(58);
		download3.setDownloadsize("526MB");
		download3.setDownloadspeed("192kB/s");
		download3.setDownloadstate("下载中...");
		listData.add(download3);
		
		RouterDownload download4=new RouterDownload();
		download4.setDownloadtitle("使徒行者第1集");
		download4.setDownloadpercent("26%");
		download4.setDownloadBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.item3));
		download4.setDownloadprogress(26);
		download4.setDownloadsize("101MB");
		download4.setDownloadspeed("96kB/s");
		download4.setDownloadstate("下载中...");
		listData.add(download4);
		
		
		adapter=new ListViewAdapter(getActivity(), listData, 1, handler);
		lvnotdownload.setAdapter(adapter);
		lvnotdownload.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
		lvnotdownload.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				puplongbox();
				return false;
			}
		});
	}
	
	private void puplongbox(){
		final Dialog dlg = new Dialog(getActivity());
		View dlgView = View.inflate(getActivity(),  R.layout.device_longpress_box,null);
		View layrename = dlgView.findViewById(R.id.laychangeremark); 
		layrename.setTag(dlg); 
		TextView tvcontinue = (TextView)dlgView.findViewById(R.id.tvrevise); 
		tvcontinue.setText("继续"); 
		TextView tvstop = (TextView)dlgView.findViewById(R.id.tvbreak); 
		tvstop.setText("暂停");
		TextView tvdel = (TextView)dlgView.findViewById(R.id.tvoperate); 
		tvdel.setText("删除");
		layrename.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dlg.dismiss(); 
			} 
		});
		View laydisconnect = dlgView.findViewById(R.id.laydisconnect);
		laydisconnect.setTag(dlg);
		laydisconnect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dlg.dismiss();
			}
		});
		View layaddblack = dlgView.findViewById(R.id.layaddblack); 
		layaddblack.setTag(dlg);
		layaddblack.setOnClickListener(new OnClickListener() {
			
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
