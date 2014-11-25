package com.foxrouter.api.model;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ProgressBar;

import com.yunhuwifi.view.ListViewItem;

public class RouterWifiViewModel implements Serializable ,ListViewItem{

	private static final long serialVersionUID = 5656947868660127516L;
	private String netid;
	private String ssid;
	private boolean hidden;
	private boolean enable;
	private String encryption;
	private String key;

	public String getNetid() {
		return netid;
	}

	public void setNetid(String netid) {
		this.netid = netid;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getEncryption() {
		return encryption;
	}

	public void setEncryption(String encryption) {
		this.encryption = encryption;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String getTitle() {
		return getSsid();
	}

	@Override
	public Bitmap getBitmap() {
		return null;
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



}
