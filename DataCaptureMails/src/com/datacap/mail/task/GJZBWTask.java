package com.datacap.mail.task;

import java.util.TimerTask;

import com.datacap.mail.data.GJZB;

public class GJZBWTask extends TimerTask {

	private static GJZBWTask gjzbwtask = null;

	private GJZBWTask() {
	}

	public static GJZBWTask getGJZBWTask() {
		if (gjzbwtask == null) {
			gjzbwtask = new GJZBWTask();
		}
		return gjzbwtask;
	}

	@Override
	public void run() {
		GJZB gjzbw = GJZB.getGJZB();
		if (gjzbw == null) {
			gjzbw = GJZB.getInstance();
			gjzbw.start();
		}
	}

}
