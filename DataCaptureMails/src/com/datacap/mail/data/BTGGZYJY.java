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
 * 包头市公共资源交易信息网
 * 
 * @author WenC
 * 
 */
public class BTGGZYJY extends DataRequest {
	private static final Logger log = Logger.getLogger(DataRequest.class);
	private static BTGGZYJY btggzyjy = null;
	private String url = "http://www.btggzyjy.cn";
	private int currentPage = 1;
	private String __VIEWSTATE = "";

	private BTGGZYJY() {
	}

	public static BTGGZYJY getInstance() {
		if (btggzyjy == null) {
			btggzyjy = new BTGGZYJY();
		}
		return btggzyjy;
	}

	public static BTGGZYJY getBTGGZYJY() {
		return btggzyjy;
	}

	public void start() {
		beginParse("post");
	}

	private void close() {
		btggzyjy = null;
	}

	@Override
	protected Map<String, Object> parseHtml(Document document) {
		Map<String, Object> map = null;

		if (currentPage == 1) {
			__VIEWSTATE = document.getElementById("__VIEWSTATE").val();
		}
		Element list = document.getElementById("MoreInfoList1_tdcontent");
		if (list == null) {
			mIsOver = true;
			return null;
		}
		Elements trs = list.getElementsByTag("tr");
		for (Element e : trs) {
			Elements tds = e.getElementsByTag("td");
			String date = tds.get(2).text().trim();
			// utils.yesterday()
			if (date.compareTo(utils.yesterday()) < 0) {
				log.debug("日期不符，结束");
				mIsOver = true;
				break;
			}
			Element a = tds.get(1).getElementsByTag("a").get(0);
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
		mSource = "包头市公共资源交易信息网";
		mCharset = "utf-8";
		mUri = "http://www.btggzyjy.cn/btzbtb/jyxx/001001/MoreInfo.aspx?CategoryNum=001001";
		while (true) {
			log.debug("当前获取第" + currentPage + "页数据");
			if (currentPage == 1) {
				doGet(httpClient);
			} else {
				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("__EVENTARGUMENT", ""
							+ currentPage));
					params.add(new BasicNameValuePair("__EVENTTARGET",
							"MoreInfoList1$Pager"));
					params.add(new BasicNameValuePair("__VIEWSTATE",
							__VIEWSTATE));
					UrlEncodedFormEntity formDate = new UrlEncodedFormEntity(
							params);
					doPost(httpClient, formDate);
				} catch (UnsupportedEncodingException e) {
					log.debug("请求参数异常");
					mIsOver = true;
				}
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
