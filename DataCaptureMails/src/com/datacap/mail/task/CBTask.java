package com.datacap.mail.task;

import java.util.TimerTask;

import com.datacap.mail.data.ZGCG;

public class CBTask extends TimerTask {

	private static CBTask cbtask = null;

	private CBTask() {
	}

	public static CBTask getCBTask() {
		if (cbtask == null) {
			cbtask = new CBTask();
		}
		return cbtask;
	}

	@Override
	public void run() {
		// 是否可以抓取
		ZGCG zgcg = ZGCG.getZGCG();
		if (zgcg == null) {
			zgcg = ZGCG.getInstance();
			zgcg.start();
		}
	}

}
