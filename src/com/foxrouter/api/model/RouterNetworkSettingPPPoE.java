package com.foxrouter.api.model;

import java.util.ArrayList;
import java.util.List;

public class RouterNetworkSettingPPPoE extends RouterNetworkSetting {

	private String username;
	private String password;

	public RouterNetworkSettingPPPoE(String username, String password) {
		super.setProto("pppoe");
		this.setUsername(username);
		this.setPassword(password);
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

	@Override
	public String[] toArray() {
		List<String> list = new ArrayList<String>();
		list.add(this.getProto());
		list.add(this.getUsername());
		list.add(this.getPassword());

		return (String[]) list.toArray();
	}
}
