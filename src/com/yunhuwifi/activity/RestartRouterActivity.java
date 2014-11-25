package com.yunhuwifi.activity;

import com.yunhuwifi.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class RestartRouterActivity extends Activity {
	private TextView dialogtitle,dialogmessage;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_box);
		 dialogtitle=(TextView) findViewById(R.id.dialogtitle);
		 dialogmessage=(TextView) findViewById(R.id.dialogmessage);
		dialogtitle.setText("重启路由器");
		dialogmessage.setText("在重启的过程中你将不能上网！");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
	
	public void ok(View v) {  
    	this.finish();    	
      }  
	public void cancel(View v) {  
    	this.finish();
      }  
	

}
