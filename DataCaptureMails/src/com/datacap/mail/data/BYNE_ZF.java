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
 * 巴彦淖尔市政服务中心-政府采购
 * 
 * @author WenC
 * 
 */
public class BYNE_ZF extends DataRequest {
	private static final Logger log = Logger.getLogger(DataRequest.class);
	private static BYNE_ZF byne_js = null;
	private String url = "http://zwzx.bynr.gov.cn/";

	private BYNE_ZF() {
	}

	public static BYNE_ZF getInstance() {
		if (byne_js == null) {
			byne_js = new BYNE_ZF();
		}
		return byne_js;
	}

	public static BYNE_ZF getBYNE_ZF() {
		return byne_js;
	}

	public void start() {
		beginParse("post");
	}

	private void close() {
		byne_js = null;
	}

	@Override
	protected Map<String, Object> parseHtml(Document document) {
		Map<String, Object> map = null;
		Elements list = document.getElementsByTag("tbody");
		if (list == null || list.isEmpty()) {
			mIsOver = true;
			return null;
		}
		Elements trs = list.get(3).getElementsByTag("tr");
		for (Element e : trs) {
			Elements tds = e.getElementsByTag("td");
			String date = tds.get(1).text().trim();
			System.out.println(date);
			// utils.yesterday()
			if (date.compareTo("2015-07-30") < 0) {
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
		mSource = "巴彦淖尔市政服务中心-政府采购";
		mCharset = "utf-8";
		int currentPage = 1;
		mUri = "http://zwzx.bynr.gov.cn/articleWeb!list.action";
		while (true) {
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("resourceCode", "cgzbgg"));
				params.add(new BasicNameValuePair("type", "cg"));
				params.add(new BasicNameValuePair("wbnewsfield", "all"));
				params.add(new BasicNameValuePair("startIndex", ""
						+ (currentPage - 1) * 15));
				params.add(new BasicNameValuePair("infocontent", ""));

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
