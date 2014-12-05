package com.yunhuwifi.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FeedBackActivity extends HeaderActivity implements OnClickListener{

	private Button btnfeedback;
	private EditText etContent,etContact;
	
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_feedback);
		
		etContent=(EditText) findViewById(R.id.etContact);
		etContact=(EditText) findViewById(R.id.etContext);
		btnfeedback=(Button) findViewById(R.id.btnfeedback);
		btnfeedback.setOnClickListener(this);
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
