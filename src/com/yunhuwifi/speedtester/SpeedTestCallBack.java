package com.yunhuwifi.speedtester;

public abstract class SpeedTestCallBack {
	public abstract void onSpeed(double maxSpeed, double speed);

	public abstract void onComplete(double maxSpeed);
}
