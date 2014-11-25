package com.foxrouter.api.model;

import java.io.Serializable;

import com.yunhuwifi.view.ListViewItem;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ProgressBar;

public class RouterDevice implements Serializable, ListViewItem {

	private static final long serialVersionUID = -8569802417162137570L;
	private String brand;
	private String remark;
	private String ipaddr;
	private String macaddr;
	private String hostname;

	public String getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}

	public String getMacaddr() {
		return macaddr;
	}

	public void setMacaddr(String macaddr) {
		this.macaddr = macaddr;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	@Override
	public Bitmap getBitmap() {
		return null;
	}

	@Override
	public String getTitle() {
		if (getRemark() != null && getRemark().length() > 0) {
			return getRemark();
		} else if (getHostname() != null && getHostname().length() > 0) {
			return getHostname();
		} else {
			return getMacaddr();
		}
	}

	@Override
	public int getOperationView() {
		return 0;
	}

	@Override
	public String getSpeed() {
		return getIpaddr();
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
