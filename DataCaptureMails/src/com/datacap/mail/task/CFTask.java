package com.datacap.mail.task;

import java.util.TimerTask;

import com.datacap.mail.data.CF_JS_HW;
import com.datacap.mail.data.CF_JS_JL;
import com.datacap.mail.data.CF_JS_KC;
import com.datacap.mail.data.CF_JS_QT;
import com.datacap.mail.data.CF_JS_SG;
import com.datacap.mail.data.CF_JS_SJ;
import com.datacap.mail.data.CF_ZF_FW;
import com.datacap.mail.data.CF_ZF_GC;
import com.datacap.mail.data.CF_ZF_HW;

public class CFTask extends TimerTask {

	private static CFTask task = null;

	private CFTask() {
	}

	public static CFTask getCFTask() {
		if (task == null) {
			task = new CFTask();
		}
		return task;
	}

	@Override
	public void run() {
		CF_JS_HW cf_js_hw = CF_JS_HW.getCF_JS_HW();
		if (cf_js_hw == null) {
			cf_js_hw = CF_JS_HW.getInstance();
			cf_js_hw.start();
		}
		CF_JS_JL cf_js_jl = CF_JS_JL.getCF_JS_JL();
		if (cf_js_jl == null) {
			cf_js_jl = CF_JS_JL.getInstance();
			cf_js_jl.start();
		}
		CF_JS_KC cf_js_kc = CF_JS_KC.getCF_JS_KC();
		if (cf_js_kc == null) {
			cf_js_kc = CF_JS_KC.getInstance();
			cf_js_kc.start();
		}
		CF_JS_QT cf_js_qt = CF_JS_QT.getCF_JS_QT();
		if (cf_js_qt == null) {
			cf_js_qt = CF_JS_QT.getInstance();
			cf_js_qt.start();
		}
		CF_JS_SG cf_js_sg = CF_JS_SG.getCF_JS_SG();
		if (cf_js_sg == null) {
			cf_js_sg = CF_JS_SG.getInstance();
			cf_js_sg.start();
		}
		CF_JS_SJ cf_js_sj = CF_JS_SJ.getCF_JS_SJ();
		if (cf_js_sj == null) {
			cf_js_sj = CF_JS_SJ.getInstance();
			cf_js_sj.start();
		}
		CF_ZF_FW cf_zf_fw = CF_ZF_FW.getCF_ZF_FW();
		if (cf_zf_fw == null) {
			cf_zf_fw = CF_ZF_FW.getInstance();
			cf_zf_fw.start();
		}
		CF_ZF_GC cf_zf_gc = CF_ZF_GC.getCF_ZF_GC();
		if (cf_zf_gc == null) {
			cf_zf_gc = CF_ZF_GC.getInstance();
			cf_zf_gc.start();
		}
		CF_ZF_HW cf_zf_hw = CF_ZF_HW.getCF_ZF_HW();
		if (cf_zf_hw == null) {
			cf_zf_hw = CF_ZF_HW.getInstance();
			cf_zf_hw.start();
		}

	}

}
