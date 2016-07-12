package cn.j.core.list;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
 * 内蒙古招投标网列表
 * 
 * @author WenC
 * 
 */
public class NMZTBList extends PageList {

	private static final Logger log = Logger.getLogger(NMZTBList.class);
	private static List<DataInfo> backupList = null;
	private static String backupPath = null;
	private static String pubdates = null;

	public NMZTBList() {
		// 获得当前时间
		pubdates = DateTime.beforeDayDate();

		// // 获得备份文件路径
		backupPath = NMZTBList.class.getClassLoader().getResource("/")
				.getPath();

		// 获得备份列表
		backupList = getListData();
	}

	@Override
	public List<DataInfo> getPageList() {
		List<DataInfo> list = null;
		int page = 1;
		// 1186
		while (!isOK && page <= 700) {
			if (list == null) {
				list = new ArrayList<DataInfo>();
			}
			List<DataInfo> dataList = null;
			try {
				dataList = httpClient(ReqConstant.NMZTB_URL
						+ ReqConstant.NMZTB_LIST_URL + "page=" + page);
			} catch (Exception e) {
				log.error(ReqConstant.NMZTB + " get list error, msg: "
						+ e.toString());
				e.printStackTrace();
				exceptCount++;
				if (exceptCount < 10) {
					if (exceptCount % 5 == 0) {
						log.debug(ReqConstant.NMZTB
								+ " exception current page is: " + page);
						page++;
					}
					continue;
				} else {
					log.debug(ReqConstant.NMZTB
							+ " page exception count is 10, break");
					break;
				}
			}
			exceptCount = 0;
			if (dataList != null) {
				list.addAll(dataList);
			}
			page++;
			DateTime.doSleep();// 延迟
		}
		// 如果之前没有备份则创建新备份
		if (backupList == null) {
			backupList = list;
		} else {
			if (list == null) {

			}
			backupList.addAll(list);
		}
		backupListData(backupList);
		return list;
	}

	@Override
	protected List<DataInfo> httpList(String pageHtml) {
		Document document = Jsoup.parse(pageHtml);
		Element rowsBody = document.getElementById("GridView1");
		Elements rows = rowsBody.getElementsByAttributeValue("align", "center");
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
		BackUpToFile.saveToFile(">>>>>>>>>> " + ReqConstant.NMZTB
				+ " <<<<<<<<<<");
		for (Element element : list) {
			// 获得a标签
			Element aElement = element.getElementsByTag("a").get(0);
			// 获得td标签
			Elements tds = element.getElementsByTag("td");

			String title = aElement.text();// 标题
			String href = ReqConstant.NMZTB_URL + aElement.attr("href");// 链接
			String industry = tds.get(3).text(); // 行业
			String time = pubdates;// 发布时间

			BackUpToFile.saveToFile(title);
			if (verifyList(title)) {
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
			data.setSource(ReqConstant.NMZTB);
			dataList.add(data);
			log.debug(data);
		}
		return dataList;
	}

	/**
	 * 验证list
	 * 
	 * @param list
	 *            已备份的之前的列表
	 * @param title
	 *            校验的标题
	 * @return
	 */
	private boolean verifyList(String title) {
		if (backupList != null) {
			log.debug("backup list is exist");
			for (DataInfo data : backupList) {
				if (data.getTitle().equals(title)) {
					log.debug("title is exist");
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 列表序列化
	 * 
	 * @param buList
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private boolean backupListData(List<DataInfo> buList) {
		// 获得组成路径的数组
		String[] date = DateTime.currentDate().split("-");
		String yearFolder = date[0];
		String monthFolder = date[1];

		// 构建路径
		String filePath = backupPath + "/" + yearFolder + "/" + monthFolder;
		System.out.println("filePath: " + filePath);
		File bufilePath = new File(filePath);
		if (!bufilePath.exists()) {
			bufilePath.mkdirs();
		}

		// 构建文件
		File bufile = new File(filePath + "/" + DateTime.currentDate() + ".bat");
		// 判断文件写入是否成功
		boolean flag = writeListData(bufile, buList);
		if (!flag) {
			// 删除缓存文件
			log.debug("create backupfile error, delete temp file");
			bufile.delete();
		}
		return flag;
	}

	/**
	 * 写入文件
	 * 
	 * @param bufile
	 * @return
	 */
	private boolean writeListData(File bufile, List<DataInfo> buList) {
		ObjectOutputStream oos = null;
		// 标识文件写入状态
		boolean isSuccess = true;
		try {
			// 获得文件的输出流
			oos = new ObjectOutputStream(new FileOutputStream(bufile));
			oos.writeObject(buList);
			oos.flush();
		} catch (Exception e) {
			isSuccess = false;
			log.error(e.toString());
			log.debug("backup file write error");
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isSuccess;
	}

	/**
	 * 获得序列化列表
	 * 
	 * @param path
	 * @return
	 */
	private List<DataInfo> getListData() {
		// 获得前一天的日期
		String date = DateTime.beforeDayDate();
		String[] dates = date.split("-");
		String yearFolder = dates[0];
		String monthFolder = dates[1];

		File buFile = new File(backupPath + "/" + yearFolder + "/"
				+ monthFolder + "/" + date + ".bat");
		return readListData(buFile);
	}

	/**
	 * 读取文件
	 * 
	 * @param buFile
	 * @return
	 */
	private List<DataInfo> readListData(File buFile) {
		List<DataInfo> dataList = null;
		// 创建对象读入流
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(buFile));
			List<?> backupList = (List<?>) ois.readObject();
			ois.close();
			// 有列表读入
			if (backupList != null) {
				log.debug("has backup list");
				dataList = new ArrayList<DataInfo>();
				// 将获得的对象转换为List<DataList>类型
				for (int i = 0; i < backupList.size(); i++) {
					DataInfo data = (DataInfo) backupList.get(i);
					dataList.add(data);
				}
			}
		} catch (Exception e) {
			log.error(e.toString());
			log.debug("backup file read error");
			e.printStackTrace();
		}
		return dataList;
	}
}
