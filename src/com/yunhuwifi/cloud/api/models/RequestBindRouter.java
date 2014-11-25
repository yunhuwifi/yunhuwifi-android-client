package com.yunhuwifi.cloud.api.models;

import java.util.ArrayList;
import java.util.List;

public class RequestBindRouter extends CloudModelBase {

	private String name;
	private String remark;
	private String firmwareVersion;
	private String hardwareVersion;
	private String username;
	private String password;
	private List<RequestBindRouterInterface> interfaces;

	public RequestBindRouter() {
		this.interfaces = new ArrayList<RequestBindRouterInterface>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public List<RequestBindRouterInterface> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<RequestBindRouterInterface> interfaces) {
		this.interfaces = interfaces;
	}
}
