package com.yunhuwifi.activity;

import com.yunhuwifi.view.ClearEditText;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FeedBackActivity extends HeaderActivity implements OnClickListener{

	private Button btnfeedback;
	private ClearEditText etContent,etContact;
	
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_feedback);
		
		etContent=(ClearEditText) findViewById(R.id.etContact);
		etContact=(ClearEditText) findViewById(R.id.etContext);
		btnfeedback=(Button) findViewById(R.id.btnfeedback);
		btnfeedback.setOnClickListener(this);
		this.setHeaderText("用户反馈");
		this.setLeftImageVisible(true);
		this.setRightImageVisible(false);
		this.ivLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
	}

	@Override
	public void onClick(View v) {
		
	}
	

	
}
