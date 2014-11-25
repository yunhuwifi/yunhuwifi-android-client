package com.yunhuwifi.fragment;


import java.util.ArrayList;
import java.util.List;

import com.yunhuwifi.activity.DownloadActivity;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.models.RouterState;
import com.yunhuwifi.view.ListViewItem;
import com.yunhuwifi.view.ListViewAdapter;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadedFragment extends Fragment {
	private Button btnDel;
	private ListView lvdownloaded;
	private List<ListViewItem> listData;
	private ListViewAdapter adapter;
	private LinearLayout layopreatedel;
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
		/*layopreatedel=(LinearLayout) v.findViewById(R.id.layopreatedel);
		btnDel=(Button) v.findViewById(R.id.btndel);
		btnDel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

					final List<Integer> result=new ArrayList<Integer>();
					for (int i = 0; i < adapter.getCount(); i++) {
						if(adapter.flags.get(i)){
							result.add(i);
						}
					}
					if(result==null || result.size()==0){
						Toast.makeText(getActivity(), "请选中要删除的项", Toast.LENGTH_SHORT).show();
						return;
					}
					final Dialog dlg = new Dialog(getActivity());
					View dlgView = View.inflate(getActivity(),  R.layout.confirm_box,null);
					View oklink = dlgView.findViewById(R.id.ok); 
					oklink.setTag(dlg); 
					TextView dialogtitle = (TextView)dlgView.findViewById(R.id.dialogtitle); 
					dialogtitle.setText("删除选中项");
					TextView dialogmessage = (TextView)dlgView.findViewById(R.id.dialogmessage); 
					dialogmessage.setText("您确定要删除选中项？");
					
					oklink.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dlg.dismiss();
							for (int i = result.size()-1; i>=0 ;i--) {
								int p=result.get(i);
								listData.remove(p);
							}
							adapter=new ListViewAdapter(getActivity(), listData,1,handler);
							lvdownloaded.setAdapter(adapter);
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
					ListViewAdapter.visable=true;
					adapter.notifyDataSetChanged();
					}
		});*/
		init();
		return v;
	}
	
	private void init(){
		listData=new ArrayList<ListViewItem>();
		RouterState download1=new RouterState();
		download1.setMsg("露水红颜");
		listData.add(download1);
		RouterState download2=new RouterState();
		download2.setMsg("一生一世");
		listData.add(download2);
		RouterState download3=new RouterState();
		download3.setMsg("亲爱的");
		listData.add(download3);
		RouterState download4=new RouterState();
		download4.setMsg("闺蜜");
		listData.add(download4);
		adapter=new ListViewAdapter(getActivity(), listData,5,handler);
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
			/*	if(ListViewAdapter.visable){
					layopreatedel.setVisibility(View.VISIBLE);
					DownloadActivity.operateVisible=true;
				}else{
					ListViewAdapter.visable=true;
				}*/
				return false;
			}
		});
	}
	
	private void puplongbox(){
		final Dialog dlg = new Dialog(getActivity());
		View dlgView = View.inflate(getActivity(),  R.layout.downloads_longpress_box,null);
		View layrename = dlgView.findViewById(R.id.layopen); 
		layrename.setTag(dlg); 
		layrename.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dlg.dismiss(); 
			} 
		});
		View laydisconnect = dlgView.findViewById(R.id.laydel);
		laydisconnect.setTag(dlg);
		laydisconnect.setOnClickListener(new OnClickListener() {
			
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
