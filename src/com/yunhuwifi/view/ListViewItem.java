package com.yunhuwifi.view;

import android.graphics.Bitmap;

public interface ListViewItem {

	Bitmap getBitmap();

	String getTitle();

	String getSpeed();
	
	String getDate();
	
	String getState();

	String getSize();

	String getPrecent();
	
	int getOperationView();
	
	int getProgress();
}
