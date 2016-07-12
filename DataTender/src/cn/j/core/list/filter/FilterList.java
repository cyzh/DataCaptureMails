package cn.j.core.list.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.j.core.entity.DataInfo;

/**
 * 对全部四个列表进行过滤筛选
 * 
 * @author WenC
 * 
 */
public class FilterList {

	private static final Logger log = Logger.getLogger(FilterList.class);

	// 最后的列表
	public List<DataInfo> finalList(
			Map<String, Map<String, List<DataInfo>>> zgcgList,
			Map<String, Map<String, List<DataInfo>>> gjzbList,
			Map<String, Map<String, List<DataInfo>>> zzqcgList,
			Map<String, Map<String, List<DataInfo>>> nmztbList) {

		// 循环去除列表中的重复项
		if (zgcgList != null) {
			log.debug("integrating zgcglist....");
			zgcgList = filterList(zgcgList, gjzbList);
			zgcgList = filterList(zgcgList, zzqcgList);
			zgcgList = filterList(zgcgList, nmztbList);
		}

		if (gjzbList != null) {
			log.debug("integrating gjzbList....");
			gjzbList = filterList(gjzbList, zzqcgList);
			gjzbList = filterList(gjzbList, nmztbList);
		}

		if (zzqcgList != null) {
			log.debug("integrating zzqcgList....");
			zzqcgList = filterList(zzqcgList, nmztbList);
		}

		// 整合列表
		return integrationList(zgcgList, gjzbList, zzqcgList, nmztbList);
	}

	/**
	 * 整合列表
	 * 
	 * @param zgcgMap
	 * @param gjzbMap
	 * @param zzqcgMap
	 * @param nmztbMap
	 * @return
	 */
	private List<DataInfo> integrationList(
			Map<String, Map<String, List<DataInfo>>> zgcgMap,
			Map<String, Map<String, List<DataInfo>>> gjzbMap,
			Map<String, Map<String, List<DataInfo>>> zzqcgMap,
			Map<String, Map<String, List<DataInfo>>> nmztbMap) {
		List<DataInfo> infoList = mergerMap(zgcgMap);
		infoList.addAll(mergerMap(gjzbMap));
		infoList.addAll(mergerMap(zzqcgMap));
		infoList.addAll(mergerMap(nmztbMap));
		log.debug("integration list......");
		return infoList;
	}

	/**
	 * <pre>
	 *   合并两个map：
	 *   1) 获得两个map的城市键值集合，将其合并
	 *   2) 遍历已合并的键值集合，获得对应的区域id集合
	 * </pre>
	 * 
	 * @param map1
	 * @param map2
	 * @return
	 */
	private List<DataInfo> mergerMap(
			Map<String, Map<String, List<DataInfo>>> map) {
		if (map == null) {
			return new ArrayList<DataInfo>();
		}
		List<DataInfo> list = null;
		// 获得map1的城市键值
		Set<String> cityKeysSet = map.keySet();
		Iterator<String> cityKeyIterator = cityKeysSet.iterator();
		while (cityKeyIterator.hasNext()) {
			String cityKey = cityKeyIterator.next();
			Map<String, List<DataInfo>> areaMap = map.get(cityKey);
			// 获得map的area键值集合
			Set<String> areaKeySet = areaMap.keySet();
			Iterator<String> areaKeyIterator = areaKeySet.iterator();
			while (areaKeyIterator.hasNext()) {
				String areaKey = areaKeyIterator.next();
				List<DataInfo> infoList = areaMap.get(areaKey);
				if (list == null) {
					log.debug("list is null, created");
					list = infoList;
				} else {
					log.debug("list is not null, add");
					list.addAll(infoList);
				}
			}
		}
		return list;
	}

	/**
	 * <pre>
	 * 筛选过滤列表，取target中的每一项匹配reference对应的列表，如果有重复的列表项，则删除如果遍历完后没有重复的则保留。
	 *   1) 获得目标数据对比map的城市id键值集合
	 *   2) 根据目标map的键值结合进行遍历
	 *   3) 获得与目标map的城市id做对比的map集合:
	 *      1) 如果为空，说明不需要做进一步比对，跳过
	 *      2) 如果不为空则要做进一步比对:
	 *         1) 获得目标城市id对应的companyId的集合
	 *         2) 根据城市id集合获取做对比的城市列表：
	 *            1) 如果为空，说明不需要做进一步比对，跳过
	 *            2) 如果不为空则依次比对列表名称
	 *               1) 如果标题相同，或一个包含一个，说明是同一标题，则将目标列表中的内容删除
	 * </pre>
	 * 
	 * @param target
	 *            要进行筛选过滤的列表
	 * @param reference
	 *            参考列表
	 * @return
	 */
	private Map<String, Map<String, List<DataInfo>>> filterList(
			Map<String, Map<String, List<DataInfo>>> target,
			Map<String, Map<String, List<DataInfo>>> reference) {

		if (target == null) {
			return null;
		}
		// 获得列表的键名集合
		Set<String> targetKeys = target.keySet();
		// 获得列表的键名迭代器
		Iterator<String> keyIterator = targetKeys.iterator();
		// 循环遍历
		while (keyIterator.hasNext()) {
			// targetMap的城市id键值
			String cityKey = keyIterator.next();
			if (reference == null) {
				log.debug("reference map is null, shut down");
				break;
			}
			// 得到要对比的城市id对应的map
			Map<String, List<DataInfo>> referenceMap = reference.get(cityKey);
			// 如果要对比的map中没有该城市的id值，说明没有重复，则直接跳过
			if (referenceMap == null) {
				log.debug("reference map was not contains cityKey: " + cityKey
						+ ", skip!");
				continue;
			}
			Map<String, List<DataInfo>> targetCompanyMap = target.get(cityKey);
			// 公司名键值集合
			Set<String> companyKeys = targetCompanyMap.keySet();
			// 公司名键值迭代器
			Iterator<String> companyKeyIterator = companyKeys.iterator();

			// 解析companyMap
			while (companyKeyIterator.hasNext()) {
				String companyKey = companyKeyIterator.next();
				List<DataInfo> referenceList = referenceMap.get(companyKey);
				// 如果company对应的列表得空，说明没有重复，直接跳过
				if (referenceList == null) {
					log.debug("reference company list was not contains companyKey: "
							+ companyKey + ", skip!");
					continue;
				}
				List<DataInfo> targetList = targetCompanyMap.get(companyKey);
				targetList = filterDataList(targetList, referenceList);
				log.debug("replace companyId is :" + companyKey + "'s list");
				if (targetList.isEmpty()) {
					log.debug("targetCompanyMap is empty, remove companyId: "
							+ companyKey);
					targetCompanyMap.remove(companyKey);
					companyKeys = targetCompanyMap.keySet();
					companyKeyIterator = companyKeys.iterator();
				} else {
					// 替换之前的列表
					targetCompanyMap.put(companyKey, targetList);
				}
			}
			if (targetCompanyMap.isEmpty()) {
				log.debug("target is empty, remove cityId: " + cityKey);
				target.remove(cityKey);
				targetKeys = target.keySet();
				keyIterator = targetKeys.iterator();
			} else {
				log.debug("replace cityId is :" + cityKey + "'s map");
				target.put(cityKey, targetCompanyMap);
			}
		}
		return target;
	}

	/**
	 * 过滤列表，如果有相同或者之间存在包含关系，则将目标列表中的内容删除
	 * 
	 * @param target
	 * @param reference
	 * @return
	 */
	private List<DataInfo> filterDataList(List<DataInfo> target,
			List<DataInfo> reference) {

		for (int i = 0; i < target.size(); i++) {
			DataInfo tarData = target.get(i);
			// 获得做比对的标题
			String tarTitle = tarData.getTitle();
			// 依次遍历reference中的列表
			for (int j = 0; j < reference.size(); j++) {
				DataInfo refData = reference.get(j);
				String refTitle = refData.getTitle();
				// 如果相同
				if (tarTitle.equals(refTitle)) {
					log.debug(tarTitle + " equals " + refTitle);
					target.remove(i);
					i--;
					break;
				} else if (tarTitle.contains(refTitle)) {
					log.debug(tarTitle + " contains " + refTitle);
					target.remove(i);
					i--;
					break;
				} else if (refTitle.contains(tarTitle)) {
					log.debug(refTitle + " contains " + tarTitle);
					target.remove(i);
					i--;
					break;
				} else {
					log.debug(tarTitle + " not equal or contains " + refTitle);
				}
			}
		}
		return target;
	}
}
