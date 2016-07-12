package com.datacap.mail.task;

import java.util.TimerTask;

import com.datacap.mail.data.BYNE_CQJY;
import com.datacap.mail.data.BYNE_GTZY;
import com.datacap.mail.data.BYNE_JS;
import com.datacap.mail.data.BYNE_QT;
import com.datacap.mail.data.BYNE_ZF;

public class BYNRTask extends TimerTask {

	private static BYNRTask task = null;

	private BYNRTask() {
	}

	public static BYNRTask getBYNRTask() {
		if (task == null) {
			task = new BYNRTask();
		}
		return task;
	}

	@Override
	public void run() {
		BYNE_CQJY byne_cqjy = BYNE_CQJY.getBYNE_CQJY();
		if (byne_cqjy == null) {
			byne_cqjy = BYNE_CQJY.getInstance();
			byne_cqjy.start();
		}
		BYNE_GTZY byne_gtzy = BYNE_GTZY.getBYNE_GTZY();
		if (byne_gtzy == null) {
			byne_gtzy = BYNE_GTZY.getInstance();
			byne_gtzy.start();
		}
		BYNE_JS byne_js = BYNE_JS.getBYNE_JS();
		if (byne_js == null) {
			byne_js = BYNE_JS.getInstance();
			byne_js.start();
		}
		BYNE_QT byne_qt = BYNE_QT.getBYNE_QT();
		if (byne_qt == null) {
			byne_qt = BYNE_QT.getInstance();
			byne_qt.start();
		}
		BYNE_ZF byne_zf = BYNE_ZF.getBYNE_ZF();
		if (byne_zf == null) {
			byne_zf = BYNE_ZF.getInstance();
			byne_zf.start();
		}
	}
}
