package com.datacap.mail.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.datacap.mail.data.factory.DataRequest;

public class FactoryTest extends DataRequest {
	private static final Logger log = Logger.getLogger(DataRequest.class);

	public FactoryTest() {
		super();
	}

	public void getContent() {
		beginParse("get");
	}

	@Override
	protected Map<String, Object> parseHtml(Document document) {
		Map<String, Object> map = null;
		Elements listrow1 = document.getElementsByClass("listrow1");
		Elements listrow2 = document.getElementsByClass("listrow2");

		// 判断是否抓取完毕，如果抓取完毕则将mIsOver置为false
		if ((listrow1 == null || listrow1.isEmpty())
				|| (listrow2 == null || listrow2.isEmpty())) {
			mIsOver = false;
		}

		for (int i = 0; i < listrow2.size(); i++) {
			listrow1.add((2 * i) + 1, listrow2.get(i));
		}

		log.debug("listrow1 size : " + listrow1.size());
		// 获得链接
		for (int i = 0; i < listrow1.size(); i++) {
			Element e = listrow1.get(i);
			Elements as = e.getElementsByTag("a");
			String title = as.text().trim();
			log.debug("title: " + title);

			// 判断标题是否含有指定的字段
			boolean titleFlag = utils.mailTitle(title);
			Elements td = e.getElementsByTag("td");
			String endDate = td.get(5).text().split(" ")[0];

			// 如果不是日期格式则显示今天
			endDate = utils.isDate(endDate);
			if (endDate.compareTo(utils.yesterday()) < 0) {
				log.debug("日期不符合，结束");
				// 日期不符合，将mIsOver置为false
				mIsOver = true;
				return null;
			}

			// 题目不符合条件，跳过
			if (!titleFlag) {
				log.debug("题目不符合，跳过");
				continue;
			}
			String alink = "http://www.chinabidding.com.cn" + as.attr("href"); // 获得链接地址
			if (map == null) {
				map = new HashMap<String, Object>();
			}
			map.put("title", title);
			map.put("link", alink);
			map.put("date", endDate);
		}
		return map;
	}

	@Override
	protected void traversePage(CloseableHttpClient httpClient) {
		int currentPage = 1;
		mCharset = "utf-8";
		while (true) {
			log.debug("当前获取第" + currentPage + "页数据");
			mUri = "http://www.chinabidding.com.cn/search/searchzbw/search2?rp=22&categoryid=&keywords=&page="
					+ currentPage + "&areaid=7&table_type=0&b_date=year";
			doGet(httpClient);
			// 如果抓取结束了，则跳出循环
			if (mIsOver == true) {
				break;
			}
			currentPage++;
		}
	}
}
