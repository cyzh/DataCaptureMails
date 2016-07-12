package com.datacap.mail.task;

import java.util.TimerTask;

import com.datacap.mail.data.NMGZTB;

public class NMGZTBTask extends TimerTask {

	private static NMGZTBTask nmgztbtask = null;

	private NMGZTBTask() {
	}

	public static NMGZTBTask getNMGZTBTask() {
		if (nmgztbtask == null) {
			nmgztbtask = new NMGZTBTask();
		}
		return nmgztbtask;
	}

	@Override
	public void run() {
		NMGZTB nmgztb = NMGZTB.getNMGZTB();
		if (nmgztb == null) {
			nmgztb = NMGZTB.getInstance();
			nmgztb.getContent();
		}

	}

}
