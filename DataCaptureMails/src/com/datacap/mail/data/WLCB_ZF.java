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
 * 乌兰察布市政务服务中心-政府采购
 * 
 * @author WenC
 * 
 */
public class WLCB_ZF extends DataRequest {
	private static final Logger log = Logger.getLogger(DataRequest.class);
	private static WLCB_ZF wlcb = null;
	private String url = "http://www.wlcbzwfwzx.cn/articleView/";

	private WLCB_ZF() {
	}

	public static WLCB_ZF getInstance() {
		if (wlcb == null) {
			wlcb = new WLCB_ZF();
		}
		return wlcb;
	}

	public static WLCB_ZF getWLCB_ZF() {
		return wlcb;
	}

	private void close() {
		wlcb = null;
	}

	public void start() {
		beginParse("get");
	}

	@Override
	protected Map<String, Object> parseHtml(Document document) {
		Map<String, Object> map = null;
		Elements listElements = document.getElementsByClass("con_newslist");
		if (listElements == null || listElements.size() <= 0) {
			mIsOver = true;
			return null;
		}
		Element listElement = listElements.get(0);
		Elements list = listElement.getElementsByTag("li");
		for (Element e : list) {
			Elements times = e.getElementsByTag("sup");
			if (times == null || times.size() == 0) {
				mIsOver = true;
				return null;
			}
			String date = times.get(0).text().trim();
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
		mSource = "乌兰察布市政务服务中心-政府采购";
		int currentPage = 1;
		mCharset = "utf-8";
		while (true) {
			log.debug("当前获取第" + currentPage + "页数据");
			mUri = "http://www.wlcbzwfwzx.cn/articleView/articleView!list.action?categoryCode=zfcg&categoryId=136529&domainCode=nt&page.pageNo="
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
