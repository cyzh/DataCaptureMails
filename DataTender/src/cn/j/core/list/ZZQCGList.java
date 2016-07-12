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
 * 自治区采购网列表
 * 
 * @author WenC
 * 
 */
public class ZZQCGList extends PageList {

	private static final Logger log = Logger.getLogger(ZZQCGList.class);

	public ZZQCGList() {
		// 获得当前时间
	}

	@Override
	public List<DataInfo> getPageList() {
		String pubdates = DateTime.beforeBeforeDayDate();
		String pubdatee = DateTime.beforeDayDate();
		List<DataInfo> list = null;
		int page = 1;
		while (!isOK) {
			if (list == null) {
				list = new ArrayList<DataInfo>();
			}
			List<DataInfo> dataList = null;
			try {
				log.debug("url: " + ReqConstant.ZZQCG_URL
						+ ReqConstant.ZZQCG_LIST_URL + "pubdates=" + pubdates
						+ "&pubdatee=" + pubdatee + "&pos=" + page);

				dataList = httpClient(ReqConstant.ZZQCG_URL
						+ ReqConstant.ZZQCG_LIST_URL + "pubdates=" + pubdates
						+ "&pubdatee=" + pubdatee + "&pos=" + page);

			} catch (Exception e) {
				log.error(ReqConstant.ZZQCG + " get list error, msg: "
						+ e.toString());
				e.printStackTrace();
				exceptCount++;
				if (exceptCount < 10) {
					if (exceptCount % 5 == 0) {
						log.debug(ReqConstant.ZZQCG
								+ " exception current page is: " + page);
						page++;
					}
					continue;
				} else {
					log.debug(ReqConstant.ZZQCG
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
		Elements rows = document.getElementsByTag("tr");
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
		BackUpToFile.saveToFile(">>>>>>>>>> " + ReqConstant.ZZQCG
				+ " <<<<<<<<<<");
		for (Element element : list) {
			Element aElement = element.getElementsByTag("a").get(0);
			Element fontElement = element.getElementsByTag("font").get(0);
			Element tdElement = element.getElementsByAttribute("nowrap").get(0);

			// 标题
			String title = aElement.attr("title");
			// 连接
			String href = ReqConstant.ZZQCG_URL + aElement.attr("href");
			// 行业的缓存变量
			String industryTemp = fontElement.text().split("|")[1];
			// 行业
			String industry = industryTemp.substring(0,
					industryTemp.length() - 1);
			// 日期有‘]’
			String date = tdElement.text().split(":")[1];
			// 清除‘]’
			String time = date.substring(0, date.length() - 1);

			BackUpToFile.saveToFile(title);
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
			data.setSource(ReqConstant.ZZQCG);
			dataList.add(data);
			log.debug(data);
		}
		return dataList;
	}

}
