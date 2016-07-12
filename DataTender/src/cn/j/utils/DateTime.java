package cn.j.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DateTime {

	public static String currentDateTime() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	/**
	 * 获得当前日期
	 * 
	 * @return
	 */
	public static String currentDate() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	/**
	 * 前一天
	 * 
	 * @return
	 */
	public static String beforeDayDate() {
		Date date = new Date();
		Date before = new Date(date.getTime() - 24 * 3600 * 1000);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(before);
	}

	public static String beforeBeforeDayDate() {
		Date date = new Date();
		Date before = new Date(date.getTime() - 24 * 3600 * 1000l);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String beforeDate = format.format(before);
		System.out.println("before date: " + beforeDate);
		return beforeDate;
	}

	public static String afterDayDate() {
		Date date = new Date();
		Date before = new Date(date.getTime() + 24 * 3600 * 1000);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(before);
	}

	public static long randomSecond() {
		Random rand = new Random();
		Long time = 0L;
		do {
			time = Math.abs(rand.nextLong()) % 3000;
		} while (time / 1000 < 1);
		System.out.println("delay: " + time);
		return time / 10;
	}

	public static void doSleep() {
		try {
			Thread.sleep(randomSecond());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
