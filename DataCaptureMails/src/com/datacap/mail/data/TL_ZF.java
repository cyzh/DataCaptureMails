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
 * 通辽市公共资源交易中心-政府采购
 * 
 * @author WenC
 * 
 */
public class TL_ZF extends DataRequest {
	private static final Logger log = Logger.getLogger(DataRequest.class);
	private static TL_ZF tl = null;
	private String url = "http://www.tlzw.net/index/";

	private TL_ZF() {
	}

	public static TL_ZF getInstance() {
		if (tl == null) {
			tl = new TL_ZF();
		}
		return tl;
	}

	public static TL_ZF getTL_ZF() {
		return tl;
	}

	private void close() {
		tl = null;
	}

	public void start() {
		beginParse("get");
	}

	@Override
	protected Map<String, Object> parseHtml(Document document) {
		Map<String, Object> map = null;
		Elements listElements = document.getElementsByTag("tbody");
		if (listElements == null || listElements.size() <= 0) {
			mIsOver = true;
			return null;
		}
		Element listElement = listElements.get(2);
		Elements list = listElement.getElementsByTag("tr");
		for (Element e : list) {
			Elements tds = e.getElementsByTag("td");
			if (tds == null || tds.size() == 0) {
				mIsOver = true;
				return null;
			}
			String date = tds.get(1).text().trim();
			// utils.yesterday()
			if (date.compareTo(utils.yesterday()) < 0) {
				log.debug("日期不符，结束");
				mIsOver = true;
				break;
			}
			Element a = tds.get(0).getElementsByTag("a").get(0);
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
		mSource = "通辽市公共资源交易中心-政府采购";
		int currentPage = 1;
		mCharset = "utf-8";
		while (true) {
			log.debug("当前获取第" + currentPage + "页数据");
			mUri = "http://www.tlzw.net/index/showInfoListJiaoyiNew.action?infoPid=24&infoMid=1139193556826&page="
					+ currentPage;
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
