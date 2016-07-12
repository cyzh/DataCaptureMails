package com.datacap.mail.task;

import java.util.TimerTask;

import com.datacap.mail.data.NMGP;

public class NMGPTask extends TimerTask {

	private static NMGPTask nmgptask = null;

	private NMGPTask() {
	}

	public static NMGPTask getNMGPTask() {
		if (nmgptask == null) {
			nmgptask = new NMGPTask();
		}
		return nmgptask;
	}

	@Override
	public void run() {
		NMGP nmgp = NMGP.getNMGP();
		if (nmgp == null) {
			nmgp = NMGP.getInstance();
			nmgp.Start();
		}
	}

}
