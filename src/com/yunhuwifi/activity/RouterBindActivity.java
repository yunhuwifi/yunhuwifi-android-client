package com.yunhuwifi.activity;

import java.util.ArrayList;

import com.foxrouter.api.RouterModuleSystem;
import com.foxrouter.api.RouterResponseRouterInfo;
import com.foxrouter.api.model.RouterInterface;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.RouterContext;
import com.yunhuwifi.cloud.api.RouterService;
import com.yunhuwifi.cloud.api.models.RequestBindRouterInterface;
import com.yunhuwifi.cloud.api.models.RequestBindRouter;
import com.yunhuwifi.cloud.api.models.ResponseRouterBind;
import com.yunhuwifi.handlers.JsonCallBack;
import com.yunhuwifi.util.CustomDialog;
import com.yunhuwifi.view.ClearEditText;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class RouterBindActivity extends HeaderActivity implements
		OnClickListener {

	private final static int ACTIVITY_REQUEST_ROUTER_LOGIN = 0;
	private final static int ACTIVITY_REQUEST_USER_LOGIN = 1;

	private ClearEditText txtRouterName;
	private Button btnBind;
	private Dialog  loading;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_router_bind);

		setHeaderText("绑定路由器");
		this.setLeftButtonVisible(true);
		this.txtRouterName = (ClearEditText) findViewById(R.id.txtRouterName);
		this.btnBind = (Button) findViewById(R.id.btnBind);
		this.btnBind.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (getApplicationContext().getRouterContext() == null) {
			// 路由器未连接
			Intent intent = new Intent(this, RouterLoginActivity.class);
			startActivityForResult(intent, ACTIVITY_REQUEST_ROUTER_LOGIN);
		} else if (getApplicationContext().getUserContext() == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent, ACTIVITY_REQUEST_USER_LOGIN);
		} else {
			// this.initView();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == ACTIVITY_RESULT_SUCCESS) {
			this.initView();
		} else {
			showToast("未连接路由器", Toast.LENGTH_SHORT);
		}
	}

	private void initView() {
		 loading =CustomDialog.createLoadingDialog(RouterBindActivity.this, "正在读取路由器设置，请稍后...");
		loading.show();
		RouterModuleSystem system = new RouterModuleSystem(
				getApplicationContext().getRouterContext());
		system.info(new JsonCallBack<RouterResponseRouterInfo>(
				RouterResponseRouterInfo.class) {

			@Override
			public void onJsonSuccess(RouterResponseRouterInfo t) {
				loading.dismiss();
				if (t.getError() == null && t.getResult() != null) {
					txtRouterName.setText(t.getResult().getHostname());
				} else {
					showToast(t.getError().getMessage(), Toast.LENGTH_SHORT);
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				loading.dismiss();
				showToast(strMsg, Toast.LENGTH_SHORT);
			}
		});
	}

	private void bind() {
		RouterContext routerContext = getApplicationContext()
				.getRouterContext();
		final RequestBindRouter model = new RequestBindRouter();
		model.setName(routerContext.getRouter().getHostname());
		model.setUsername(routerContext.getUsername());
		model.setPassword(routerContext.getPassword());
		model.setRemark(this.txtRouterName.getText().toString());
		model.setFirmwareVersion(routerContext.getRouter().getFirmwareVersion());
		model.setHardwareVersion(routerContext.getRouter().getHardwareVersion());

		model.setInterfaces(new ArrayList<RequestBindRouterInterface>());
		for (RouterInterface item : routerContext.getRouter().getInterfaces()) {
			RequestBindRouterInterface bif = new RequestBindRouterInterface();
			bif.setName(item.getName());
			bif.setMacAddress(item.getMacaddr());
			model.getInterfaces().add(bif);
		}
		loading =CustomDialog.createLoadingDialog(RouterBindActivity.this, "正在绑定，请稍后...");
		loading.show();
		RouterService service = new RouterService(getApplicationContext()
				.getUserContext());
		service.bindRouter(model, new JsonCallBack<ResponseRouterBind>(
				ResponseRouterBind.class) {

			@Override
			public void onJsonSuccess(ResponseRouterBind t) {
				loading.dismiss();
				if (t.isSuccess()) {
					setResult(ACTIVITY_RESULT_SUCCESS);
					finish();
				} else {
					showToast("绑定失败", Toast.LENGTH_SHORT);
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				loading.dismiss();
				showToast("绑定失败:" + t.getMessage(), Toast.LENGTH_SHORT);
			};
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnBind) {
			bind();
		}
	}
}
