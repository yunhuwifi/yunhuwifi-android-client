package com.yunhuwifi.util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyToast {
	
	public static void myToastShow(Context context,String content,int duration) {
		Toast toast = new Toast(context);
		toast.setDuration(duration);
		toast.setGravity(Gravity.BOTTOM, 0, 25);
		
		LinearLayout toastLayout = new LinearLayout(context);
		toastLayout.setOrientation(LinearLayout.HORIZONTAL);
		toastLayout.setGravity(Gravity.CENTER_VERTICAL);
		
	/*	ImageView imageView = new ImageView(context);
		imageView.setImageResource(imageResId);
		toastLayout.addView(imageView);*/
		
		TextView tv_content = new TextView(context);
		tv_content.setText(content);
		tv_content.setBackgroundColor(Color.BLACK);
		
		toastLayout.addView(tv_content);
		
		toast.setView(toastLayout);
		
		toast.show();
		
		
	}

}
