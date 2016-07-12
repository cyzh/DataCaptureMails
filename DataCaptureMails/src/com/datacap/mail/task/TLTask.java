package com.datacap.mail.task;

import java.util.TimerTask;

import com.datacap.mail.data.TL_JS;
import com.datacap.mail.data.TL_KYQ;
import com.datacap.mail.data.TL_TD;
import com.datacap.mail.data.TL_ZF;

public class TLTask extends TimerTask {

	private static TLTask task = null;

	private TLTask() {
	}

	public static TLTask getTLTask() {
		if (task == null) {
			task = new TLTask();
		}
		return task;
	}

	@Override
	public void run() {
		TL_JS tl_js = TL_JS.getTL_JS();
		if (tl_js == null) {
			tl_js = TL_JS.getInstance();
			tl_js.start();
		}
		TL_KYQ tl_kyq = TL_KYQ.getTL_KYQ();
		if (tl_kyq == null) {
			tl_kyq = TL_KYQ.getInstance();
			tl_kyq.start();
		}
		TL_TD tl_td = TL_TD.getTL_TD();
		if (tl_td == null) {
			tl_td = TL_TD.getInstance();
			tl_td.start();
		}
		TL_ZF tl_zf = TL_ZF.getTL_ZF();
		if (tl_zf == null) {
			tl_zf = TL_ZF.getInstance();
			tl_zf.start();
		}
	}
}
