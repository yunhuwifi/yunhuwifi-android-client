package com.yunhuwifi.speedtester;

public class NetSpeedTesterResult {
	private double maxSpeed;
	private double avgSpeed;

	public double getMaxSpeed() {
		return this.maxSpeed;
	}

	public double getAvgSpeed() {
		return this.avgSpeed;
	}

	public NetSpeedTesterResult(double maxSpeed, double avgSpeed) {
		this.maxSpeed = maxSpeed;
		this.avgSpeed = avgSpeed;
	}
}
