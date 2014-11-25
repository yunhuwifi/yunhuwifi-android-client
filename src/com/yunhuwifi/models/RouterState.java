package com.yunhuwifi.models;

import com.yunhuwifi.view.ListViewItem;

import android.graphics.Bitmap;


public class RouterState implements ListViewItem{

	private String msg;
	private String time;
	private int viewoperate;
	@Override
	public String getTitle() {
		return getMsg();
	}

	@Override
	public Bitmap getBitmap() {
		return null;
	}


	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String getSpeed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDate() {
		// TODO Auto-generated method stub
		return getTime();
	}

	@Override
	public String getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOperationView() {
		return getViewoperate();
	}

	@Override
	public String getPrecent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getProgress() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getViewoperate() {
		return viewoperate;
	}

	public void setViewoperate(int viewoperate) {
		this.viewoperate = viewoperate;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	


	
}