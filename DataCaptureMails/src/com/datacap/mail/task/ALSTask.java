package com.datacap.mail.task;

import java.util.TimerTask;

import com.datacap.mail.data.ALS_JS;
import com.datacap.mail.data.ALS_ZF;

public class ALSTask extends TimerTask {

	private static ALSTask alstask = null;

	private ALSTask() {
	}

	public static ALSTask getALSTask() {
		if (alstask == null) {
			alstask = new ALSTask();
		}
		return alstask;
	}

	@Override
	public void run() {
		ALS_JS als_js = ALS_JS.getALS_JS();
		if (als_js == null) {
			als_js = ALS_JS.getInstance();
			als_js.start();
		}

		ALS_ZF als_zf = ALS_ZF.getALS_ZF();
		if (als_zf == null) {
			als_zf = ALS_ZF.getInstance();
			als_zf.start();
		}
	}

}
