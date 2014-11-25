package com.yunhuwifi.cloud.api.models;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.annotations.SerializedName;
import com.yunhuwifi.view.ListViewItem;

public class RouterModel extends CloudModelBase implements ListViewItem{

	@SerializedName("Id")
	private int id;
	@SerializedName("Remark")
	private String remark;
	@SerializedName("MacAddress")
	private String macAddress;
	@SerializedName("UserName")
	private String username;
	@SerializedName("Password")
	private String password;
	@SerializedName("IPAddress")
	private String ipAddress;
	@SerializedName("Port")
	private int port;
	
	private int operateView;

	private Bitmap bitmapView;
	public int getOperateView() {
		return operateView;
	}
	
	public void setOperateView(int operateView) {
		this.operateView = operateView;
	}
	
	public Bitmap getBitmapView() {
		return bitmapView;
	}
	
	public void setBitmapView(Bitmap bitmapView) {
		this.bitmapView = bitmapView;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public Bitmap getBitmap() {
		return getBitmapView();
	}

	@Override
	public String getTitle() {
		return getRemark();
	}


	@Override
	public int getOperationView() {
		return getOperateView();
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
	public String getPrecent() {
		return null;
	}

	@Override
	public int getProgress() {
		return 0;
	}
}
