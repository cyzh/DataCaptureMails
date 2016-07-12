package cn.j.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Verify {

	/**
	 * 验证时间
	 * 
	 * @param time
	 * @return
	 */
	public static boolean verifyTime(String time) {
		// Date date = new Date();
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (time.compareTo(DateTime.beforeDayDate()) >= 0) {
			return true;
		}
		return false;
	}

	public static boolean canRun(String runDate) {
		// 验证是否已经运行过一次
		if (runDate != null && !runDate.isEmpty()
				&& DateTime.currentDate().compareTo(runDate) <= 0) {
			return false;
		}
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		String time = format.format(date);

		// 获得配置文件的时间
		ConfFile file = ConfFile.getInstance();
		String startTime = file.getStartTime();
		String endTime = file.getEndTime();
		System.out.println("current time: " + time + "\nstart time: "
				+ startTime + "\nend time: " + endTime);
		System.out.println();
		// 比较
		if (time.compareTo(startTime) > 0 && time.compareTo(endTime) < 0) {
			return true;
		}
		return false;
	}

	/**
	 * 验证标题
	 * 
	 * @param title
	 * @return
	 */
	public static boolean verifyTitle(String title) {
		String[] titleAllow = ConfFile.getInstance().getTitleAllow();
		String[] titleDenial = ConfFile.getInstance().getTitleDenial();

		for (int i = 0; i < titleAllow.length; i++) {
			// 判断是否有允许的字段
			if (title.contains(titleAllow[i])) {
				// 判断是否有非法字段
				for (int j = 0; j < titleDenial.length; j++) {
					if (title.contains(titleDenial[j])) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
}
