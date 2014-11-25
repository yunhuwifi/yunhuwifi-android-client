package com.yunhuwifi.speedtester;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;

public class NetSpeedTester {
	private final static int MESSAGE_SPEED = 1;
	private final static int MESSAGE_COMPLETE = 2;

	private String[] urls;
	private long[] lastTransfered;
	private List<SingleSpeedTester> tests;
	private Runnable runnable;
	private Handler handler;
	private long lastCalcTimeTick;
	private double maxSpeed;
	private boolean completed;

	public NetSpeedTester(String[] urls) {
		this.urls = urls;
	}

	public void run(final SpeedTestCallBack callback) {
		this.lastCalcTimeTick = 0;
		this.maxSpeed = 0;
		this.completed = false;

		this.tests = new ArrayList<SingleSpeedTester>();

		for (String u : urls) {
			SingleSpeedTester tester = new SingleSpeedTester(u) {
				public void onComplete() {
					boolean completed = true;
					// 计算是否所有的都完成了
					for (SingleSpeedTester tester : tests) {
						if (!tester.isCompleted()) {
							completed = false;
						}
					}

					if (completed) {
						NetSpeedTester.this.completed = completed;
						Message msg = handler.obtainMessage();
						msg.what = MESSAGE_COMPLETE;
						msg.sendToTarget();
					}

				};
			};
			tests.add(tester);

			tester.run();
		}

		this.lastTransfered = new long[this.tests.size()];

		this.handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == MESSAGE_SPEED) {
					NetSpeedTesterResult result = (NetSpeedTesterResult) msg.obj;
					callback.onSpeed(result.getMaxSpeed(), result.getAvgSpeed());
				} else if (msg.what == MESSAGE_COMPLETE) {
					callback.onComplete(maxSpeed);
				}

				if (!completed) {
					handler.postDelayed(runnable, 1000);
				}
			}
		};

		this.runnable = new Runnable() {

			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				long totalTransfered = 0;
				long lastTransfered = 0;
				for (int i = 0; i < tests.size(); i++) {
					SingleSpeedTester tester = tests.get(i);
					long transfered = tester.getTransferedBytes();
					totalTransfered += transfered;
					lastTransfered += NetSpeedTester.this.lastTransfered[i];
					NetSpeedTester.this.lastTransfered[i] = transfered;
				}

				long currentTimeTick = System.currentTimeMillis();
				double speed = (double) (totalTransfered - lastTransfered)
						/ (currentTimeTick - lastCalcTimeTick);
				if (speed > maxSpeed) {
					maxSpeed = speed;
				}
				lastCalcTimeTick = currentTimeTick;
				msg.what = MESSAGE_SPEED;
				msg.obj = new NetSpeedTesterResult(maxSpeed, speed);
				msg.sendToTarget();
			}

		};
		this.handler.postDelayed(this.runnable, 1000);
	}
}
