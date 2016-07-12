package com.datacap.mail.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.datacap.mail.data.factory.DataRequest;

public class GJZB extends DataRequest {
	private static final Logger log = Logger.getLogger(DataRequest.class);
	private static GJZB gjzb = null;

	private GJZB() {
	}

	public static GJZB getInstance() {
		if (gjzb == null) {
			gjzb = new GJZB();
		}
		return gjzb;
	}

	public static GJZB getGJZB() {
		return gjzb;
	}

	public void start() {
		beginParse("get");
	}

	private void close() {
		gjzb = null;
	}

	@Override
	protected Map<String, Object> parseHtml(Document document) throws Exception {
		Map<String, Object> map = null;
		Elements contents = document.getElementsByClass("as-pager-item");
		int i = 0;
		for (Element e : contents) {
			String title = e.getElementsByAttribute("title").get(0)
					.attr("title").trim();
			// 判断标题是否含有指定的字段
			log.debug("正在扫描第" + i + "条记录：" + title);
			boolean titleFlag = utils.mailTitle(title);
			String endDate = e.getElementsByClass("time").get(0).text()
					.substring(5);
			if (endDate.compareTo(utils.yesterday()) < 0) {
				log.debug("日期不在范围内");
				mIsOver = true;
				break;
			}
			i++;
			// 题目不符合条件，跳过
			if (!titleFlag) {
				log.debug("题目不符合，跳过");
				continue;
			}
			String alink = e.attr("href");
			if (map == null) {
				map = new HashMap<String, Object>();
			}
			map.put("title", title);
			map.put("link", alink);
			map.put("date", endDate);
			log.debug(mSource + "，列表" + map.toString());
		}
		return map;
	}

	@Override
	protected void traversePage(CloseableHttpClient httpClient) {
		int currentPage = 1;
		mSource = "国际招标网";
		while (true) {
			log.debug(mSource + "：当前第" + currentPage + "页");
			mUri = "http://www.chinabidding.com/search/proj.htm?poClass=&infoClassCodes=0105&zoneCode=15*&currentPage="
					+ currentPage;
			doGet(httpClient);
			if (mIsOver == true) {
				log.debug(mSource + "，比选完毕");
				break;
			}
			currentPage++;
		}
		close();

	}

}
