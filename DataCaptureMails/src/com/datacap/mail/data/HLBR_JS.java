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
 * 呼伦贝尔公共资源交易中心-建设招标
 * 
 * @author WenC
 * 
 */
public class HLBR_JS extends DataRequest {
	private static final Logger log = Logger.getLogger(DataRequest.class);
	private static HLBR_JS hlbr = null;
	private String url = "http://www.hlbeggzyjy.org.cn";

	private HLBR_JS() {
	}

	public static HLBR_JS getInstance() {
		if (hlbr == null) {
			hlbr = new HLBR_JS();
		}
		return hlbr;
	}

	public static HLBR_JS getHLBR_JS() {
		return hlbr;
	}

	private void close() {
		hlbr = null;
	}

	public void start() {
		beginParse("get");
	}

	@Override
	protected Map<String, Object> parseHtml(Document document) {
		Map<String, Object> map = null;
		Elements listElements = document.getElementsByClass("c1-body");
		if (listElements == null || listElements.isEmpty()) {
			mIsOver = true;
			return null;
		}
		Elements list = listElements.get(0).getElementsByClass("c1-bline");
		for (Element e : list) {
			Elements divs = e.getElementsByTag("div");
			if (divs == null || divs.isEmpty()) {
				mIsOver = true;
				return null;
			}
			String date = divs.get(2).text().trim();
			date = date.substring(1, date.length() - 1);
			// utils.yesterday()
			if (date.compareTo(utils.yesterday()) < 0) {
				log.debug("日期不符，结束");
				mIsOver = true;
				break;
			}
			Element a = divs.get(0).getElementsByTag("a").get(0);
			String title = a.attr("title");
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
		mSource = "呼伦贝尔公共资源交易中心-建设招标";
		int currentPage = 1;
		mCharset = "utf-8";
		while (true) {
			log.debug("当前获取第" + currentPage + "页数据");
			mUri = "http://www.hlbeggzyjy.org.cn/page/index_" + currentPage
					+ ".jspx?type=notice&code=biddingNotice_dyproject";
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
