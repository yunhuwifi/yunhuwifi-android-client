package com.yunhuwifi.models;

import net.tsz.afinal.annotation.sqlite.Id;

public class RouterLocalAccount {

	@Id(column = "macAddress")
	private String macAddress;
	private String username;
	private String password;

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
}
