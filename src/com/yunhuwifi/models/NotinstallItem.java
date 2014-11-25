package com.yunhuwifi.models;

import com.yunhuwifi.view.ListViewItem;

import android.graphics.Bitmap;

public class NotinstallItem implements ListViewItem{
	private Bitmap iconRes;
	private String name;
	private String msg;



	public Bitmap getIconRes() {
		return iconRes;
	}

	public void setIconRes(Bitmap iconRes) {
		this.iconRes = iconRes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public Bitmap getBitmap() {
		// TODO Auto-generated method stub
		return getIconRes();
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return getName();
	}

	@Override
	public String getSpeed() {
		// TODO Auto-generated method stub
		return getMsg();
	}

	@Override
	public String getDate() {
		// TODO Auto-generated method stub
		return null;
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
	public String getPrecent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOperationView() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getProgress() {
		// TODO Auto-generated method stub
		return 0;
	}
}