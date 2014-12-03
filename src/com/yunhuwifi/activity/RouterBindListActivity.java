package com.yunhuwifi.activity;

import java.util.ArrayList;
import java.util.List;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.RouterContext;
import com.yunhuwifi.UserContext;
import com.yunhuwifi.cloud.api.RouterService;
import com.yunhuwifi.cloud.api.models.RouterModel;
import com.yunhuwifi.cloud.api.models.RouterResult;
import com.yunhuwifi.handlers.JsonCallBack;
import com.yunhuwifi.util.CustomDialog;
import com.yunhuwifi.view.ListViewAdapter;
import com.yunhuwifi.view.ListViewItem;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RouterBindListActivity extends HeaderActivity {

	private ListView lvBindRouter;
	private ListViewAdapter adapter;
	private List<ListViewItem> dataList = new ArrayList<ListViewItem>();
	private UserContext userContext;

	private final static int REQUEST_CODE_LOGIN = 0;
	private final static int REQUEST_CODE_BIND = 1;
	private Dialog loading;

	Handler handler = new Handler() ;
	public void handleMessage(android.os.Message msg) {
		if (msg.what == 1) {
			Log.d("tag","共" + msg.arg1 + "条，已选" + msg.arg2 + "条");
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_router_bind_list);

		// 设置顶部按钮
		this.setHeaderText("我绑定的路由器");
		this.setLeftImageVisible(true);
		this.ivLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		this.ivRight.setBackgroundResource(R.drawable.addicon);
		this.setRightImageVisible(true);
		this.ivRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RouterBindListActivity.this,
						RouterBindActivity.class);
				startActivityForResult(intent, REQUEST_CODE_BIND);
			}
		});

		this.lvBindRouter = (ListView) findViewById(R.id.lvBindRouter);
		this.adapter = new ListViewAdapter(this.getApplicationContext(),
				dataList,8,handler);

		this.lvBindRouter.setAdapter(this.adapter);

		this.lvBindRouter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				RouterModel item = (RouterModel) dataList.get((int) id);

				loading = CustomDialog.createLoadingDialog(
						RouterBindListActivity.this, "");
				loading.show();
				RouterContext.login(item.getUsername(), item.getPassword(),
						item.getIpAddress(), item.getPort(),
						new JsonCallBack<RouterContext>(RouterContext.class) {

							@Override
							public void onJsonSuccess(RouterContext t) {
								loading.dismiss();
								getApplicationContext().setRouterContext(t);
								setResult(ACTIVITY_RESULT_SUCCESS);
								finish();
							}

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								loading.dismiss();
								showToast(strMsg + "(" + errorNo + ")",
										Toast.LENGTH_SHORT);
							}
						});
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (getApplicationContext().getUserContext() == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent, REQUEST_CODE_LOGIN);
		} else {
			this.userContext = getApplicationContext().getUserContext();
			loadRouter();
		}
	}

	private void loadRouter() {
		RouterService service = new RouterService(getApplicationContext()
				.getUserContext());
		service.getRouters(new JsonCallBack<RouterResult>(RouterResult.class) {
			@Override
			public void onStart() {
				loading = CustomDialog.createLoadingDialog(
						RouterBindListActivity.this, "");
				loading.show();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (loading != null && loading.isShowing()) {
					loading.dismiss();
					showToast("无法获取已绑定的路由器", Toast.LENGTH_SHORT);
				}
			}

			@Override
			public void onJsonSuccess(RouterResult t) {
				loading.dismiss();
				if (t.isSuccess()) {
					dataList.clear();
					for (RouterModel model : t.getResult()) {
						dataList.add(model);
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK) {
			this.loadRouter();
		}
	}
}
