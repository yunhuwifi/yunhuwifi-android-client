package com.yunhuwifi.models;

import android.graphics.Bitmap;

import com.yunhuwifi.view.ListViewItem;



public class RouterSet implements ListViewItem{
	private String msg;
	private Bitmap iconRes;
	private int viewOpreate;
	
	@Override
	public Bitmap getBitmap() {
		return getIconRes();
	}
	@Override
	public String getTitle() {
		return getMsg();
	}
	@Override
	public String getSpeed() {
		return null;
	}
	@Override
	public String getDate() {
		return null;
	}
	@Override
	public String getState() {
		return null;
	}
	@Override
	public String getSize() {
		return null;
	}
	@Override
	public int getOperationView() {
		return 0;
	}
	@Override
	public String getPrecent() {
		return null;
	}
	@Override
	public int getProgress() {
		return 0;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getViewOpreate() {
		return viewOpreate;
	}
	public void setViewOpreate(int viewOpreate) {
		this.viewOpreate = viewOpreate;
	}
	public Bitmap getIconRes() {
		return iconRes;
	}
	public void setIconRes(Bitmap iconRes) {
		this.iconRes = iconRes;
	}
	

	
	


	
}