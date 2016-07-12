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
 * 国际招标网列表
 * 
 * @author WenC
 * 
 */
public class GJZBList extends PageList {

	private static final Logger log = Logger.getLogger(GJZBList.class);

	public GJZBList() {
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
				dataList = httpClient(ReqConstant.GJZB_URL
						+ ReqConstant.GJZB_LIST_URL + "&currentPage=" + page);
			} catch (Exception e) {
				log.error(ReqConstant.GJZB_URL + " get list error, msg: "
						+ e.toString());
				e.printStackTrace();
				exceptCount++;
				if (exceptCount < 10) {
					if (exceptCount % 5 == 0) {
						log.debug(ReqConstant.GJZB_URL
								+ " exception current page is: " + page);
						page++;
					}
					continue;
				} else {
					log.debug(ReqConstant.GJZB_URL
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
		Elements rowsBody = document.getElementsByClass("as-pager-body");
		System.out.println("--->" + rowsBody.size());
		Elements rows = rowsBody.get(0).getElementsByTag("li");
		System.out.println("------>" + rows.size());
		if (rows == null || rows.isEmpty()) {
			log.debug("list is null");
			isOK = true;
			return null;
		}
		return parseList(rows);
	}

	@Override
	protected List<DataInfo> parseList(Elements list) {
		List<DataInfo> dataList = null;
		BackUpToFile.saveToFile(">>>>>>>>>> "+ReqConstant.GJZB +" <<<<<<<<<<");
		for (Element element : list) {
			Element aElement = element.getElementsByTag("a").get(0);
			Elements spans = element.getElementsByTag("span");

			String title = spans.get(1).attr("title");// 标题
			String href = aElement.attr("href");// 链接
			String industry = element.getElementsByTag("strong").get(0).text(); // 行业
			String time = spans.get(2).text().split("：")[1];// 发布时间

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
				log.debug("new datat list");
				dataList = new ArrayList<DataInfo>();
			}
			DataInfo data = new DataInfo();
			data.setHref(href);
			data.setIndustry(industry);
			data.setTime(time);
			data.setTitle(title);
			data.setSource(ReqConstant.GJZB);
			dataList.add(data);
			log.debug(data);
		}
		return dataList;
	}

}
