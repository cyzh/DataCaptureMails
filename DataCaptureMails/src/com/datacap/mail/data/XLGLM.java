package com.datacap.mail.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.datacap.mail.data.factory.DataRequest;

/**
 * 锡林郭勒盟政务公共资源中心
 * 
 * @author WenC
 * 
 */
public class XLGLM extends DataRequest {
	private static final Logger log = Logger.getLogger(DataRequest.class);
	private static XLGLM xlglm = null;
	private String url = "http://www.xmzwggzy.com";
	private static String year = null;

	private XLGLM() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		year = format.format(date);
	}

	public static XLGLM getInstance() {
		if (xlglm == null) {
			xlglm = new XLGLM();
		}
		return xlglm;
	}

	public static XLGLM getXLGLM() {
		return xlglm;
	}

	private void close() {
		xlglm = null;
	}

	public void start() {
		beginParse("get");
	}

	@Override
	protected Map<String, Object> parseHtml(Document document) {
		Map<String, Object> map = null;
		Elements list = document.getElementsByTag("tbody");
		if (list == null || list.isEmpty()) {
			mIsOver = true;
			return null;
		}
		Elements trs = list.get(0).getElementsByTag("tr");
		for (Element e : trs) {
			Elements tds = e.getElementsByTag("td");
			String date = tds.get(3).text().trim();
			date = year + "-" + date.substring(1, date.length() - 1);
			if (date.compareTo(utils.yesterday()) < 0) {
				log.debug("日期不符，结束");
				mIsOver = true;
				break;
			}
			Element a = tds.get(2).getElementsByTag("a").get(0);
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
		mSource = "锡林郭勒盟政务公共资源中心";
		int currentPage = 1;
		mCharset = "utf-8";
		while (true) {
			log.debug("当前获取第" + currentPage + "页数据");
			mUri = "http://www.xmzwggzy.com/xmweb/showinfo/zbgg_moreNew.aspx?categoryNum=009001005001&categoryNum2=009002006001&categoryNum3=009003003001&categoryNum4=009004003001&categoryNum5=009002008&Paging="
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
