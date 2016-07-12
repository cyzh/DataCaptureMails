package com.datacap.mail.task;

import java.util.TimerTask;

import com.datacap.mail.data.NMJTT;

/**
 * 内蒙古交通厅
 * 
 * @author WenC
 * 
 */
public class NMJTTTask extends TimerTask {

	private static NMJTTTask cbtask = null;

	private NMJTTTask() {
	}

	public static NMJTTTask getNMJTTTask() {
		if (cbtask == null) {
			cbtask = new NMJTTTask();
		}
		return cbtask;
	}

	@Override
	public void run() {
		// 是否可以抓取
		NMJTT nmjtt = NMJTT.getNMJTT();
		if (nmjtt == null) {
			nmjtt = NMJTT.getInstance();
			nmjtt.start();
		}
	}

}
