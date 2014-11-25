package com.yunhuwifi.fragment;

import com.foxrouter.api.RouterModuleNetwork;
import com.foxrouter.api.RouterResponseResult;
import com.foxrouter.api.model.RouterNetworkSettingPPPoE;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.YunhuApplication;
import com.yunhuwifi.handlers.JsonCallBack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PPPoEFrament extends Fragment {

	private Button btnpppoe;
	private EditText wanUser, wanPwd;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_pppoe_setting, container,
				false);
		btnpppoe = (Button) v.findViewById(R.id.btnpppoe);
		wanUser = (EditText) v.findViewById(R.id.wanUser);
		wanPwd = (EditText) v.findViewById(R.id.wanPwd);
		btnpppoe.setOnClickListener(listener);
		return v;
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnpppoe:
				editPPPo();
				break;
			}
		}
	};

	private void editPPPo() {
		YunhuApplication context = (YunhuApplication) getActivity()
				.getApplicationContext();
		String user = wanUser.getText().toString();
		String pwd = wanPwd.getText().toString();
		RouterNetworkSettingPPPoE ppoe = new RouterNetworkSettingPPPoE(user,
				pwd);
		RouterModuleNetwork wanppoe = new RouterModuleNetwork(
				context.getRouterContext());
		wanppoe.editWan(ppoe, new JsonCallBack<RouterResponseResult>(
				RouterResponseResult.class) {

			@Override
			public void onJsonSuccess(RouterResponseResult t) {
				if (t.isResult()) {
					Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT)
							.show();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
}
