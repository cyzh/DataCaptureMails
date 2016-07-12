package com.datacap.mail.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.datacap.mail.dao.ISendMailDao;
import com.datacap.mail.dao.impl.SendMailDaoImpl;

public class JUtils {
	private static final Logger log = Logger.getLogger(JUtils.class);
	private ISendMailDao sendDao = null;

	public JUtils() {
		sendDao = new SendMailDaoImpl();
	}

	/**
	 * 将yyyy年MM月dd日的日期格式转换为yyyy-MM-dd格式
	 * 
	 * @param date
	 * @return
	 */
	public String formatDate(String date) {
		date = date.replace("年", "-").replace("月", "-").replace("日", "");
		// 如果获得的日期是1900-01-01，转换为今天
		if (date.equals("1900-01-01")) {
			date = currentDay();
		}
		System.out.println(date);
		return date;
	}

	/**
	 * 判断当日
	 * 
	 * @return
	 */
	public String currentDay() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String currentYear = format.format(date);
		return currentYear;
	}

	public String yesterday() {
		String date = currentDay();
		int day = (Integer.parseInt(date.split("-")[2]) - 1);
		String d = "";
		if (day / 10 == 0) {
			d = "0";
		}
		date = date.substring(0, date.length() - 2) + d + day;
		log.debug("date: " + date);
		return date;
	}

	/**
	 * 判断是不是日期格式
	 * 
	 * @param date
	 * @return
	 */
	public String isDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			format.parse(date);
		} catch (ParseException e) {
			log.debug("非日期格式，设置为当天日期，知道发送为");
			date = currentDay();
		}
		return date;
	}

	/**
	 * 判断邮件是否发送
	 * 
	 * @param title
	 * @return
	 */
	public boolean isSend(String source, String title, String date) {
		List<Object> params = new ArrayList<Object>();
		params.add(source);
		params.add(title);
		params.add(date);
		return sendDao.findSender(params);
	}

	/**
	 * 名称是否符合发送邮件的条件
	 * 
	 * @param title
	 * @return
	 */
	public boolean mailTitle(String title) {
		for (int i = 0; i < Constant.getInstance().getMailFilter().length; i++) {
			if (title.contains(Constant.getInstance().getMailFilter()[i])) {
				return true;
			}
		}
		return false;
	}

	public void saveSend(String source, List<Map<String, Object>> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		for (Map<String, Object> map : list) {
			String title = (String) map.get("title");
			String date = (String) map.get("date");
			List<Object> params = new ArrayList<Object>();
			params.add(source);
			params.add(title);
			params.add(date);
			boolean flag = sendDao.newSender(params);
			if (flag) {
				log.debug("新增记录成功");
			} else {
				log.debug("新增记录失败");
			}
		}
	}

	/**
	 * 发送邮件
	 * 
	 * @param List
	 */
	public void sendMail(final String source, List<Map<String, Object>> list) {

		if (list == null || list.isEmpty()) {
			log.debug("发送列表为空");
			return;
		}

		log.debug("列表不为空，原始列表大小为：" + list.size());
		for (int i = 0; i < list.size(); i++) {
			// 消除列表中的重复数据
			for (int j = 0; j < i; j++) {
				if (list.get(j).get("title").toString()
						.equals(list.get(i).get("title").toString())) {
					log.debug("该信息在列表中存在");
					list.remove(i);
					i -= 1;
					break;
				}
			}
		}

		log.debug("列表不为空，列表过滤后大小为：" + list.size());
		final List<Map<String, Object>> mailList = list;
		new Thread(new Runnable() {

			List<Map<String, Object>> list = mailList;

			@Override
			public void run() {

				StringBuffer sb = new StringBuffer();
				sb.append("<table border='1' cellspacing='0' style='text-align: center;'><tr><th width='550'>标题</th><th width='100'>日期</th></tr>");
				for (int i = 0; i < list.size(); i++) {
					log.debug("获取第" + i + "条信息，内容为: " + list.get(i).toString());
					String title = (String) list.get(i).get("title");
					String date = (String) list.get(i).get("date");
					boolean titleFlag = isSend(source, title, date);
					if (titleFlag) {
						log.debug("该条信息已经发送过");
						list.remove(i);
						i -= 1;
						continue;
					}

					log.debug("该条信息没有发送过");
					String link = (String) list.get(i).get("link");
					sb.append("<tr><td width='500'><a href='" + link + "'>"
							+ title + "</a></td><td width='100'>" + date
							+ "</td></tr>");
				}
				sb.append("</table>");
				log.debug("要发送的邮件列表长度: " + list.size());
				if (list.size() > 0) {
					log.debug("邮件列表不为空，发送邮件");
					Mail.sendMessage(source + "-->条件筛选结果列表", sb.toString());
					saveSend(source, list);
				}
			}
		}).start();

	}
}
