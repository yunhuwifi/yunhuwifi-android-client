package com.yunhuwifi.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.models.NotinstallItem;
import com.yunhuwifi.view.ListViewAdapter;
import com.yunhuwifi.view.ListViewItem;
import com.yunhuwifi.view.PullDownListView;
import com.yunhuwifi.view.PullDownListView.OnRefreshListioner;

public class NotinstallFragment extends Fragment implements OnRefreshListioner{
	private ListView lvapps; 
	private List<ListViewItem> listNotinstall;
	private PullDownListView pullnotinstall;
	ListViewAdapter adapter ;
	Handler handler = new Handler() ;
	public void handleMessage(android.os.Message msg) {
		if (msg.what == 1) {
			Log.d("tag","共" + msg.arg1 + "条，已选" + msg.arg2 + "条");
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_notinstallapp, container, false);
		pullnotinstall=(PullDownListView) view.findViewById(R.id.pullnotinstall);
		lvapps =pullnotinstall.mListView; 
		init();
		return view;
	}

 
	private void init() {
		 listNotinstall = new ArrayList<ListViewItem>();
		 NotinstallItem item1=new NotinstallItem();
		 NotinstallItem item2=new NotinstallItem();
		 item1.setMsg("观看最新剧集");
		 item1.setName("优酷追剧");
		 item2.setMsg("baidu.apk");
		 item2.setName("修改时间:2014-11-21");
		 listNotinstall.add(item2);
		 listNotinstall.add(item1);

		 adapter = new ListViewAdapter(getActivity(), listNotinstall, 6,handler);
		lvapps.setAdapter(adapter);
		lvapps.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 switch (position) {
		            case 0:
		            	Toast.makeText(getActivity(), getTag(), Toast.LENGTH_SHORT).show();
		                break;
		 
		            default:
		                break;
		            }
				
			}
		});
	}
	@Override
	public void onRefresh() {
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				pullnotinstall.onRefreshComplete();
				adapter.notifyDataSetChanged();
			}
		}, 1500);
		
	}
	

}