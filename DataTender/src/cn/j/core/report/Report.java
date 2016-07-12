package cn.j.core.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import cn.j.constant.ReqConstant;

/**
 * 抓取数量占比分析
 * 
 * @author WenC
 * 
 */
public class Report {
	private static final Logger log = Logger.getLogger(Report.class);
	private static Report report = null;
	// 作统计的时间
	private String runDate = null;
	private double allCount = 100.00;
	private Date startTime = null;
	private Date endTime = null;
	private int zzqcgCount = 0;
	private int nmztbCount = 0;
	private int zgcgCount = 0;
	private int gjzbCount = 0;

	private Report() {
	}

	public static Report getReport() {
		if (report == null) {
			report = new Report();
		}
		return report;
	}

	/**
	 * 执行报告
	 */
	public void doReport() {

		String path = Report.class.getResource("/").getPath();

		File file = new File(path + "report/");
		if (!file.exists()) {
			log.debug("create report path");
			file.mkdirs();
		}
		File reportFile = new File(path + "report/report.txt");
		if (!reportFile.exists()) {
			log.debug("create report file");
			try {
				reportFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			writeReport(reportFile);
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}
	}

	private void writeReport(File reportFile) throws Exception {

		String zgcg = ReqConstant.ZGCG;
		String gjzb = ReqConstant.GJZB;
		String zzqcg = ReqConstant.ZZQCG;
		String nmztb = ReqConstant.NMZTB;

		long time = (endTime.getTime() - startTime.getTime()) / 1000;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String start = format.format(startTime);
		String end = format.format(endTime);

		String reportStr = "[开始时间:" + start + "]\r\n\t"
				+ reportMsg(zgcg, zgcgCount) + "\t|\t"
				+ reportMsg(gjzb, gjzbCount) + "\t|\t"
				+ reportMsg(zzqcg, zzqcgCount) + "\t|\t"
				+ reportMsg(nmztb, nmztbCount) + "\r\n[结束时间:" + end
				+ "]\r\n[耗时" + time + "s]\r\n";

		FileOutputStream fos = new FileOutputStream(reportFile, true);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
		osw.write(reportStr + "\r\n");
		osw.close();
	}

	/**
	 * 计算比重
	 * 
	 * @param source
	 * @param count
	 * @return
	 */
	private String reportMsg(String source, int count) {
		DecimalFormat df = new DecimalFormat("0.00");
		return source + "：" + count + "，占比："
				+ df.format(count * 1.0 / allCount * 100) + "%";
	}

	public void setZgcgCount(int zgcgCount) {
		this.zgcgCount = zgcgCount;
	}

	public void setGjzbCount(int gjzbCount) {
		this.gjzbCount = gjzbCount;
	}

	public void setZzqcgCount(int zzqcgCount) {
		this.zzqcgCount = zzqcgCount;
	}

	public void setNmztbCount(int nmztbCount) {
		this.nmztbCount = nmztbCount;
	}

	public void setAllCount(double allCount) {
		this.allCount = allCount;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getRunDate() {
		return runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}

}
