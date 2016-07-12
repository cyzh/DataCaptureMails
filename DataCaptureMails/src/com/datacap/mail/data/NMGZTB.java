package com.datacap.mail.data;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.datacap.mail.utils.JUtils;

/**
 * 内蒙古招投标协会
 * 
 * @author WenC
 * 
 */
public class NMGZTB {
	private static final Logger log = Logger.getLogger(NMGZTB.class);
	private List<Map<String, Object>> mailList = null;
	private JUtils utils = null;
	private static NMGZTB nmgztb = null;

	private NMGZTB() {
		utils = new JUtils();
		mailList = new ArrayList<Map<String, Object>>();
	}

	public static NMGZTB getInstance() {
		if (nmgztb == null) {
			nmgztb = new NMGZTB();
		}
		return nmgztb;
	}

	public static NMGZTB getNMGZTB() {
		return nmgztb;
	}

	private void closeNMGZTB() {
		nmgztb = null;
	}

	/**
	 * 获得招投标协会招标信息
	 */
	public void getContent() {
		int currentPage = 1;
		String url = "http://www.nmgztb.com.cn/zhaolist.aspx";
		String source = "内蒙古招标投标协会";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		while (true) {
			log.debug(source + "：当前第" + currentPage + "页");
			URI uri = null;
			try {
				uri = new URI(url + "?page=" + currentPage);
			} catch (URISyntaxException e2) {
				e2.printStackTrace();
			}
			httpGet.setURI(uri);
			boolean isOver = false;
			try {
				CloseableHttpResponse response = httpclient.execute(httpGet);
				String html = EntityUtils.toString(response.getEntity(), "gbk");
				response.close();
				Document page = Jsoup.parse(html);

				// 爬取页面的信息
				Element content = page.getElementById("GridView1");
				Elements contentList = content.getElementsByTag("tr");
				// 清除没用的内容
				contentList.remove(0);
				contentList.remove(contentList.size() - 1);
				contentList.remove(contentList.size() - 1);

				// 获取每项的具体内容
				int i = 0;
				for (Element e : contentList) {
					Element as = e.getElementsByTag("a").get(0);
					String title = as.text().trim();
					String alink = "http://www.nmgztb.com.cn/"
							+ as.attr("href");
					// 判断标题是否含有指定的字段
					log.debug("正在扫描第" + i + "条记录：" + title);
					i++;
					Map<String, String> titDate = titleDate(alink);
					// 如果标题和时间map为空，直接跳过
					if (titDate == null) {
						log.debug("标题和时间map为空，跳过");
						continue;
					}
					String endDate = titDate.get("date");
					// 如果截取到的日期为空，直接跳过执行下一条
					if (endDate == null) {
						log.debug("日期为空，跳过");
						continue;
					}
					endDate = utils.formatDate(endDate);
					// 判断日期
					if (endDate.compareTo(utils.yesterday()) < 0) {
						log.debug("日期不在范围内");
						isOver = true;
						break;
					}

					title = titDate.get("title");
					boolean titleFlag = utils.mailTitle(title);
					// 题目不符合条件，跳过
					if (!titleFlag) {
						log.debug("题目不符合，跳过");
						continue;
					}

					title = title.substring(1).substring(0, title.length() - 9);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("title", title);
					map.put("link", alink);
					map.put("date", endDate);
					mailList.add(map);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 如果抓取完毕，跳出循环
			if (isOver) {
				log.debug("抓取完毕");
				closeNMGZTB();
				break;
			}
			currentPage++;
		}
		try {
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 发送邮件
		utils.sendMail(source, mailList);
	}

	private Map<String, String> titleDate(String url) {
		Map<String, String> map = null;
		String title = null;
		String time = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		try {
			CloseableHttpResponse response = httpclient.execute(httpGet);
			String html = EntityUtils.toString(response.getEntity(), "gbk");
			response.close();
			Document page = Jsoup.parse(html);
			Elements trs = page.getElementsByTag("tr");
			// 遍历出有招标文件的时间
			for (Element tr : trs) {
				String trText = tr.text();
				if (trText.contains("招标项目名称")) {
					title = tr.getElementsByTag("td").get(1).text();
					log.debug("项目名称: " + title);
					continue;
				}
				if (trText.contains("招标文件开始时间")) {
					time = tr.getElementsByTag("td").get(1).text();
					log.debug("招标文件开始时间: " + time);
					break;
				} else if (trText.contains("资格预审开始时间")) {
					time = tr.getElementsByTag("td").get(1).text();
					log.debug("招标文件开始时间: " + time);
					break;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if ((title == null || title.isEmpty())
				|| (time == null || time.isEmpty())) {
			log.debug("标题和时间都为空值，title：" + title + "，time：" + time);
			return null;
		}
		// 获得日期
		time = time.substring(1, time.indexOf("日") + 1);
		// 如果获得的时间是1900-01-01，则转换为今天
		map = new HashMap<String, String>();
		map.put("date", time);
		map.put("title", title);
		return map;
	}
}
