package cn.j.core.list.filter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import cn.j.core.entity.DataInfo;
import cn.j.data.dao.ObjectCompany;
import cn.j.data.dao.ObjectDictionariesArea;
import cn.j.data.session.HSession;

/**
 * 对单个列表进行操作
 * 
 * @author WenC
 * 
 */
public class Filter {

	private static final Logger log = Logger.getLogger(Filter.class);
	private Map<String, List<String>> cities = null;
	private Map<String, List<ObjectDictionariesArea>> areasMap = null;
	private List<String> filterCompanyWords = null;
	private List<ObjectCompany> companies = null;
	private FilterWords fw = null;
	// 常量
	private static final String COMPANY_ID = "companyId";
	private static final String COMPANY_NAME = "companyName";
	private static final String TITLE = "title";
	private static final String OTHER = "-1";// -1表示不符合任何情况

	public Filter() {
		fw = new FilterWords();
		// 公司过滤字段用于筛选除公司名
		filterCompanyWords = fw.filterCompanyWords();
		// 公司列表
		companies = fw.companies();
		// 城市列表
		cities = fw.citys();
		// 城市包含的地区列表
		areasMap = fw.cityAreas();
	}

	/**
	 * 处理标题
	 * 
	 * @param list
	 * @return
	 */
	public Map<String, Map<String, List<DataInfo>>> classifyTitle(
			List<DataInfo> list) {
		Map<String, List<DataInfo>> cityInfos = clearCity(list);
		Map<String, Map<String, List<DataInfo>>> areaInfos = clearCompany(cityInfos);
		return areaInfos;
	}

	/**
	 * 2. 去除标题中的城市(第一次去除关键字)，返回每个城市发布的信息标题列表集合。遍历方式：1)获得已分类的城市map；2)循环要分类的列表；3)
	 * 依次获得分类的城市列表；4)对获得的城市列表进行遍历，如果符合则按照已经分好的城市id进行分类，如果没有匹配当前的城市id则对其包含的地区
	 * 进行遍历：若地区匹配到，则获得地区id并与城市id组合（城市id-地区id），如果不符合则分为其他类。
	 * 
	 * @param list
	 * @return
	 */
	private Map<String, List<DataInfo>> clearCity(List<DataInfo> list) {
		Map<String, List<DataInfo>> map = null;
		// 缓存list，用于存放不符合条件的对象
		List<DataInfo> tempList = null;

		// 过滤条件中城市map的键名集合
		Set<String> cityKeySet = cities.keySet();

		// 依次过滤
		for (int i = 0; i < list.size(); i++) {
			DataInfo datalist = list.get(i);
			// 需要筛选的标题
			String title = datalist.getTitle();
			datalist.setFullTitle(title);
			// 去除title中的空格
			title = title.replaceAll("\\s*", "");

			// 标识是否有匹配城市在标题中
			boolean hasCity = false;

			// map的键名迭代器
			Iterator<String> iterator = cityKeySet.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				// 是否可以停止遍历
				boolean canStop = false;
				// 获得当前键值对应的城市列表
				List<String> cityList = cities.get(key);
				// 依次过滤
				for (int j = 0; j < cityList.size(); j++) {
					String cityName = cityList.get(j);
					// 如果标题中包含城市名称，则以城市名称为准
					if (title.contains(cityName)) {
						log.debug(title + " contains " + cityName);
						// 遍历完成
						canStop = true;
						// 标题符合标准
						hasCity = true;
						datalist.setTitle(cutStr(cityName, title));
						datalist.setCity(cityName);
						datalist.setCityId(Integer.parseInt(key));
						// 对符合要求的内容进行归类
						map = classifyByCity(map, key, datalist);
						break;
					}
					// 如果遍历完城市名列表还没有匹配到，则要遍历城市对应的区域列表
					if (j == cityList.size() - 1) {
						log.debug("city name was not matched, matching area list of city: "
								+ cityName);
						// 如果标题中不包含城市名称，则要遍历城市对应的地区列表，判断是否包含
						List<ObjectDictionariesArea> areas = areasMap.get(key);
						for (int k = 0; k < areas.size(); k++) {
							String areaName = areas.get(k)
									.getObjectDictionariesAreaName();
							int areaId = areas.get(k)
									.getObjectDictionariesAreaId();
							if (title.contains(areaName)) {
								log.debug(title + " contains " + areaName);
								// 遍历完成
								canStop = true;
								// 标题符合标准
								hasCity = true;
								datalist.setTitle(cutStr(areaName, title));
								datalist.setCity(cityName);
								datalist.setCityId(Integer.parseInt(key));
								datalist.setArea(areaName);
								datalist.setAreaId(areaId);
								map = classifyByCity(map, key + "-" + areaId,
										datalist);
								break;
							}
						}
					}
				}
				// 可以停止遍历，跳出循环
				if (canStop) {
					log.debug("loop over");
					break;
				}
			}
			// 判断是否加入缓存列表
			if (!hasCity) {
				if (tempList == null) {
					tempList = new ArrayList<DataInfo>();
				}
				tempList.add(datalist);
				log.debug("add datalist to temp list");
			}
		}
		if (map == null) {
			map = new HashMap<String, List<DataInfo>>();
		}
		// 将不包含任何城市的内容列表加入map
		map.put(OTHER, tempList);
		return map;
	}

	/**
	 * 去除字符串中包含的字段
	 * 
	 * @param contains
	 *            包含的字段
	 * @param target
	 *            目标字符串
	 * @return
	 */
	private String cutStr(String contains, String target) {
		log.debug("title contains: " + contains + ", title is: " + target);
		int containsIndex = contains.indexOf(contains);
		String title = target.substring(containsIndex, target.length());
		return title;
	}

	/**
	 * 对标题信息进行归类处理，通过城市键值获得对应的信息列表，如果该城市的键值不存在，则新建；如果存在，则直接新增信息
	 * 
	 * @param map
	 * @param key
	 * @param data
	 * @return
	 */
	private Map<String, List<DataInfo>> classifyByCity(
			Map<String, List<DataInfo>> map, String key, DataInfo data) {
		// 如果map的值为空，则新建一个map
		if (map == null) {
			map = new HashMap<String, List<DataInfo>>();
		}
		// 获得map键值对应的列表，如果列表为空，则新增
		List<DataInfo> list = map.get(key);
		// 如果该键值对应的城市列表为空
		if (list == null) {
			log.debug("Map list is not contains key: " + key + ", created");
			list = new ArrayList<DataInfo>();
			list.add(data);
			map.put(key, list);
		} else {
			log.debug("Map list contains key: " + key + ", replaced");
			list.add(data);
			map.put(key, list);
		}
		return map;
	}

	/**
	 * <pre>
	 * 3. 去除标题中的公司名称。
	 *  1) 依次获取已经去除城市的列表
	 *  2) 在获取城市列表之后，对其进行遍历
	 *  3) 获取公司列表
	 *  4) 循环过滤
	 *     1> 如果公司列表为空，则根据字段列表将标题中的公司名称截取出来并判断符合规则后保存到数据库，根据保存到数据库的id值创建新列表保存内容。
	 *     2> 如果公司列表不为空，则依次比对公司列表
	 *       1> 如有包含的公司名存在，则根据获取的公司的id得到该公司的列表并将内容保存在列表中
	 *       2> 若公司名称不存在，就将公司名接取出来并且判断符合规则后保存到数据库，根据保存到数据库的id值创建新列表保存内容。
	 *  5) 最后的结果格式为三级目录格式，用 Map 表示
	 * </pre>
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Map<String, List<DataInfo>>> clearCompany(
			Map<String, List<DataInfo>> map) {
		Map<String, Map<String, List<DataInfo>>> classifiedMap = null;
		// 获得按城市分组map的键名
		Set<String> keySet1 = map.keySet();
		// 获得城市id名迭代器
		Iterator<String> cityKeys = keySet1.iterator();
		// 对列表依次处理
		while (cityKeys.hasNext()) {
			// 地域分组的键名
			String citykey = cityKeys.next();
			List<DataInfo> datalist = map.get(citykey);
			if (datalist == null) {
				log.debug("datalist is null");
				continue;
			}
			// 缓存按公司id分类后的内容
			Map<String, List<DataInfo>> tempDataMap = null;
			// 缓存公司id对应的列表
			List<DataInfo> tempDatalist = null;
			// 城市对应的列表
			for (int i = 0; i < datalist.size(); i++) {
				DataInfo data = datalist.get(i);
				String title = data.getTitle();
				log.debug("company title is: " + title);
				// 对城市
				Map<String, String> classifyMap = classifyCompany(title);
				// 分类后的公司id值
				String companyId = classifyMap.get(COMPANY_ID);
				// 分类后的公司名
				String companyTitle = classifyMap.get(TITLE);
				data.setTitle(companyTitle);
				data.setCompany(classifyMap.get(COMPANY_NAME));
				data.setCompanyId(Integer.parseInt(companyId));
				// 实例化缓存map
				if (tempDataMap == null) {
					tempDataMap = new HashMap<String, List<DataInfo>>();
				}
				// 判断标题是否有公司名
				if ("-1".equals(companyId)) {
					log.debug("companyId is -1");
					tempDatalist = tempDataMap.get(OTHER);
					// 如果列表是空的话实例化新的列表
					if (tempDatalist == null) {
						log.debug("create tempDataList");
						tempDatalist = new ArrayList<DataInfo>();
					}
					// 将内容添加到缓存队列
					tempDatalist.add(data);
					// 将更新的列表加到缓存map中
					tempDataMap.put(OTHER, tempDatalist);
				} else {
					// 公司id对应的列表
					tempDatalist = tempDataMap.get(companyId);
					if (tempDatalist == null) {
						log.debug("create tempDataList");
						tempDatalist = new ArrayList<DataInfo>();
					}
					// 将内容添加到缓存队列
					tempDatalist.add(data);
					// 将更新的列表加到缓存map中
					tempDataMap.put(companyId, tempDatalist);
				}
			}
			// 如果不存在则实例化返回结果map
			if (classifiedMap == null) {
				log.debug("create classifiedMap");
				classifiedMap = new HashMap<String, Map<String, List<DataInfo>>>();
			}
			classifiedMap.put(citykey, tempDataMap);
		}
		return classifiedMap;
	}

	/**
	 * 对标题进行公司分类
	 * 
	 * @param title
	 */
	private Map<String, String> classifyCompany(String title) {
		Map<String, String> map = null;
		// 如果companies为空，则需要将标题中的公司截取出来判断并保存；如果不为空，则遍历companies列表是否有匹配的公司，
		// 如果没有则仍然需要将公司名截取出来并保存, 有新列表保存后更新companies列表
		if (companies == null || companies.isEmpty()) {
			log.debug("companies is null");
			map = saveCompany(title);
		} else {
			log.debug("companies is not null");
			// 遍历companies列表
			int companiesLength = companies.size();
			boolean hasCompany = false;
			for (int i = 0; i < companiesLength; i++) {
				// 公司名
				String company = companies.get(i).getObjectCompanyName();
				// 公司id
				String companyId = "" + companies.get(i).getObjectCompanyId();
				// 判断标题是否包含公司名称
				if (title.contains(company)) {
					log.debug(title + " contains company: " + company);
					// 获得标题的下标
					int companyIndex = title.indexOf(company);
					// 获得完整的公司名称
					String companyName = title.substring(0, companyIndex
							+ company.length());
					// 截取标题
					title = title.substring(companyIndex + company.length(),
							title.length());
					log.debug("cleared company title: " + title);
					hasCompany = true;
					// 返回结果
					map = new HashMap<String, String>();
					map.put(COMPANY_ID, companyId);
					map.put(COMPANY_NAME, companyName);
					map.put(TITLE, title);
					log.debug("Company map: " + map.toString());
					break;
				}
			}
			// 没有匹配到公司
			if (!hasCompany) {
				log.debug("there is no title matched in the company list");
				map = saveCompany(title);
			}
		}
		return map;
	}

	/**
	 * 返回保存的公司的id值，如果保存失败则返回-1
	 * 
	 * @param title
	 * @return
	 */
	private Map<String, String> saveCompany(String title) {
		Map<String, String> map = new HashMap<String, String>();
		boolean hasFilterWord = false;
		// 遍历公司关键字列表，截取出公司名称
		for (int i = 0; i < filterCompanyWords.size(); i++) {
			String companyKeyword = filterCompanyWords.get(i);
			if (title.contains(companyKeyword)) {
				log.debug(title + " conatins companyKeyword: " + companyKeyword);
				// 匹配的公司关键字下标
				int ckwIndex = title.indexOf(companyKeyword);
				// 获得完整的公司名称
				String company = title.substring(0,
						ckwIndex + companyKeyword.length());

				// 判断公司名是否已经保存
				if (hasCompany(company)) {
					log.debug("has company");
					continue;
				}

				log.debug("clear company name: " + company);
				// 保存标题
				int flag = fw.saveCompanyWord(company);
				// 保存失败
				if (flag == -1) {
					log.debug("company saved failed");
					map.put(COMPANY_ID, "-1");
				} else {
					log.debug("company saved successed");
					// 更新companies列表
					companies = fw.companies();
					// 清除公司名称
					int companyIndex = title.indexOf(company);
					title = title.substring(companyIndex + company.length(),
							title.length());
					map.put(COMPANY_ID, "" + flag);
				}
				map.put(TITLE, title);
				map.put(COMPANY_NAME, company);
				hasFilterWord = true;
				break;
			}
		}
		// 如果最后任然没有匹配
		if (!hasFilterWord) {
			log.debug("There is no filter word matched");
			map.put(COMPANY_ID, "-1");
			map.put(TITLE, title);
		}
		return map;
	}

	private boolean hasCompany(String company) {
		Session session = null;
		try {
			session = HSession.getSession();
			Query query = session
					.createSQLQuery("SELECT COUNT(*) FROM object_company WHERE object_company_name=?");
			query.setString(0, company);
			int size = ((BigInteger) query.uniqueResult()).intValue();
			if (size > 0) {
				return true;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			session.close();
			HSession.closeSession();
		}
		return false;
	}

}
