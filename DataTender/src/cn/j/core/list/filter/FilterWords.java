package cn.j.core.list.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.j.data.dao.ObjectCompany;
import cn.j.data.dao.ObjectDictionariesArea;
import cn.j.data.session.HSession;
import cn.j.utils.ConfFile;

/**
 * 过滤的字段
 * 
 * @author WenC
 * 
 */
public class FilterWords {

	private static final Logger log = Logger.getLogger(FilterWords.class);

	public FilterWords() {
	}

	/**
	 * 获得数据库中城市列表，按城市字典id分组，城市名为同一城市的分一组，最后返回Map，键名为城市字典id
	 * 
	 * @return
	 */
	public Map<String, List<String>> citys() {
		Session session = null;
		List<?> list = null;
		try {
			session = HSession.getSession();
			Query query = session
					.createSQLQuery("SELECT * FROM object_dictionaries_city");
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			session.close();
			HSession.closeSession();
		}

		Map<String, List<String>> map = null;
		// 判断是否有数据列表返回
		if (list != null && !list.isEmpty()) {
			map = new HashMap<String, List<String>>();
			// 城市id的
			int temp = 0;
			// 城市列表，同一城市为一组
			List<String> cityList = null;
			Iterator<?> itr = list.iterator();
			while (itr.hasNext()) {
				Object[] objs = (Object[]) itr.next();

				String objectDictionariesCityName = String.valueOf(objs[2]);
				int objectDictionariesCityId = Integer.parseInt(String
						.valueOf(objs[1]));
				log.debug(objectDictionariesCityName);
				// 如果城市id与之前存储的不同，则需生成新列表
				if (objectDictionariesCityId != temp) {
					if (cityList != null) {
						log.debug("city list is not null");
						map.put("" + temp, cityList);
					}
					cityList = new ArrayList<String>();
					cityList.add(objectDictionariesCityName);
					temp = objectDictionariesCityId;
				} else {
					log.debug("put city to city list");
					cityList.add(objectDictionariesCityName);
				}
			}
		}
		return map;
	}

	/**
	 * 获得城市中对象的列表
	 * 
	 * @return
	 */
	public Map<String, List<ObjectDictionariesArea>> cityAreas() {
		Map<String, List<ObjectDictionariesArea>> map = null;
		Session session = null;
		List<?> list = null;
		try {
			session = HSession.getSession();
			Query query = session
					.createSQLQuery("SELECT * FROM object_dictionaries_area");
			list = query.list();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			session.close();
			HSession.closeSession();
		}

		List<ObjectDictionariesArea> areaList = null;
		Iterator<?> itr = list.iterator();
		while (itr.hasNext()) {
			if (map == null) {
				map = new HashMap<String, List<ObjectDictionariesArea>>();
			}
			Object[] objs = (Object[]) itr.next();
			int objectDictionariesId = Integer
					.parseInt(String.valueOf(objs[0]));
			int objectDictionariesCityId = Integer.parseInt(String
					.valueOf(objs[1]));
			int objectDictionariesAreaId = Integer.parseInt(String
					.valueOf(objs[2]));
			String objectDictionariesAreaName = String.valueOf(objs[3]);

			ObjectDictionariesArea area = new ObjectDictionariesArea();
			area.setObjectDictionariesId(objectDictionariesId);
			area.setObjectDictionariesCityId(objectDictionariesCityId);
			area.setObjectDictionariesAreaId(objectDictionariesAreaId);
			area.setObjectDictionariesAreaName(objectDictionariesAreaName);

			String cityId = "" + objectDictionariesCityId;
			// 获得
			areaList = map.get(cityId);
			if (areaList == null) {
				areaList = new ArrayList<ObjectDictionariesArea>();
			}
			areaList.add(area);
			map.put(cityId, areaList);
		}

		return map;
	}

	/**
	 * 获得数据库中的公司名称列表
	 * 
	 * @return
	 */
	public List<ObjectCompany> companies() {
		log.debug("get company dao");
		Session session = null;
		List<?> list = null;
		try {
			session = HSession.getSession();
			Query query = session
					.createSQLQuery("SELECT * FROM object_company");
			list = query.list();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			session.close();
			HSession.closeSession();
		}

		List<ObjectCompany> companies = null;
		if (list != null && !list.isEmpty()) {
			companies = new ArrayList<ObjectCompany>();
			Iterator<?> itr = list.iterator();
			while (itr.hasNext()) {
				Object[] objs = (Object[]) itr.next();
				int objectCompanyId = Integer.parseInt(String.valueOf(objs[0]));
				String objectCompanyName = String.valueOf(objs[1]);

				ObjectCompany company = new ObjectCompany();
				company.setObjectCompanyId(objectCompanyId);
				company.setObjectCompanyName(objectCompanyName);
				companies.add(company);
			}
		}
		return companies;
	}

	/**
	 * 公司筛选字段，根据这些字段将公司名从标题中筛选出来
	 * 
	 * @return
	 */
	public List<String> filterCompanyWords() {
		Session session = null;
		List<?> list = null;
		try {
			session = HSession.getSession();
			Query query = session
					.createSQLQuery("SELECT * FROM object_company_filter");
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			session.close();
			HSession.closeSession();
		}

		List<String> words = null;
		if (list != null && !list.isEmpty()) {
			log.debug("company filter size:" + list.size());
			words = new ArrayList<String>();
			Iterator<?> itr = list.iterator();
			while (itr.hasNext()) {
				Object[] objs = (Object[]) itr.next();
				String filterWords = String.valueOf(objs[1]);
				words.add(filterWords);
			}
		}
		return words;
	}

	/**
	 * 用于保存公司名称，如果公司名称过长或过短则不做处理或者有其他的字段，返回-1, 否则返回新增的的id值
	 * 
	 * @param company
	 * @return
	 */
	public int saveCompanyWord(String company) {
		// 去除company中的空格
		company = company.replaceAll("\\s*", "");
		log.debug("saving company name: " + company);
		int companyId = -1;
		if (companyCanSave(company)) {
			// 去除多余字段
			company = clearField(company);
			Session session = null;
			Transaction t = null;
			try {
				session = HSession.getSession();
				t = session.beginTransaction();
				ObjectCompany oc = new ObjectCompany();
				oc.setObjectCompanyName(company);
				companyId = (Integer) session.save(oc);
				t.commit();
				log.debug("saved company id is: " + companyId);
			} catch (Exception e) {
				log.error(e.getMessage());
				t.rollback();
			} finally {
				session.close();
				HSession.closeSession();
			}
		}
		return companyId;
	}

	/**
	 * 删除公司名中的多余字段
	 * 
	 * @param company
	 * @return
	 */
	private String clearField(String company) {
		ConfFile conf = ConfFile.getInstance();
		String[] delete = conf.getCompanyDelete();
		for (int i = 0; i < delete.length; i++) {
			if (company.contains(delete[i])) {
				return company.substring(
						company.indexOf(delete[i]) + delete[i].length(),
						company.length());
			}
		}
		return company;
	}

	/**
	 * 验证要存储的公司名是否包含垃圾字段
	 * 
	 * @param company
	 * @return
	 */
	private boolean companyCanSave(String company) {
		int comLength = company.length();
		// 公司名称长度在4~15之间
		if (comLength >= 4 && comLength < 25) {
			log.debug("company name length right");
			// 验证公司命中是否有垃圾字段
			ConfFile conf = ConfFile.getInstance();
			String[] illage = conf.getCompanyIllage();
			for (int i = 0; i < illage.length; i++) {
				if (company.contains(illage[i])) {
					log.debug("company name has illage field");
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
