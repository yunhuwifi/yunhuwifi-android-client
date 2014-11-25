package com.foxrouter.api.model;

import java.util.ArrayList;
import java.util.List;

public class RouterNetworkSettingStatic extends RouterNetworkSetting {

	private String ipaddr;
	private String netmask;
	private String gwaddr;
	private String[] dns;

	public RouterNetworkSettingStatic(String ipaddr, String netmask, String gwaddr,
			String[] dns) {
		super.setProto("static");
		this.setIpaddr(ipaddr);
		this.setNetmask(netmask);
		this.setGwaddr(gwaddr);
		this.setDns(dns);
	}

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	public String getGwaddr() {
		return gwaddr;
	}

	public void setGwaddr(String gwaddr) {
		this.gwaddr = gwaddr;
	}

	public String[] getDns() {
		return dns;
	}

	public void setDns(String[] dns) {
		this.dns = dns;
	}

	public String getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}

	@Override
	public String[] toArray() {
		List<String> list = new ArrayList<String>();
		list.add(this.getProto());
		list.add(this.getIpaddr());
		list.add(this.getNetmask());
		list.add(this.getGwaddr());
		String dnsList = null;
		for (String item : this.getDns()) {
			if (dnsList.length() > 0) {
				dnsList += ",";
			}
			dnsList += item;
		}
		list.add(dnsList);

		return (String[]) list.toArray();
	}
}
