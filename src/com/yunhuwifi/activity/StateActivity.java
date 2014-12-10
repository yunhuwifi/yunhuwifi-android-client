package com.yunhuwifi.activity;

import java.util.ArrayList;
import java.util.List;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.models.RouterState;
import com.yunhuwifi.service.NetWorkListenService;
import com.yunhuwifi.service.NetWorkListenService.NetworkAvailableListener;
import com.yunhuwifi.view.ListViewAdapter;
import com.yunhuwifi.view.ListViewItem;
import com.yunhuwifi.view.PullDownListView;
import com.yunhuwifi.view.PullDownListView.OnRefreshListioner;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class StateActivity extends BaseFragmentActivity implements OnClickListener ,OnRefreshListioner,NetworkAvailableListener{
	private static final String TAG = StateActivity.class.getSimpleName();
	private PullDownListView pullList;
	private ListView lvState;
	private RelativeLayout laynonet;
	private DisplayMetrics dm;
	private int AGAIN_CODE=1;
	private List<ListViewItem> listState; 
	private LinearLayout linearlayoperate;
	private ListViewAdapter adapter; 
	private  NetWorkListenService netListen;
	private  Button btndeleted,btncancel,btnclear;
	private RadioButton  radiobtnall; 
	private TextView tvheaderstate;
	Handler handler = new Handler() ;
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
			}
		};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_state);
		netListen=new NetWorkListenService(this);
		netListen.bind(this);
		if(netListen.hasConnection()){
			showToast("网络正常", Toast.LENGTH_SHORT);
		}else{
			showToast("网络不可用",Toast.LENGTH_SHORT);
		}
		pullList=(PullDownListView) findViewById(R.id.pulllist);
		pullList.setRefreshListioner(this);
		lvState=pullList.mListView;
		btndeleted=(Button) findViewById(R.id.btndeleted);
		btncancel=(Button) findViewById(R.id.btncancel);
		radiobtnall=(RadioButton) findViewById(R.id.radiobtnall);
		btnclear=(Button) findViewById(R.id.btnclear);
		laynonet=(RelativeLayout) findViewById(R.id.laynonet);
		tvheaderstate=(TextView) findViewById(R.id.tvheaderstate);
		btndeleted.setOnClickListener(this);
		btncancel.setOnClickListener(this);
		btnclear.setOnClickListener(this);
		radiobtnall.setOnClickListener(this);
		linearlayoperate=(LinearLayout) findViewById(R.id.linearlayoperate);
		laynonet.setOnClickListener(this);
		if (this.getApplicationContext().getRouterContext() == null
				|| this.getApplicationContext().getRouterContext().getRouter() == null) {
			tvheaderstate.setText("未连接云狐路由器");
		} else {
			tvheaderstate.setText("云狐路由器");
//			this.setHeaderText(this.getApplicationContext().getRouterContext()
//					.getRouter().getHostname());
		}
	      // 获取屏幕尺寸
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
		init();
		judge();
	}

	protected void onResume(){
		super.onResume();
		netListen.bind(this);
	}
	
	protected void onPause(){
		super.onPause();
		netListen.unbind(this);
	}
	private void  judge(){
	
			adapter = new ListViewAdapter(getApplicationContext(),listState,R.layout.listview_item_state,handler);
			adapter.notifyDataSetChanged();
			lvState.setAdapter(adapter);
			lvState.setOnItemClickListener(new OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
				}
			});
			
			lvState.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						int position, long id) {
		        		linearlayoperate.setVisibility(View.VISIBLE);
		        		ListViewAdapter.visable = true;
			        	adapter.notifyDataSetChanged();
			            return true;
				}
			});
			
	}
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.btncancel:
			cancel();
			break;
		case R.id.radiobtnall:
			ListViewAdapter.visable=true;
			for(int i=0;i<adapter.flags.size();i++){
				adapter.flags.set(i, true);
			}
			adapter.notifyDataSetChanged();
			break;
		case R.id.laynonet:
			if (android.os.Build.VERSION.SDK_INT > 10) {
				Intent intent = new Intent(
						android.provider.Settings.ACTION_WIFI_SETTINGS);
				startActivityForResult(intent,AGAIN_CODE);
			} else {
				Intent intent = new Intent(
						android.provider.Settings.ACTION_SETTINGS);
				startActivityForResult(intent, AGAIN_CODE);
			}
			break;
		case R.id.btndeleted:
			final List<Integer> result=new ArrayList<Integer>();
			for (int i = 0; i < adapter.getCount(); i++) {
				if(adapter.flags.get(i)){
					result.add(i);
				}
			}
			if(result==null || result.size()==0){
				Toast.makeText(this, "请选中要删除的项", Toast.LENGTH_SHORT).show();
				return;
			}
			
			final Dialog dlg = new Dialog(this);
			View cleardlgView = View.inflate(this,  R.layout.confirm_box,null);
			View oklink = cleardlgView.findViewById(R.id.ok); 
			oklink.setTag(dlg); 
			TextView dialogtitle = (TextView)cleardlgView.findViewById(R.id.dialogtitle); 
			dialogtitle.setText("删除选中项");
			TextView dialogmessage = (TextView)cleardlgView.findViewById(R.id.dialogmessage); 
			dialogmessage.setText("您确定要删除选中项？");
			
			oklink.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dlg.dismiss();
					for (int i = result.size()-1; i>=0 ;i--) {
						int p=result.get(i);
						listState.remove(p);
					}
					adapter=new ListViewAdapter(StateActivity.this, listState,0,handler);
					lvState.setAdapter(adapter);
				} 
			});
			View cancellink = cleardlgView.findViewById(R.id.cancel);
			cancellink.setTag(dlg);
			cancellink.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dlg.dismiss();
				}
			});
			
			dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dlg.setCanceledOnTouchOutside(true);
			dlg.setContentView(cleardlgView);
			dlg.show();
			ListViewAdapter.visable=true;
			adapter.notifyDataSetChanged();
			break;
		case R.id.btnclear:  
				final Dialog cleandlg = new Dialog(this); 
				View clearndlgView = View.inflate(this,  R.layout.confirm_box,null);
				View clearoklink = clearndlgView.findViewById(R.id.ok); 
				clearoklink.setTag(cleandlg); 
				TextView cleardialogtitle = (TextView)clearndlgView.findViewById(R.id.dialogtitle); 
				cleardialogtitle.setText("提示");
				TextView cleardialodmessage = (TextView)clearndlgView.findViewById(R.id.dialogmessage); 
				cleardialodmessage.setText("您确定要清空所有消息吗？"); 
				
				clearoklink.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						cleandlg.dismiss();
						listState.clear();
						adapter=new ListViewAdapter(StateActivity.this, listState,0,handler);
						lvState.setAdapter(adapter);
					} 
				});
				View clearcancellink = clearndlgView.findViewById(R.id.cancel); 
				clearcancellink.setTag(cleandlg);
				clearcancellink.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						cleandlg.dismiss();
					}
				});
				
				cleandlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
				cleandlg.setCanceledOnTouchOutside(true);
				cleandlg.setContentView(clearndlgView);
				cleandlg.show();
				
				ListViewAdapter.visable=true;
				adapter.notifyDataSetChanged();
				break;
			}
	}

	private void  cancel(){
		linearlayoperate.setVisibility(View.GONE);
		ListViewAdapter.visable=false;
		for(int i=0;i<adapter.flags.size();i++){
			adapter.flags.set(i, false);
		}
		adapter.notifyDataSetChanged();
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
				return false;
			}
			return super.onKeyDown(keyCode, event);
		}
		
    
		/*private void setHigh(){
			LinearLayout.LayoutParams  laysp=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
			laysp.height=dm.widthPixels/2;
			laySpeed.setLayoutParams(laysp);
			laySpeed.setBackgroundDrawable(null);
			laystate.setBackgroundResource(R.drawable.nonoticebg);
		}*/
		private void init() {
			listState = new ArrayList<ListViewItem>();
			RouterState item1=new RouterState();
			item1.setMsg("已是最新版本");
			item1.setTime("2012-12-3");
			RouterState item4=new RouterState();
			item4.setMsg("新连接一个设备");
			item4.setTime("2012-12-3");
			RouterState item2=new RouterState();
			item2.setMsg("离线下载");
			item2.setTime("2012-12-3");
			RouterState item3=new RouterState();
			item3.setMsg("已是最新版本");
			item3.setTime("2012-12-3");
			listState.add(item3);
			listState.add(item1);
			listState.add(item2);
			listState.add(item4);

		}
    
		
		public void onRefresh() {
			handler.postDelayed(new Runnable() {
				
				public void run() {
					pullList.onRefreshComplete();
					adapter.notifyDataSetChanged();	
					
				}
			}, 1500);
			
			
		}
		
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			Log.d("request code", requestCode + "");
			Log.d("resultCode code", resultCode + "");
			if (requestCode ==AGAIN_CODE
					&& resultCode == ACTIVITY_RESULT_FAILURE) {
				laynonet.setVisibility(View.GONE);
			}
		}
		
		@Override
		public void networkAvailable() {
			laynonet.setVisibility(View.GONE);
		}

		@Override
		public void networkUnavailable() {
			laynonet.setVisibility(View.VISIBLE);
			
		}
		
}
