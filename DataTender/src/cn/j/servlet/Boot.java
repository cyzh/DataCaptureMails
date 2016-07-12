package cn.j.servlet;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import cn.j.core.entity.DataInfo;
import cn.j.core.list.task.DataList;
import cn.j.core.report.Report;
import cn.j.utils.DateTime;
import cn.j.utils.TableConfig;
import cn.j.utils.Verify;

public class Boot extends HttpServlet {

	private static final long serialVersionUID = -5764111138977579701L;
	private static final Logger log = Logger.getLogger(Boot.class);

	@Override
	public void init() throws ServletException {
		TableConfig.getInstance().doTableConfig();

		// 定时器，每10分钟检查一次
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// 判断时间是否符合
				if (Verify.canRun(Report.getReport().getRunDate())) {
					log.debug("data list can run");
					DataList datalist = DataList.getDatalist();
					if (datalist == null) {
						Report report = Report.getReport();
						// 获得开始时间
						report.setStartTime(new Date());
						log.debug("data list starting...");
						datalist = DataList.getInstance();
						List<DataInfo> infoList = datalist.start();
						int flag = datalist.saveList(infoList);
						log.debug("save count: " + flag);
						datalist.close();
						// 设置运行时间
						report.setRunDate(DateTime.currentDate());
						// 获得结束时间
						report.setEndTime(new Date());
						// 执行报表
						report.doReport();
						log.debug("do report");
					} else {
						log.debug("data list is running...");
					}
				} else {
					log.debug("can not run");

				}
			}
		}, 2000, 60 * 10 * 1000); // 60 * 10 * 1000
	}
}
