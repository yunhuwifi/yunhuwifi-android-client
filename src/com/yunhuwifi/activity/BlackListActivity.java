package com.yunhuwifi.activity;

import java.util.ArrayList;
import java.util.List;

import com.foxrouter.api.model.RouterBlacklist;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.view.ListViewAdapter;
import com.yunhuwifi.view.ListViewItem;
import com.yunhuwifi.view.PullDownListView;
import com.yunhuwifi.view.PullDownListView.OnRefreshListioner;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BlackListActivity extends BaseActivity implements OnClickListener, OnRefreshListioner {
	private PullDownListView pullList;
	private ListView lvBlackList;
	private ListViewAdapter adapter;
	private List<ListViewItem> listData = new ArrayList<ListViewItem>();
	private ImageView header_ivLeft, header_ivRight;
	Handler handler = new Handler() ;
	public void handleMessage(android.os.Message msg) {
		if (msg.what == 1) {
			Log.d("tag","共" + msg.arg1 + "条，已选" + msg.arg2 + "条");
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blacklist);
		TextView headerText = (TextView) findViewById(R.id.header_txtView);
		header_ivLeft = (ImageView) findViewById(R.id.header_ivLeft);
		header_ivRight = (ImageView) findViewById(R.id.header_ivRight);
		this.header_ivRight.setVisibility(View.INVISIBLE);
		header_ivLeft.setOnClickListener(this);
		headerText.setText("黑名单");
//		this.lvBlackList = (ListView) findViewById(R.id.lvBlackList);
		pullList=(PullDownListView) findViewById(R.id.pullBlackList);
		pullList.setRefreshListioner(this);
		lvBlackList=pullList.mListView;
		setListData();
	}

	private void setListData() {
		RouterBlacklist item=new RouterBlacklist();
		item.setMsg("127.23.3.1");
		listData.add(item);
		adapter=new ListViewAdapter(BlackListActivity.this, listData,2,handler);
		adapter.notifyDataSetChanged();
		this.lvBlackList.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.header_ivLeft) {
			finish();
		}
	}
	@Override
	public void onRefresh() {
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				pullList.onRefreshComplete();
				adapter.notifyDataSetChanged();
			}
		}, 1500);
		
	}

}
