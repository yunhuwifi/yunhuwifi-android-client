package com.yunhuwifi.fragment;


import java.util.ArrayList;
import java.util.List;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.models.RouterState;
import com.yunhuwifi.view.ListViewItem;
import com.yunhuwifi.view.ListViewAdapter;
 
import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class DownloadedFragment extends Fragment {
	private ListView lvdownloaded;
	private List<ListViewItem> listData;
	private ListViewAdapter adapter;
	public static String tvSelected="";
	Handler handler = new Handler() ;
	public void handleMessage(android.os.Message msg) {
		if (msg.what == 1) {
			tvSelected="共" + msg.arg1 + "条，已选" + msg.arg2 + "条";
		}
	};

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_downloaded, container, false);
		lvdownloaded = (ListView) v.findViewById(R.id.lvdownloaded);
		init();
		return v;
	}
	
	private void init(){
		listData=new ArrayList<ListViewItem>();
		RouterState item1=new RouterState();
		item1.setImg(BitmapFactory.decodeResource(getResources(), R.drawable.launcher));
		item1.setTime("186MB");
		item1.setMsg("匹诺曹" );
		RouterState item2=new RouterState();
		item2.setImg(BitmapFactory.decodeResource(getResources(), R.drawable.launcher));
		item2.setTime("165");
		item2.setMsg("飞虎2" );
		listData.add(item2);
		listData.add(item1);
		adapter=new ListViewAdapter(getActivity(), listData,R.layout.listview_item_file,handler);
		lvdownloaded.setAdapter(adapter);
		lvdownloaded.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
		lvdownloaded.setOnItemLongClickListener(new OnItemLongClickListener() {

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
		View dlgView = View.inflate(getActivity(),  R.layout.file_longpress_box,null);
		View layreopen = dlgView.findViewById(R.id.layitem0); 
		layreopen.setTag(dlg);  
		TextView tvopen = (TextView)dlgView.findViewById(R.id.tv0); 
		tvopen.setText("打开"); 
		TextView tvdel = (TextView)dlgView.findViewById(R.id.tv1); 
		tvdel.setText("删除");
		layreopen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dlg.dismiss(); 
			} 
		});
		View laydel = dlgView.findViewById(R.id.layitem1);
		laydel.setTag(dlg);
		laydel.setOnClickListener(new OnClickListener() {
			
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
