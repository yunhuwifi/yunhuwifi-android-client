package com.foxrouter.api.model;

public class RouterWifiDetails {

	private String wdev;
	private String netid;
	private String sid;
	private RouterWifiIWData iwdata;

	public String getWdev() {
		return wdev;
	}

	public void setWdev(String wdev) {
		this.wdev = wdev;
	}

	public String getNetid() {
		return netid;
	}

	public void setNetid(String netid) {
		this.netid = netid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public RouterWifiIWData getIwdata() {
		return iwdata;
	}

	public void setIwdata(RouterWifiIWData iwdata) {
		this.iwdata = iwdata;
	}

	public class RouterWifiIWData {
		private String ssid;
		private String encryption;
		private String device;
		private String key;
		private String mode;
		private String network;

		public String getSsid() {
			return ssid;
		}

		public void setSsid(String ssid) {
			this.ssid = ssid;
		}

		public String getEncryption() {
			return encryption;
		}

		public void setEncryption(String encryption) {
			this.encryption = encryption;
		}

		public String getDevice() {
			return device;
		}

		public void setDevice(String device) {
			this.device = device;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getMode() {
			return mode;
		}

		public void setMode(String mode) {
			this.mode = mode;
		}

		public String getNetwork() {
			return network;
		}

		public void setNetwork(String network) {
			this.network = network;
		}
	}
}
