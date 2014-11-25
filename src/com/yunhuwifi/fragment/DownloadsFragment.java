package com.yunhuwifi.fragment;


import java.util.ArrayList;
import java.util.List;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.models.RouterState;
import com.yunhuwifi.view.ListViewAdapter;
import com.yunhuwifi.view.ListViewItem;

import android.app.Dialog;
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
		RouterState item1=new RouterState();
		item1.setMsg("匹诺曹" );
		RouterState item2=new RouterState();
		item2.setMsg("飞虎2" );
		listData.add(item2);
		listData.add(item1);
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
