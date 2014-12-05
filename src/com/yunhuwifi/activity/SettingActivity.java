package com.yunhuwifi.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.models.RouterSet;
import com.yunhuwifi.view.ListViewAdapter;
import com.yunhuwifi.view.ListViewItem;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;
import android.graphics.BitmapFactory;

public class SettingActivity extends HeaderActivity implements OnClickListener {
	FeedbackAgent agent;
	private ListView lvSet;
	private List<ListViewItem> data = new ArrayList<ListViewItem>();;
	private ListViewAdapter adapter;
	private String name = null;
	Handler handler = new Handler() ;
	public void handleMessage(android.os.Message msg) {
		if (msg.what == 1) {
			Log.d("tag","共" + msg.arg1 + "条，已选" + msg.arg2 + "条");
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_setting);
		this.setHeaderText("设置");
		this.setLeftImageVisible(false);

		agent = new FeedbackAgent(this);
		agent.sync();
		lvSet = (ListView) findViewById(R.id.lvSet);

		/*
		 * if (this.getApplicationContext().getUserContext() != null) {
		 * this.tvAccount.setText(this.getApplicationContext()
		 * .getUserContext().getUser().getUsername()); }
		 */
		initdata();

	}

	private void checkUpdate() {
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse arg1) {

				
				  switch (updateStatus) {
			        case UpdateStatus.Yes: 
			            UmengUpdateAgent.showUpdateDialog(SettingActivity.this, null);
			            Toast.makeText(SettingActivity.this, "有更新", Toast.LENGTH_SHORT).show();
			            break;
			        case UpdateStatus.No: 
			            Toast.makeText(SettingActivity.this, "没有更新", Toast.LENGTH_SHORT).show();
			            break;
			        case UpdateStatus.NoneWifi: 
			            Toast.makeText(SettingActivity.this, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
			            break;
			        case UpdateStatus.Timeout: 
			            Toast.makeText(SettingActivity.this, "超时", Toast.LENGTH_SHORT).show();
			            break;
			        }
				
				
				Gson gson = new Gson();
				String json = gson.toJson(arg1);
				Log.d("json", json);
				showToast(arg1.version, Toast.LENGTH_SHORT);
			}

		});

		UmengUpdateAgent.forceUpdate(this);
	}

	private void initdata() {
		if (this.getApplicationContext().getUserContext() != null) {
			name = this.getApplicationContext().getUserContext().getUser()
					.getUsername();
		}
		
		RouterSet item1=new RouterSet();
		RouterSet item2=new RouterSet();
		RouterSet item3=new RouterSet();
		RouterSet item4=new RouterSet();
		RouterSet item5=new RouterSet();
		
		item1.setIconRes(BitmapFactory.decodeResource(this.getResources(), R.drawable.accounticon));
		item1.setMsg("账号管理");
//		item1.setViewOpreate(R.drawable.nextbg);
		item2.setIconRes(BitmapFactory.decodeResource(this.getResources(), R.drawable.communicationicon));
		item2.setMsg("我的路由器");
//		item2.setViewOpreate(R.drawable.nextbg);
		item3.setIconRes(BitmapFactory.decodeResource(this.getResources(), R.drawable.routerseticon));
		item3.setMsg("路由器设置");
//		item3.setViewOpreate(R.drawable.nextbg);
		item4.setIconRes(BitmapFactory.decodeResource(this.getResources(), R.drawable.feedbackicon));
		item4.setMsg("用户反馈");
//		item4.setViewOpreate(R.drawable.nextbg);
		item5.setIconRes(BitmapFactory.decodeResource(this.getResources(), R.drawable.updateiconn));
		item5.setMsg("版本更新");
//		item5.setViewOpreate(R.drawable.nextbg);
		
		data.add(item1);
		data.add(item2);
		data.add(item3);
		data.add(item4);
		data.add(item5);
		
		adapter = new ListViewAdapter(getApplicationContext(), data,7,handler);
		lvSet.setAdapter(adapter);
		lvSet.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				switch (position) {
				case 0:
					Intent t = new Intent(SettingActivity.this,
							AccountManageActivity.class);
					Bundle b = new Bundle();
					b.putSerializable("name", name != null ? name : "");
					t.putExtras(b);
					startActivity(t);
					break;
				case 1:
					startActivity(new Intent(SettingActivity.this,
							RouterBindListActivity.class));
					break;
				case 2:
					startActivity(new Intent(SettingActivity.this,
							RouterSetActivity.class));
					break;
				case 3:
//					agent.startFeedbackActivity();
					startActivity(new Intent(SettingActivity.this,FeedBackActivity.class));
					break;
				case 4:
					checkUpdate();
					break;
				default:
					break;
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == ACTIVITY_RESULT_SUCCESS) {
			showToast("登录成功", Toast.LENGTH_SHORT);
			// TODO:在账号管理的右边显示出用户名
		}
	}

	@Override
	public void onClick(View v) {

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
}
