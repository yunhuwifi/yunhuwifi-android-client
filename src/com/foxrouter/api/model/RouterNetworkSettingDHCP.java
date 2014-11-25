package com.foxrouter.api.model;

import java.util.ArrayList;
import java.util.List;

public class RouterNetworkSettingDHCP extends RouterNetworkSetting {

	private String name;

	public RouterNetworkSettingDHCP(String name) {
		super.setProto("dhcp");
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String[] toArray() {
		List<String> list = new ArrayList<String>();
		list.add(this.getProto());
		list.add(this.getName());

		return (String[]) list.toArray();
	}
}
