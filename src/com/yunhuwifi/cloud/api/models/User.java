package com.yunhuwifi.cloud.api.models;

import net.tsz.afinal.annotation.sqlite.Id;

import com.google.gson.annotations.SerializedName;

public class User {
	@Id(column = "Id")
	private int id;
	@SerializedName("Uid")
	private int uid;
	@SerializedName("UserName")
	private String username;
	@SerializedName("Phone")
	private String phone;
	@SerializedName("PassWord")
	private String password;
	@SerializedName("Mail")
	private String mail;
	@SerializedName("HasSameName")
	private boolean hasSameName;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.username = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public boolean isHasSameName() {
		return hasSameName;
	}

	public void setHasSameName(boolean hasSameName) {
		this.hasSameName = hasSameName;
	}
}
