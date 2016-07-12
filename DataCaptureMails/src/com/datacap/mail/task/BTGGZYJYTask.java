package com.datacap.mail.task;

import java.util.TimerTask;

import com.datacap.mail.data.BTGGZYJY;

public class BTGGZYJYTask extends TimerTask {

	private static BTGGZYJYTask task = null;

	private BTGGZYJYTask() {
	}

	public static BTGGZYJYTask getBTGGZYJYTask() {
		if (task == null) {
			task = new BTGGZYJYTask();
		}
		return task;
	}

	@Override
	public void run() {
		BTGGZYJY btggzyjy = BTGGZYJY.getBTGGZYJY();
		if (btggzyjy == null) {
			btggzyjy = BTGGZYJY.getInstance();
			btggzyjy.start();
		}
	}
}
