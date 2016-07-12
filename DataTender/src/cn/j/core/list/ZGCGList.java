package cn.j.core.list;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.j.constant.ReqConstant;
import cn.j.core.abs.PageList;
import cn.j.core.entity.DataInfo;
import cn.j.utils.BackUpToFile;
import cn.j.utils.DateTime;
import cn.j.utils.Verify;

/**
 * 
 * 中国采购网列表
 * 
 * @author WenC
 * 
 */
public class ZGCGList extends PageList {

	private static Logger log = Logger.getLogger(ZGCGList.class);

	public ZGCGList() {

	}

	@Override
	public List<DataInfo> getPageList() {
		List<DataInfo> list = null;
		int page = 1;
		while (!isOK) {
			if (list == null) {
				list = new ArrayList<DataInfo>();
			}
			List<DataInfo> dataList = null;
			try {
				dataList = httpClient(ReqConstant.ZGCG_URL
						+ ReqConstant.ZGCG_LIST_URL + "&page=" + page);
			} catch (Exception e) {
				log.error(ReqConstant.ZGCG + " get list error, msg: "
						+ e.toString());
				e.printStackTrace();
				exceptCount++;
				if (exceptCount < 10) {
					if (exceptCount % 5 == 0) {
						log.debug(ReqConstant.ZGCG
								+ " exception current page is: " + page);
						page++;
					}
					continue;
				} else {
					log.debug(ReqConstant.ZGCG
							+ " page exception count is 10, break");
					break;
				}
			}
			exceptCount = 0;
			if (dataList != null) {
				list.addAll(list.size(), dataList);
			}
			page++;
			DateTime.doSleep();// 延迟
		}
		return list;
	}

	@Override
	protected List<DataInfo> httpList(String pageHtml) {
		Document document = Jsoup.parse(pageHtml);
		Elements row1s = document.getElementsByClass("listrow1");
		Elements row2s = document.getElementsByClass("listrow2");

		// 用于合并row1s和row2s
		Elements rows = null;
		if (row1s == null || row1s.isEmpty()) {
			if (row2s == null || row2s.isEmpty()) {
				log.debug("list1 and list2 are empty");
				isOK = true;
				return null;
			} else {
				log.debug("only list2");
				rows = row2s;
			}
		} else {
			if (row2s == null || row2s.isEmpty()) {
				log.debug("only list1");
				rows = row1s;
			} else {
				log.debug("list1 and list2 both have");
				rows = new Elements();
				// 合并list1列表和list2列表，按页面顺序排序
				int size = row1s.size() + row2s.size();
				for (int i = 0; i < size; i++) {
					if (row1s.isEmpty()) {
						if (row2s.isEmpty()) {
							log.debug("list1 and list2 are empty");
							break;
						} else {
							log.debug("list1 is empty，list2 is not empty");
							rows.add(i, row2s.get(0));
							row2s.remove(0); // remove list2 index 0's element
						}
					} else {
						if (row2s.isEmpty()) {
							log.debug("list1 is not empty, list2 is empty");
							rows.add(i, row1s.get(0));
							row1s.remove(0); // remove list1 index 0's element
						} else {
							log.debug("list1 and list2 are not empty");
							if (i % 2 == 0) {
								rows.add(i, row1s.get(0));
								row1s.remove(0);
							} else {
								rows.add(i, row2s.get(0));
								row2s.remove(0);
							}
						}
					}
				}
			}
		}
		return parseList(rows);
	}

	@Override
	protected List<DataInfo> parseList(Elements list) {
		List<DataInfo> dataList = null;
		BackUpToFile.saveToFile(">>>>>>>>>> " + ReqConstant.ZGCG
				+ " <<<<<<<<<<");
		for (Element element : list) {
			// get ‘a’ tag
			Element aElement = element.getElementsByTag("a").get(0);
			Element divElement = element.getElementsByTag("div").get(1);

			String title = aElement.text();// 标题
			String href = ReqConstant.ZGCG_URL + aElement.attr("href");// 链接
			String industry = element.getElementsByTag("td").get(4).text(); // 行业
			String time = divElement.text().split(" ")[0];// 发布时间

			BackUpToFile.saveToFile(title);
			// 判断时间是否是当天
			if (!Verify.verifyTime(time)) {
				log.debug("Get list over");
				isOK = true;
				break;
			}

			if (!Verify.verifyTitle(title)) {
				log.debug("title is illagel");
				continue;
			}

			if (dataList == null) {
				log.debug("new data list");
				dataList = new ArrayList<DataInfo>();
			}
			DataInfo data = new DataInfo();
			data.setHref(href);
			data.setIndustry(industry);
			data.setTime(time);
			data.setTitle(title);
			data.setSource(ReqConstant.ZGCG);
			dataList.add(data);
			log.debug(data);
		}
		return dataList;
	}
}
