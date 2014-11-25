package com.yunhuwifi.models;

import java.util.ArrayList;

import android.provider.CallLog;

public class CallLogs {
	ArrayList<CallLog> mCallLogs= new ArrayList<CallLog>(); 
	private int id;
	private long duration;
	private long date;
	private int type;
	private String number;
	private String name;
	private String dateFormat;
	private int count;

	public int getCount() {
	return count;
	}

	public void setCount(int count) {
	this.count = count;
	}

	public String getDateFormat() {
	return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
	this.dateFormat = dateFormat;
	}

	public int getId() {
	return id;
	}

	public void setId(int id) {
	this.id = id;
	}

	public long getDuration() {
	return duration;
	}

	public void setDuration(long duration) {
	this.duration = duration;
	}

	public long getDate() {
	return date;
	}

	public void setDate(long date) {
	this.date = date;
	}

	public int getType() {
	return type;
	}

	public void setType(int type) {
	this.type = type;
	}

	public String getTelNumber() {
	return number;
	}

	public void setTelNumber(String telNumber) {
	this.number = telNumber;
	}

	public String getName() {
	return name;
	}

	public void setName(String name) {
	this.name = name;
	}
}
