package com.yunhuwifi.speedtester;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.Handler;
import android.os.Message;

public class SingleSpeedTester {
	private final static int MESSAGE_START = 0;
	private final static int MESSAGE_SPEED = 1;
	private final static int MESSAGE_COMPLETE = 2;

	private boolean completed;
	private String url;
	private Handler handler;
	private Runnable runnable;
	private long transferedBytes;

	public SingleSpeedTester(String url) {
		this.url = url;
		this.handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == MESSAGE_START) {
					onStart();
				} else if (msg.what == MESSAGE_SPEED) {
					onSpeed((Double) msg.obj);
				} else if (msg.what == MESSAGE_COMPLETE) {
					completed = true;
					onComplete();
				}
			}
		};
	}

	public long getTransferedBytes() {
		return this.transferedBytes;
	}

	public void run() {
		this.completed = false;
		this.runnable = new Runnable() {

			@Override
			public void run() {
				URLConnection conn;
				InputStream stream = null;
				try {
					conn = getConnection(url);
					conn.connect();

					stream = conn.getInputStream();
					while (transferedBytes < conn.getContentLength()) {
						byte[] buffer = new byte[1024 * 4];
						long startTicks = System.currentTimeMillis();
						int rec = stream.read(buffer, 0, buffer.length);
						long endTicks = System.currentTimeMillis();
						double speed = 0;
						if ((endTicks - startTicks) > 0) {
							speed = (double) rec / (endTicks - startTicks);
						}

						transferedBytes += rec;
						Message msg = handler.obtainMessage();
						msg.what = MESSAGE_SPEED;
						msg.obj = speed;
						msg.sendToTarget();
					}

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					completed = true;
					try {
						if (stream != null) {
							stream.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		};

		Thread thread = new Thread(this.runnable);
		thread.start();
	}

	private URLConnection getConnection(String u) throws IOException {
		URL url = new URL(u);
		return url.openConnection();
	}

	public void onStart() {
	}

	public void onSpeed(double speed) {
	}

	public void onComplete() {
	}

	public boolean isCompleted() {
		return completed;
	}
}
