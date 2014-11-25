package com.yunhuwifi.fragment;


import com.yunhuwifi.activity.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class DHCPFrament extends Fragment {

	private TextView txtContent;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_dhcp_setting, container, false);
		txtContent = (TextView) v.findViewById(R.id.txtContent);
		txtContent.setOnClickListener(listener);
		return v;
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.txtContent:
				break;
			}
		}
	};
}
