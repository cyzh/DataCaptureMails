package com.datacap.mail.task;

import java.util.TimerTask;

import com.datacap.mail.data.EEDS_JS;
import com.datacap.mail.data.EEDS_ZF;

public class EEDSTask extends TimerTask {

	private static EEDSTask task = null;

	private EEDSTask() {
	}

	public static EEDSTask getEEDSTask() {
		if (task == null) {
			task = new EEDSTask();
		}
		return task;
	}

	@Override
	public void run() {
		EEDS_JS eeds_js = EEDS_JS.getEEDS_JS();
		if (eeds_js == null) {
			eeds_js = EEDS_JS.getInstance();
			eeds_js.start();
		}
		EEDS_ZF eeds_zf = EEDS_ZF.getEEDS_ZF();
		if (eeds_zf == null) {
			eeds_zf = EEDS_ZF.getInstance();
			eeds_zf.start();
		}
	}
}
