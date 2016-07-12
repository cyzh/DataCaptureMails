package com.datacap.mail.task;

import java.util.TimerTask;

import com.datacap.mail.data.WLCB_JS;
import com.datacap.mail.data.WLCB_ZF;

public class WLCBTask extends TimerTask {

	private static WLCBTask task = null;

	private WLCBTask() {
	}

	public static WLCBTask getWLCBTask() {
		if (task == null) {
			task = new WLCBTask();
		}
		return task;
	}

	@Override
	public void run() {
		WLCB_JS wlcb_js = WLCB_JS.getWLCB_JS();
		if (wlcb_js == null) {
			wlcb_js = WLCB_JS.getInstance();
			wlcb_js.start();
		}
		WLCB_ZF wlcb_zf = WLCB_ZF.getWLCB_ZF();
		if (wlcb_zf == null) {
			wlcb_zf = WLCB_ZF.getInstance();
			wlcb_zf.start();
		}
	}
}
