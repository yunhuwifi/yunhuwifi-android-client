package com.yunhuwifi.activity;

import java.util.ArrayList;
import java.util.List;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.models.RouterSet;
import com.yunhuwifi.view.ListViewAdapter;
import com.yunhuwifi.view.ListViewItem;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RouterSetActivity extends BaseActivity implements OnClickListener {
	private TextView header_txtView;
	private ImageView header_ivLeft,header_ivRight;
	private ListView lvRouterSet;
	private List<ListViewItem> data=new ArrayList<ListViewItem>();
	private ListViewAdapter adapter;
	Handler handler = new Handler() ;
	public void handleMessage(android.os.Message msg) {
		if (msg.what == 1) {
			Log.d("tag","共" + msg.arg1 + "条，已选" + msg.arg2 + "条");
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_router_setting);
		header_txtView=(TextView) findViewById(R.id.header_txtView);
		header_txtView.setText("路由器设置");
		header_ivLeft=(ImageView) findViewById(R.id.header_ivLeft);
		header_ivRight=(ImageView) findViewById(R.id.header_ivRight);
		header_ivLeft.setOnClickListener(this);
		header_ivRight.setVisibility(View.INVISIBLE);
		lvRouterSet=(ListView) findViewById(R.id.lvRouterSet);
		initdata();
	}


	private void initdata() {
		RouterSet item1=new RouterSet();
		RouterSet item2=new RouterSet();
		RouterSet item3=new RouterSet();
		RouterSet item4=new RouterSet();
		RouterSet item5=new RouterSet();
		RouterSet item6=new RouterSet();
		item1.setMsg("管理密码设置");
		item1.setViewOpreate(R.drawable.nextbg);
		item1.setIconRes(BitmapFactory.decodeResource(this.getResources(),R.drawable.adminpwdicon));
		item2.setMsg("无线设置");
		item2.setViewOpreate(R.drawable.nextbg);
		item2.setIconRes(BitmapFactory.decodeResource(this.getResources(),R.drawable.wifiicon));
		item3.setMsg("内网设置");
		item3.setViewOpreate(R.drawable.nextbg);
		item3.setIconRes(BitmapFactory.decodeResource(this.getResources(),R.drawable.lanicon));
		item4.setMsg("外网设置");
		item4.setViewOpreate(R.drawable.nextbg);
		item4.setIconRes(BitmapFactory.decodeResource(this.getResources(),R.drawable.wanicon));
		item5.setMsg("重启路由器");
		item5.setViewOpreate(R.drawable.nextbg);
		item5.setIconRes(BitmapFactory.decodeResource(this.getResources(),R.drawable.restarticon));
		item6.setMsg("恢复出厂设 置");
		item6.setViewOpreate(R.drawable.nextbg);
		item6.setIconRes(BitmapFactory.decodeResource(this.getResources(),R.drawable.renewicon));
		
		data.add(item1);
		data.add(item2);
		data.add(item3);
		data.add(item4);
		data.add(item5);
		data.add(item6);
		
		
		 adapter=new ListViewAdapter(getApplicationContext(), data,7,handler);
			lvRouterSet.setAdapter(adapter);
			lvRouterSet.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					 switch (position) {
			            case 0:
			            	startActivity(new Intent(RouterSetActivity.this,
			    					ChangeRouterPwdActivty.class));
			            	break;
			            case 1:
			            	startActivity(new Intent(RouterSetActivity.this,
			    					WifiListActivity.class));
			            	break;
			            case 2:
			            	startActivity(new Intent(RouterSetActivity.this,
			            			LanActivity.class));
			            	break;
			            case 3:
			            	startActivity(new Intent(RouterSetActivity.this,
			            			WanActivity.class));
			            	break;
			            case 4:
			            	startActivity(new Intent(RouterSetActivity.this,
			            			RestartRouterActivity.class));
			            	break;
			            case 5:
			            	startActivity(new Intent(RouterSetActivity.this,
			            			RenewRouterActivity.class));
			            	break;
			            default:
			                break;
					 }
				}
			});
		
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_ivLeft:
			finish();
			break;
		}

	}
	


	
	
	
}
