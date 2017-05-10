package com.baloise.geo.http;

import static java.lang.System.currentTimeMillis;

public class Throttle {
	private long delayMilliseconds;
	private long lastCall;

	public Throttle(long delayMilliseconds) {
		this.delayMilliseconds = delayMilliseconds;
	}
	
	public void throttle() {
		long elapsedSinceLastCall = currentTimeMillis()-lastCall;
		if(elapsedSinceLastCall < delayMilliseconds) {
			long sleepMillies = delayMilliseconds - elapsedSinceLastCall;
			try {
				Thread.sleep(sleepMillies);
			} catch (InterruptedException wakeUp) {
			}
		}
		lastCall = currentTimeMillis();
	}

}
