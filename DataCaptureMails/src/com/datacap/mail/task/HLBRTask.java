package com.datacap.mail.task;

import java.util.TimerTask;

import com.datacap.mail.data.HLBR_CG;
import com.datacap.mail.data.HLBR_JS;
import com.datacap.mail.data.HLBR_XEGC;

public class HLBRTask extends TimerTask {

	private static HLBRTask task = null;

	private HLBRTask() {
	}

	public static HLBRTask getHLBRTask() {
		if (task == null) {
			task = new HLBRTask();
		}
		return task;
	}

	@Override
	public void run() {
		HLBR_CG hlbr_cg = HLBR_CG.getHLBR_CG();
		if (hlbr_cg == null) {
			hlbr_cg = HLBR_CG.getInstance();
			hlbr_cg.start();
		}
		HLBR_JS hlbr_js = HLBR_JS.getHLBR_JS();
		if (hlbr_js == null) {
			hlbr_js = HLBR_JS.getInstance();
			hlbr_js.start();
		}
		HLBR_XEGC hlbr_xegc = HLBR_XEGC.getHLBR_XEGC();
		if (hlbr_xegc == null) {
			hlbr_xegc = HLBR_XEGC.getInstance();
			hlbr_xegc.start();
		}
	}
}
