package com.foxrouter.api.model;

public class RouterLanDetails {

	private boolean enable;
	private int start;
	private int limit;
	private String ipaddr;
	private String netmask;
	private String[] dns;

	public String getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	public String[] getDns() {
		return dns;
	}

	public void setDns(String[] dns) {
		this.dns = dns;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Object[] toArray() {
		return null;
	}
}
