package com.yunhuwifi.fragment;

import com.foxrouter.api.RouterModuleNetwork;
import com.foxrouter.api.RouterResponseResult;
import com.foxrouter.api.model.RouterNetworkSettingStatic;
import com.yunhuwifi.activity.R;
import com.yunhuwifi.YunhuApplication;
import com.yunhuwifi.handlers.JsonCallBack;
import com.yunhuwifi.view.ClearEditText;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class StaticIPFrament extends Fragment {
		private Button btnstaticip,btnDNS;
		private ClearEditText netmask, ipaddr,gwaddr; 
		private String[] dns=null;
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_staticip_setting, container,
					false);
			btnstaticip = (Button) v.findViewById(R.id.btnstaticip);
			btnDNS = (Button) v.findViewById(R.id.btnDNS);
			netmask = (ClearEditText) v.findViewById(R.id.netmask);
			ipaddr = (ClearEditText) v.findViewById(R.id.ipaddr);
			gwaddr = (ClearEditText) v.findViewById(R.id.gwaddr);
			btnDNS.setOnClickListener(listener);
			btnstaticip.setOnClickListener(listener);
			return v;
		}

		private OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btnDNS:
					popupDNS();
					break;
				case R.id.btnstaticip:
					editStaticIP();
					break;
				}
			}
		};
 
		private void popupDNS() {
			final Dialog dlg = new Dialog(getActivity());
			View dlgView = View.inflate(getActivity(),  R.layout.dns_box,null);
			View ok = dlgView.findViewById(R.id.ok); 
			ok.setTag(dlg); 
			final ClearEditText first=(ClearEditText)dlgView.findViewById(R.id.first);  
			final ClearEditText backup = (ClearEditText)dlgView.findViewById(R.id.backup); 
			ok.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String ftxt=first.getText().toString();
					String btxt=backup.getText().toString();
					dns=new String[]{ftxt,btxt};
					System.out.println(dns);
					dlg.dismiss();
				} 
			});
			View cancel = dlgView.findViewById(R.id.cancel);
			cancel.setTag(dlg);
			cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dlg.dismiss();
				}
			});
			
			dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dlg.setCanceledOnTouchOutside(true);
			dlg.setContentView(dlgView);
			dlg.show();
		
		}
		
		private void editStaticIP() {
			YunhuApplication context = (YunhuApplication) getActivity()
					.getApplicationContext();
			String net = netmask.getText().toString();
			String ip = ipaddr.getText().toString();
			String gw = gwaddr.getText().toString();
			RouterNetworkSettingStatic wanip= new RouterNetworkSettingStatic(net,
					ip,gw,dns);
			RouterModuleNetwork wanstatic = new RouterModuleNetwork(
					context.getRouterContext());
			wanstatic.editWan(wanip, new JsonCallBack<RouterResponseResult>(
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
