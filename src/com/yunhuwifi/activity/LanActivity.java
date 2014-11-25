package com.yunhuwifi.activity;

import com.foxrouter.api.RouterModuleNetwork;
import com.foxrouter.api.RouterResponseLanDetails;
import com.foxrouter.api.RouterResponseResult;
import com.foxrouter.api.model.RouterLanDetails;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.handlers.JsonCallBack;
import com.yunhuwifi.util.CustomDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LanActivity extends BaseActivity implements OnClickListener {

	private ToggleButton tbDHCP;
	private EditText txtIPAddress;
	private TextView header_txtView;
	private Button header_btnLeft, header_btnRight;
	private RouterModuleNetwork network;
	private Dialog loading;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lan_setting);
		header_txtView = (TextView) findViewById(R.id.header_txtView);
		header_txtView.setText("内网设置");
		header_btnLeft = (Button) findViewById(R.id.header_btnLeft);
		header_btnRight = (Button) findViewById(R.id.header_btnRight);
		header_btnLeft.setOnClickListener(this);
		header_btnRight.setVisibility(View.INVISIBLE);
		findViewById(R.id.btnLan).setOnClickListener(this);
		tbDHCP = (ToggleButton) findViewById(R.id.dhpnSwitch);
		txtIPAddress = (EditText) findViewById(R.id.txtIPAddress);

		this.network = new RouterModuleNetwork(getApplicationContext()
				.getRouterContext());
		initLan();
	}

	private void initLan() {
		loading=CustomDialog.createLoadingDialog(LanActivity.this, "");
		loading.show();
		network.lanDetails(new JsonCallBack<RouterResponseLanDetails>(
				RouterResponseLanDetails.class) {

			@Override
			public void onJsonSuccess(RouterResponseLanDetails t) {
				loading.dismiss();
				if (t.getResult() != null) {
					tbDHCP.setChecked(t.getResult().isEnable());
					txtIPAddress.setText(t.getResult().getIpaddr());
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				loading.dismiss();
				showToast(strMsg, Toast.LENGTH_SHORT);
			}

		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLan:
			loading=CustomDialog.createLoadingDialog(LanActivity.this, "");
			loading.show();
			RouterLanDetails details = new RouterLanDetails();
			network.editLan(details, new JsonCallBack<RouterResponseResult>(
					RouterResponseResult.class) {

				@Override
				public void onJsonSuccess(RouterResponseResult t) {
					if (t.isResult()) {
						loading.dismiss();
						showToast("保存成功", Toast.LENGTH_SHORT);
						finish();
					} else {
						loading.dismiss();
						showToast("保存失败", Toast.LENGTH_SHORT);
					}
				}

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					loading.dismiss();
					showToast(strMsg, Toast.LENGTH_SHORT);
				}

			});
			break;
		case R.id.header_btnLeft:
			finish();
			break;
		}
	}

}
