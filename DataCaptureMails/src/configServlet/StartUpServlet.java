package configServlet;

import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.datacap.mail.task.ALSTask;
import com.datacap.mail.task.BTGGZYJYTask;
import com.datacap.mail.task.BYNRTask;
import com.datacap.mail.task.CBTask;
import com.datacap.mail.task.CFTask;
import com.datacap.mail.task.EEDSTask;
import com.datacap.mail.task.GJZBWTask;
import com.datacap.mail.task.HHHTTask;
import com.datacap.mail.task.HLBRTask;
import com.datacap.mail.task.NMGPTask;
import com.datacap.mail.task.NMGZTBTask;
import com.datacap.mail.task.NMJTTTask;
import com.datacap.mail.task.TLTask;
import com.datacap.mail.task.WHTask;
import com.datacap.mail.task.WLCBTask;
import com.datacap.mail.task.XLGLMTask;

public class StartUpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(StartUpServlet.class);

	@Override
	public void init() throws ServletException {
		log.debug("-----*-----*-- 启动 --*----*----");
		/*-- 中国采购网 --*/
		new Thread() {
			public void run() {
				log.debug("-----*-----*-- 中国采购网，启动 --*----*----");
				Timer timer = new Timer();
				timer.schedule(CBTask.getCBTask(), 0, 1000 * 60 * 30);
			};
		}.start();

		// /*-- 国际招标网 --*/
		new Thread() {
			public void run() {
				log.debug("-----*-----*-- 国际招标网，启动 --*----*----");
				Timer timer = new Timer();
				timer.schedule(GJZBWTask.getGJZBWTask(), 0, 1000 * 60 * 30);
			};
		}.start();

		// /*-- 自治区采购网 --*/// 慢
		new Thread() {
			public void run() {
				log.debug("-----*-----*-- 自治区采购网，启动 --*----*----");
				Timer timer = new Timer();
				timer.schedule(NMGPTask.getNMGPTask(), 0, 1000 * 60 * 30);
			};
		}.start();

		/*-- 内蒙古招投标协会 --*/
		new Thread() {
			public void run() {
				log.debug("-----*-----*-- 内蒙古招投标协会，启动 --*----*----");
				Timer timer = new Timer();
				timer.schedule(NMGZTBTask.getNMGZTBTask(), 0, 1000 * 60 * 30);
			};
		}.start();

		/*-- 阿拉善盟公共资源交易网 --*/
		new Thread() {
			public void run() {
				log.debug("-----*-----*-- 阿拉善盟公共资源交易网，启动 --*----*----");
				Timer timer = new Timer();
				timer.schedule(ALSTask.getALSTask(), 0, 1000 * 60 * 30);
			};
		}.start();

		/*-- 包头市公共资源交易信息网 --*/
		new Thread() {
			public void run() {
				log.debug("-----*-----*-- 包头市公共资源交易信息网，启动 --*----*----");
				Timer timer = new Timer();
				timer.schedule(BTGGZYJYTask.getBTGGZYJYTask(), 0,
						1000 * 60 * 30);
			};
		}.start();

		/*-- 巴彦淖尔市政服务中心 --*/
		new Thread() {
			public void run() {
				log.debug("-----*-----*-- 巴彦淖尔市政服务中心，启动 --*----*----");
				Timer timer = new Timer();
				timer.schedule(BYNRTask.getBYNRTask(), 0, 1000 * 60 * 30);
			};
		}.start();

		/*-- 赤峰公共资源交易网 --*/
		new Thread() {
			public void run() {
				log.debug("-----*-----*-- 赤峰公共资源交易网，启动 --*----*----");
				Timer timer = new Timer();
				timer.schedule(CFTask.getCFTask(), 0, 1000 * 60 * 30);
			};
		}.start();

		/*-- 鄂尔多斯公共资源交易中心 --*/
		new Thread() {
			public void run() {
				log.debug("-----*-----*-- 鄂尔多斯公共资源交易中心，启动 --*----*----");
				Timer timer = new Timer();
				timer.schedule(EEDSTask.getEEDSTask(), 0, 1000 * 60 * 30);
			};
		}.start();

		/*-- 呼和浩特市公共资源交易中心 --*/
		new Thread() {
			public void run() {
				log.debug("-----*-----*-- 呼和浩特市公共资源交易中心，启动 --*----*----");
				Timer timer = new Timer();
				timer.schedule(HHHTTask.getHHHTTask(), 0, 1000 * 60 * 30);
			};
		}.start();

		/*-- 呼伦贝尔公共资源交易中心 --*/
		new Thread() {
			public void run() {
				log.debug("-----*-----*-- 呼伦贝尔公共资源交易中心，启动 --*----*----");
				Timer timer = new Timer();
				timer.schedule(HLBRTask.getHLBRTask(), 0, 1000 * 60 * 30);
			};
		}.start();

		/*-- 内蒙古交通厅 --*/
		new Thread() {
			public void run() {
				log.debug("-----*-----*-- 内蒙古交通厅，启动 --*----*----");
				Timer timer = new Timer();
				timer.schedule(NMJTTTask.getNMJTTTask(), 0, 1000 * 60 * 30);
			};
		}.start();

		/*-- 通辽市公共资源交易中心 --*/
		new Thread() {
			public void run() {
				log.debug("-----*-----*-- 通辽市公共资源交易中心，启动 --*----*----");
				Timer timer = new Timer();
				timer.schedule(TLTask.getTLTask(), 0, 1000 * 60 * 30);
			};
		}.start();

		/*-- 乌海市公共资源交易中心 --*/
		new Thread() {
			public void run() {
				log.debug("-----*-----*-- 乌海市公共资源交易中心，启动 --*----*----");
				Timer timer = new Timer();
				timer.schedule(WHTask.getWHTask(), 0, 1000 * 60 * 30);
			};
		}.start();

		/*-- 乌兰察布市政务服务中心 --*/
		new Thread() {
			public void run() {
				log.debug("-----*-----*-- 乌兰察布市政务服务中心，启动 --*----*----");
				Timer timer = new Timer();
				timer.schedule(WLCBTask.getWLCBTask(), 0, 1000 * 60 * 30);
			};
		}.start();

		/*-- 锡林郭勒盟政务公共资源中心 --*/
		new Thread() {
			public void run() {
				log.debug("-----*-----*-- 锡林郭勒盟政务公共资源中心，启动 --*----*----");
				Timer timer = new Timer();
				timer.schedule(XLGLMTask.getXLGLMTask(), 0, 1000 * 60 * 30);
			};
		}.start();
	}
}
