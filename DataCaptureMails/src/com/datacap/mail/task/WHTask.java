package com.datacap.mail.task;

import java.util.TimerTask;

import com.datacap.mail.data.WH_JS;
import com.datacap.mail.data.WH_ZF;

public class WHTask extends TimerTask {

	private static WHTask task = null;

	private WHTask() {
	}

	public static WHTask getWHTask() {
		if (task == null) {
			task = new WHTask();
		}
		return task;
	}

	@Override
	public void run() {
		WH_JS wh_js = WH_JS.getWH_JS();
		if (wh_js == null) {
			wh_js = WH_JS.getInstance();
			wh_js.start();
		}
		WH_ZF wh_zf = WH_ZF.getWH_ZF();
		if (wh_zf == null) {
			wh_zf = WH_ZF.getInstance();
			wh_zf.start();
		}
	}
}
