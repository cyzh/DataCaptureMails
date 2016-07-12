package cn.j.core.list.task;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.j.constant.ReqConstant;
import cn.j.core.client.GJZBClient;
import cn.j.core.client.NMZTBClient;
import cn.j.core.client.ZGCGClient;
import cn.j.core.client.ZZQCGClient;
import cn.j.core.entity.DataInfo;
import cn.j.core.report.Report;
import cn.j.data.dao.Datacapture;
import cn.j.data.session.HSession;
import cn.j.utils.DateTime;
import cn.j.utils.TableConfig;

/**
 * 数据列表的操作类
 * 
 * @author WenC
 * 
 */
public class DataList {

	private static final Logger log = Logger.getLogger(DataList.class);
	private static DataList datalist = null;
	public static boolean IS_RUN = false;
	private String tableName = null;

	private DataList() {
		tableName = TableConfig.getInstance().getTableName();
	}

	public static DataList getInstance() {
		if (datalist == null) {
			datalist = new DataList();
		}
		return datalist;
	}

	public static DataList getDatalist() {
		return datalist;
	}

	/**
	 * 开启抓取
	 */
	public List<DataInfo> start() {
		List<DataInfo> list = null;
		ListTask listTask = new ListTask();
		try {
			list = listTask.listFilter();
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 清空对象
	 */
	public void close() {
		log.debug("close datalist");
		datalist = null;
	}

	public int saveList(List<DataInfo> datas) {
		GJZBClient gjzbClient = new GJZBClient();
		NMZTBClient nmztbClient = new NMZTBClient();
		ZGCGClient zgcgClient = new ZGCGClient();
		ZZQCGClient zzqcgClient = new ZZQCGClient();

		int count = 0;
		int zgcgCount = 0;
		int gjzbCount = 0;
		int zzqcgCount = 0;
		int nmztbCount = 0;
		for (DataInfo info : datas) {
			DateTime.doSleep();

			IS_RUN = true;
			String title = info.getFullTitle();
			String source = info.getSource();
			if (hasTitle(title)) {
				log.debug(title + " already exist");
				continue;
			}
			Datacapture datacap = datacaplist(info);
			if (ReqConstant.ZGCG.equals(source)) {
				String page = zgcgClient.doExecute(info.getHref());
				datacap = zgcgClient.parsePage(page, datacap);
				log.debug(ReqConstant.ZGCG + "：查询内内容");
			} else if (ReqConstant.GJZB.equals(source)) {
				String page = gjzbClient.doExecute(info.getHref());
				datacap = gjzbClient.parsePage(page, datacap);
				log.debug(ReqConstant.GJZB + "：查询内内容");
			} else if (ReqConstant.ZZQCG.equals(source)) {
				String page = zzqcgClient.doExecute(info.getHref());
				datacap = zzqcgClient.parsePage(page, datacap);
				log.debug(ReqConstant.ZZQCG + "：查询内内容");
			} else if (ReqConstant.NMZTB.equals(source)) {
				String page = nmztbClient.doExecute(info.getHref());
				datacap = nmztbClient.parsePage(page, datacap);
				log.debug(ReqConstant.NMZTB + "：查询内内容");
			}

			int flag = save(datacap);
			if (flag != -1) {
				if (ReqConstant.ZGCG.equals(source)) {
					zgcgCount++;
					log.debug(ReqConstant.ZGCG + ":" + zgcgCount);
				} else if (ReqConstant.GJZB.equals(source)) {
					gjzbCount++;
					log.debug(ReqConstant.GJZB + ":" + gjzbCount);
				} else if (ReqConstant.ZZQCG.equals(source)) {
					zzqcgCount++;
					log.debug(ReqConstant.ZZQCG + ":" + zzqcgCount);
				} else if (ReqConstant.NMZTB.equals(source)) {
					nmztbCount++;
					log.debug(ReqConstant.NMZTB + ":" + nmztbCount);
				}
				count++;
			}
			log.debug("save " + count + "s datas");
		}
		IS_RUN = false;
		Report report = Report.getReport();
		report.setZgcgCount(zgcgCount);
		report.setGjzbCount(gjzbCount);
		report.setZzqcgCount(zzqcgCount);
		report.setNmztbCount(nmztbCount);
		report.setAllCount(count);
		return count;
	}

	/**
	 * 判断标题是否存在
	 * 
	 * @param title
	 * @return
	 */
	private boolean hasTitle(String title) {
		Session session = null;
		try {
			session = HSession.getSession();
			Query query = session.createSQLQuery("SELECT COUNT(*) FROM "
					+ tableName + " WHERE title=?");
			query.setString(0, title);
			int size = ((BigInteger) query.uniqueResult()).intValue();
			if (size > 0) {
				return true;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			session.close();
			HSession.closeSession();
		}

		return false;

	}

	private Datacapture datacaplist(DataInfo info) {
		Datacapture dp = new Datacapture();
		// 来源
		dp.setSource(info.getSource());
		// 标题
		dp.setTitle(info.getFullTitle());
		// 链接
		dp.setPageurl(info.getHref());
		// 区域
		if (info.getArea() == null || info.getArea().isEmpty()) {
			dp.setArea("自治区");
		} else {
			dp.setArea(info.getArea());
		}
		// 区域id
		dp.setAreaId(info.getAreaId());
		// 城市id
		dp.setCityId(info.getCityId());
		// 公司
		dp.setCompetitor(info.getCompany());
		// 产业分类
		dp.setIndustry(info.getIndustry());
		// 省份id
		dp.setProvinceId(1);
		// 是否下载过
		dp.setIsDownload(0);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(info.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dp.setEnddate(date);
		return dp;
	}

	private int save(Datacapture data) {
		Session session = null;
		Transaction t = null;
		int id = -1;
		try {
			session = HSession.getSession();
			t = session.beginTransaction();
			id = (Integer) session.save(data);
			t.commit();
		} catch (Exception e) {
			log.debug("save list item error");
			t.rollback();
		} finally {
			session.close();
			HSession.closeSession();
		}
		if (id != -1) {
			id = 1;
		}
		return id;
	}
}
