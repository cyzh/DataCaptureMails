package com.datacap.mail.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.datacap.mail.data.factory.DataRequest;

/**
 * 内蒙古交通厅
 * 
 * @author WenC
 * 
 */
public class NMJTT extends DataRequest {
	private static final Logger log = Logger.getLogger(DataRequest.class);
	private static NMJTT nmjtt = null;
	private String url = "http://www.nmjt.gov.cn";

	private NMJTT() {
	}

	public static NMJTT getInstance() {
		if (nmjtt == null) {
			nmjtt = new NMJTT();
		}
		return nmjtt;
	}

	public static NMJTT getNMJTT() {
		return nmjtt;
	}

	private void close() {
		nmjtt = null;
	}

	public void start() {
		beginParse("get");
	}

	@Override
	protected Map<String, Object> parseHtml(Document document) {
		Map<String, Object> map = null;
		Elements listElements = document.getElementsByClass("rightbg");
		if (listElements == null || listElements.size() <= 0) {
			mIsOver = true;
			return null;
		}
		Element listElement = listElements.get(0);
		Elements list = listElement.getElementsByTag("dl");
		for (Element e : list) {
			String date = e.getElementsByTag("dd").get(0).text().trim();
			// utils.yesterday()
			if (date.compareTo(utils.yesterday()) < 0) {
				log.debug("日期不符，结束");
				mIsOver = true;
				break;
			}
			Element a = e.getElementsByTag("a").get(0);
			String title = a.text().trim();
			log.debug("日期：" + date + "，标题：" + title);
			boolean titleFlag = utils.mailTitle(title);
			if (!titleFlag) {
				log.debug("题目不符合，跳过");
				continue;
			}
			String link = url + a.attr("href");
			if (map == null) {
				map = new HashMap<String, Object>();
			}
			map.put("title", title);
			map.put("link", link);
			map.put("date", date);
			log.debug(mSource + "，列表" + map.toString());
		}
		return map;
	}

	@Override
	protected void traversePage(CloseableHttpClient httpClient) {
		mSource = "内蒙古交通厅";
		int currentPage = 1;
		mCharset = "utf-8";
		while (true) {
			log.debug("当前获取第" + currentPage + "页数据");
			if (currentPage == 1) {
				mUri = "http://www.nmjt.gov.cn/jtzw/gsgg/zbgg/index.shtml";
			} else {
				mUri = "http://www.nmjt.gov.cn/jtzw/gsgg/zbgg/index_"
						+ currentPage + ".shtml";
			}
			doGet(httpClient);
			// 如果抓取结束了，则跳出循环
			if (mIsOver == true) {
				log.debug(mSource + "，比选完毕");
				break;
			}
			currentPage++;
		}
		close();
	}
}
