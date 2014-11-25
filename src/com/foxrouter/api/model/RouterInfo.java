package com.foxrouter.api.model;

import java.io.Serializable;
import java.util.List;

import net.tsz.afinal.annotation.sqlite.Id;

public class RouterInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8683244460115335082L;
	@Id(column = "macAddress")
	private String macAddress;
	private String hostname;
	private String firmwareVersion;
	private String hardwareVersion;
	private List<RouterInterface> interfaces;

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	public String getHardwareVersion() {
		return hardwareVersion;
	}

	public void setHardwareVersion(String hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public List<RouterInterface> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<RouterInterface> interfaces) {
		this.interfaces = interfaces;
	}
}
