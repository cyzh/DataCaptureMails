package com.datacap.mail.data;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.datacap.mail.data.factory.DataRequest;

/**
 * 乌海市公共资源交易中心-采购招标
 * 
 * @author WenC
 * 
 */
public class WH_ZF extends DataRequest {
	private static final Logger log = Logger.getLogger(DataRequest.class);
	private static WH_ZF wh = null;
	private String url = "http://www.whggzy.com/";

	private WH_ZF() {
	}

	public static WH_ZF getInstance() {
		if (wh == null) {
			wh = new WH_ZF();
		}
		return wh;
	}

	public static WH_ZF getWH_ZF() {
		return wh;
	}

	private void close() {
		wh = null;
	}

	public void start() {
		beginParse("post");
	}

	@Override
	protected Map<String, Object> parseHtml(Document document) {
		Map<String, Object> map = null;
		Elements list = document.getElementsByTag("tbody");
		if (list == null || list.isEmpty()) {
			mIsOver = true;
			return null;
		}
		Elements trs = list.get(6).getElementsByTag("tr");
		for (Element e : trs) {
			Elements tds = e.getElementsByTag("td");
			String date = tds.get(1).text().trim();
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
		mSource = "乌海市公共资源交易中心-采购招标";
		int currentPage = 1;
		mCharset = "utf-8";
		mUri = "http://www.whggzy.com/articleWeb!list.action";
		while (true) {
			log.debug("当前获取第" + currentPage + "页数据");
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("resourceCode", "cgzbgg"));
				params.add(new BasicNameValuePair("serch", ""));
				params.add(new BasicNameValuePair("startIndex", ""
						+ (currentPage - 1) * 15));
				params.add(new BasicNameValuePair("article.title", ""));

				UrlEncodedFormEntity formDate = new UrlEncodedFormEntity(params);
				doPost(httpClient, formDate);
			} catch (UnsupportedEncodingException e) {
				log.debug("请求参数异常");
				mIsOver = true;
			}
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
