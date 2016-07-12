package com.datacap.mail.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * 自治区采购网
 * 
 * @author WenC
 * 
 */
public class NMGP {
	private static final Logger log = Logger.getLogger(NMGP.class);
	private List<Map<String, Object>> mailList = null;
	private JUtils utils = null;
	private static NMGP nmgp = null;

	private NMGP() {
		utils = new JUtils();
		mailList = new ArrayList<Map<String, Object>>();
	}

	public static NMGP getInstance() {
		if (nmgp == null) {
			nmgp = new NMGP();
		}
		return nmgp;
	}

	public static NMGP getNMGP() {
		return nmgp;
	}

	private void closeNMGP() {
		nmgp = null;
	}

	public boolean Start() {
		getContent();
		return true;
	}

	private void getContent() {

		int currentPage = 1;
		String url = "http://www.nmgp.gov.cn/procurement/pages/tender.jsp?pos=";
		String source = "内蒙古自治区采购网";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		while (true) {
			log.debug(source + "：当前第" + currentPage + "页");
			boolean isOver = false;
			try {
				// 下载列表页面内容
				HttpGet httpget = new HttpGet(url + currentPage);
				CloseableHttpResponse response = httpclient.execute(httpget);
				String html = EntityUtils.toString(response.getEntity());
				response.close();

				Document document = Jsoup.parse(html);
				Elements contents = document.getElementsByTag("tr");

				log.debug("抓取列表： " + currentPage);
				int i = 0;
				for (Element link : contents) {
					String linkText = link.getElementsByTag("td").get(0).text();
					linkText = linkText.substring(2).trim();
					String title = linkText
							.substring(linkText.indexOf("]") + 1).trim();

					// 判断标题是否含有指定的字段
					log.debug("正在扫描第" + i + "条记录：" + title);
					boolean titleFlag = utils.mailTitle(title);

					// 判断日期
					String linkdate = link.getElementsByTag("td").get(1).text();
					String endDate = linkdate.substring(
							linkdate.indexOf(":") + 1, linkdate.length() - 1);

					if (endDate.compareTo(utils.yesterday()) < 0) {
						log.debug("日期不在范围内");
						isOver = true;
						break;
					}
					i++;
					// 题目不符合条件，跳过
					if (!titleFlag) {
						log.debug("题目不符合，跳过");
						continue;
					}
					// 截取地域
					log.debug("截取的地址为：" + linkText);
					// 匹配行业
					linkText = linkText.substring(linkText.indexOf("|") + 1);
					// 截取链接
					String alink = "http://www.nmgp.gov.cn"
							+ link.getElementsByTag("a").attr("href");
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
				closeNMGP();
				break;
			}
			// 保存数据到数据库
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
}
