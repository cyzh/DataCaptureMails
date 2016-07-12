package com.datacap.mail.task;

import java.util.TimerTask;

import com.datacap.mail.data.XLGLM;

public class XLGLMTask extends TimerTask {

	private static XLGLMTask task = null;

	private XLGLMTask() {
	}

	public static XLGLMTask getXLGLMTask() {
		if (task == null) {
			task = new XLGLMTask();
		}
		return task;
	}

	@Override
	public void run() {
		XLGLM xlglm = XLGLM.getXLGLM();
		if (xlglm == null) {
			xlglm = XLGLM.getInstance();
			xlglm.start();
		}
	}
}
