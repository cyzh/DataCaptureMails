package com.datacap.mail.task;

import java.util.TimerTask;

import com.datacap.mail.data.HHHT_JS;
import com.datacap.mail.data.HHHT_JT;
import com.datacap.mail.data.HHHT_SL;
import com.datacap.mail.data.HHHT_ZF;

public class HHHTTask extends TimerTask {

	private static HHHTTask task = null;

	private HHHTTask() {
	}

	public static HHHTTask getHHHTTask() {
		if (task == null) {
			task = new HHHTTask();
		}
		return task;
	}

	@Override
	public void run() {
		HHHT_JS hhht_js = HHHT_JS.getHHHT_JS();
		if (hhht_js == null) {
			hhht_js = HHHT_JS.getInstance();
			hhht_js.start();
		}
		HHHT_JT hhht_jt = HHHT_JT.getHHHT_JT();
		if (hhht_jt == null) {
			hhht_jt = HHHT_JT.getInstance();
			hhht_jt.start();
		}
		HHHT_SL hhht_sl = HHHT_SL.getHHHT_SL();
		if (hhht_sl == null) {
			hhht_sl = HHHT_SL.getInstance();
			hhht_sl.start();
		}
		HHHT_ZF hhht_zf = HHHT_ZF.getHHHT_ZF();
		if (hhht_zf == null) {
			hhht_zf = HHHT_ZF.getInstance();
			hhht_zf.start();
		}
	}
}
