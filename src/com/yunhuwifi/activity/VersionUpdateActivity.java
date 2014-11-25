package com.yunhuwifi.activity;

import com.yunhuwifi.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class VersionUpdateActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_version_updated);

		TextView headerText = (TextView) findViewById(R.id.tvHeaderText);
		headerText.setText("版本升级");
		findViewById(R.id.versionUpdate).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

}
