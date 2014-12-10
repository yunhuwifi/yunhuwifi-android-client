package com.yunhuwifi.activity;

import java.util.ArrayList;
import java.util.List;

import com.foxrouter.api.RouterModuleWifi;
import com.foxrouter.api.RouterResponseWifiList;
import com.foxrouter.api.model.RouterWifiDetails;
import com.foxrouter.api.model.RouterWifiViewModel;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.handlers.JsonCallBack;
import com.yunhuwifi.util.CustomDialog;
import com.yunhuwifi.view.ListViewAdapter;
import com.yunhuwifi.view.ListViewItem;

import android.os.Bundle;
import android.os.Handler;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.widget.ListView;

public class WifiListActivity extends HeaderActivity {
	
	private ListView lvWifi;
	private ListViewAdapter adapter;
	private List<ListViewItem> list;
	private int REQUEST;
	Handler handler = new Handler() ;
	public void handleMessage(android.os.Message msg) {
		if (msg.what == 1) {
			Log.d("tag","共" + msg.arg1 + "条，已选" + msg.arg2 + "条");
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_wifi_list);
		this.list = new ArrayList<ListViewItem>();
		this.lvWifi = (ListView) findViewById(R.id.lvWifi);
		this.adapter = new ListViewAdapter(this.getApplicationContext(), list,R.layout.listview_item_wifi,handler);
		this.lvWifi.setAdapter(adapter);
		this.setLeftImageVisible(true);
		this.setHeaderText("无线");
		this.ivLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		this.lvWifi.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(WifiListActivity.this,
						WifiSetActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("model",(RouterWifiViewModel) list.get((int) id));
				intent.putExtras(bundle);
				startActivityForResult(intent, REQUEST);

			}
		});

		initView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == ACTIVITY_RESULT_SUCCESS&&requestCode==REQUEST) {
			initView();
		}
	}

	private void initView() {
		final Dialog loading=CustomDialog.createLoadingDialog(WifiListActivity.this,"");
		loading.show();
		RouterModuleWifi wifi = new RouterModuleWifi(getApplicationContext()
				.getRouterContext());

		wifi.list(new JsonCallBack<RouterResponseWifiList>(
				RouterResponseWifiList.class) {

			@Override
			public void onJsonSuccess(RouterResponseWifiList t) {
				loading.dismiss();
				if (t.getResult() != null) {
					list.clear();
					for (RouterWifiDetails model : t.getResult()) {
						if (model.getIwdata() != null) {
							RouterWifiViewModel item = new RouterWifiViewModel();
							item.setNetid(model.getNetid());
							item.setSsid(model.getIwdata().getSsid());
							item.setEncryption(model.getIwdata()
									.getEncryption());
							item.setKey(model.getIwdata().getKey());
							list.add(item);
						}
					}
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				loading.dismiss();
				showToast(strMsg, Toast.LENGTH_SHORT);
			}
		});
	}
}

	/*holder.tgOpen.setOnCheckedChangeListener(new ChangeListener(
					position));
		private class ChangeListener implements OnCheckedChangeListener {

			int mPosition;

			ChangeListener(int inPosition) {
				mPosition = inPosition;
			}

			RouterWifiViewModel wifimodel = list.get(mPosition);

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					
					  RouterModuleWifi wifi =new
					  RouterModuleWifi(getApplicationContext());
					  wifi.closeWifi(wifimodel.getNetid(), new
					  RouterResponseHandler
					  <RouterResponseResult>(RouterResponseResult.class) {
					  
					  protected void succeed(RouterResponseResult resp) { if
					  (resp.isResult()) {
					  holder.tgOpen.setChecked(wifimodel.isEnable()); } }
					  
					  @Override protected void failed(int code, String message)
					  { Toast.makeText(WifiListActivity.this, message,
					  Toast.LENGTH_SHORT).show(); } });
					 
				} else {
					
					  RouterModuleWifi wifi =new
					  RouterModuleWifi(getApplicationContext());
					  wifi.closeWifi(wifimodel.getNetid(), new
					  RouterResponseHandler
					  <RouterResponseResult>(RouterResponseResult.class) {
					  
					  protected void succeed(RouterResponseResult resp) { if
					  (resp.isResult()) { } }
					  
					  @Override protected void failed(int code, String message)
					  { Toast.makeText(WifiListActivity.this, message,
					  Toast.LENGTH_SHORT).show(); } });
					 
				}
			}
		}*/


