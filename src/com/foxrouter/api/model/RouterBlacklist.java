package com.foxrouter.api.model;

import android.graphics.Bitmap;

import com.yunhuwifi.view.ListViewItem;

public class RouterBlacklist implements ListViewItem{
	private String msg;
	private int viewoperate;
	private Bitmap imgIcon;
	public int getViewoperate() {
		return viewoperate;
	}

	public void setViewoperate(int viewoperate) {
		this.viewoperate = viewoperate;
	}

	public Bitmap getImgIcon() {
		return imgIcon;
	}

	public void setImgIcon(Bitmap imgIcon) {
		this.imgIcon = imgIcon;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String getTitle() {
		return getMsg();
	}

	@Override
	public Bitmap getBitmap() {
		return getImgIcon();
	}


	@Override
	public String getSpeed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDate() {
		// TODO Auto-generated method stub
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
		return getOperationView();
	}

	
}