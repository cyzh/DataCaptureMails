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
 * 中国采购与招标网
 * 
 * @author WenC
 * 
 */
public class ZGCG extends DataRequest {

	private static final Logger log = Logger.getLogger(DataRequest.class);
	private static ZGCG zgcg = null;

	private ZGCG() {
	}

	public static ZGCG getInstance() {
		if (zgcg == null) {
			zgcg = new ZGCG();
		}
		return zgcg;
	}

	public static ZGCG getZGCG() {
		return zgcg;
	}

	public void start() {
		beginParse("get");
	}

	private void close() {
		zgcg = null;
	}

	@Override
	protected Map<String, Object> parseHtml(Document document) throws Exception {
		Map<String, Object> map = null;
		Elements listrow1 = document.getElementsByClass("listrow1");
		Elements listrow2 = document.getElementsByClass("listrow2");
		log.debug("listrow1 size: " + listrow1.size() + ", listrow2 size: "
				+ listrow2.size());
		// 如果日期超过一天的则不进行遍历
		for (int i = 0; i < listrow2.size(); i++) {
			listrow1.add((2 * i) + 1, listrow2.get(i));
		}

		// 获得链接
		for (int i = 0; i < listrow1.size(); i++) {
			Element e = listrow1.get(i);
			Elements as = e.getElementsByTag("a");
			String title = as.text().trim();
			// 判断标题是否含有指定的字段
			log.debug("正在扫描第" + i + "条记录：" + title);
			boolean titleFlag = utils.mailTitle(title);

			Elements td = e.getElementsByTag("td");
			String date = td.get(5).text().split(" ")[0];
			// 如果不是日期格式则显示今天
			date = utils.isDate(date);
			if (date.compareTo(utils.yesterday()) < 0) {
				log.debug("日期不在范围内");
				mIsOver = true;
				break;
			}
			// 题目不符合条件，跳过
			if (!titleFlag) {
				log.debug("题目不符合，跳过");
				continue;
			}
			String link = "http://www.chinabidding.com.cn" + as.attr("href"); // 获得链接地址
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
		int currentPage = 1;
		mSource = "中国采购与招标网";
		while (true) {
			log.debug(mSource + "：当前第" + currentPage + "页");
			mUri = "http://www.chinabidding.com.cn/search/searchzbw/search2?rp=22&categoryid=&keywords=&page="
					+ currentPage + "&areaid=7&table_type=0&b_date=year";
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
