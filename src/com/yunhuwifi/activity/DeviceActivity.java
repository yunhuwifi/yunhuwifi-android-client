package com.yunhuwifi.activity;

import java.util.ArrayList;
import java.util.List;

import com.foxrouter.api.RouterModuleDevice;
import com.foxrouter.api.RouterResponseDeviceList;
import com.foxrouter.api.model.RouterDevice;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.RouterContext;
import com.yunhuwifi.handlers.JsonCallBack;
import com.yunhuwifi.util.CustomDialog;
import com.yunhuwifi.view.ListViewAdapter;
import com.yunhuwifi.view.ListViewItem;
import com.yunhuwifi.view.PullDownListView;
import com.yunhuwifi.view.PullDownListView.OnRefreshListioner;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class DeviceActivity extends HeaderActivity implements OnClickListener, OnRefreshListioner {
	private PullDownListView pullList;
	private ListView lvdevice;
	private List<ListViewItem> list;
	private ListViewAdapter adapter;
	static final private int REQUESTONE = 0;
	static final private int REQUESTTOW = 1;

	Handler handler = new Handler() ;
	public void handleMessage(android.os.Message msg) {
		if (msg.what == 1) {
			Log.d("tag","共" + msg.arg1 + "条，已选" + msg.arg2 + "条");
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_device);
		this.setHeaderText("设备管理");

		this.setLeftImageVisible(false);
		this.setRightImageVisible(true);
		this.ivRight.setBackgroundResource(R.drawable.blacklisticon);
		this.ivRight.setOnClickListener(this);
//		this.lvdevice = (ListView) findViewById(R.id.lvDeivce);
		pullList=(PullDownListView) findViewById(R.id.pullList);
		pullList.setRefreshListioner(this);
		lvdevice=pullList.mListView;
		list = new ArrayList<ListViewItem>();

		init();
	}

	private void init() {
		RouterContext routerContext = getApplicationContext()
				.getRouterContext();
		if (routerContext == null) {
			showToast("未连接路由器", Toast.LENGTH_SHORT);
			return;
		}
		final Dialog loading = CustomDialog.createLoadingDialog(
				DeviceActivity.this, "");
		loading.show();
		RouterModuleDevice device = new RouterModuleDevice(routerContext);
		device.list(new JsonCallBack<RouterResponseDeviceList>(
				RouterResponseDeviceList.class) {

			public void onJsonSuccess(RouterResponseDeviceList t) {
				loading.dismiss();
				if (t.getResult() != null) {
					list.addAll(t.getResult());

					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				loading.dismiss();
				showToast(strMsg, Toast.LENGTH_SHORT);
			}

		});

		adapter = new ListViewAdapter(DeviceActivity.this, list,4,handler);
		lvdevice.setAdapter(adapter);
		lvdevice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(DeviceActivity.this,
						DeviceDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("device",
						(RouterDevice) list.get((int) id));
				bundle.putString("did", DeviceActivity.this.toString());
				intent.putExtras(bundle);
				startActivityForResult(intent, REQUESTONE);
			}

		});

		// 长按事件
		lvdevice.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg, long arg3) {
				Intent t = new Intent(DeviceActivity.this,
						DeviceLongPressActivity.class);
				Bundle b = new Bundle();
				b.putSerializable("device", (RouterDevice) list.get((int) arg3));
				b.putString("longDevice", DeviceActivity.this.toString());
				t.putExtras(b);
				startActivityForResult(t, REQUESTTOW);
				return true;
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_ivRight:
			startActivity(new Intent(DeviceActivity.this,
					BlackListActivity.class));
			break;
		}

	}

	// 再按一次返回键退出
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		case REQUESTTOW:
			if(resultCode==RESULT_OK){
				adapter.notifyDataSetChanged();
				if (data != null) {
					showToast("返回的结果:\n" + data.getAction(), Toast.LENGTH_SHORT);
				}
			}
			break;
		case REQUESTONE:
			if(resultCode==RESULT_OK){
				adapter.notifyDataSetChanged();
			}
			break;
		}
	}
	@Override
	public void onRefresh() {
		handler.postDelayed(new Runnable() {
			
			public void run() {
				pullList.onRefreshComplete();
				adapter.notifyDataSetChanged();	
				
			}
		}, 1500);		
	}
}
