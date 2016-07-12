package cn.j.core.list.task;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.j.constant.ReqConstant;
import cn.j.core.entity.DataInfo;
import cn.j.core.list.GJZBList;
import cn.j.core.list.NMZTBList;
import cn.j.core.list.ZGCGList;
import cn.j.core.list.ZZQCGList;
import cn.j.core.list.filter.Filter;
import cn.j.core.list.filter.FilterList;

public class ListTask {

	private static final Logger log = Logger.getLogger(ListTask.class);
	// 中国采购网list
	private List<DataInfo> zgcgList = null;
	// 国际招标list
	private List<DataInfo> gjzbList = null;
	// 自治区采购网list
	private List<DataInfo> zzqcgList = null;
	// 内蒙古招投list
	private List<DataInfo> nmztbList = null;

	public ListTask() {

	}

	public List<DataInfo> listFilter() throws Exception {
		allList();
		return filter(nmztbList, gjzbList, zzqcgList, zgcgList);
	}

	/**
	 * 获得全部当天列表，优先抓取“内蒙古招投标网”
	 */
	private void allList() {
		zgcgList = new ZGCGList().getPageList();
		gjzbList = new GJZBList().getPageList();
		nmztbList = new NMZTBList().getPageList();
		zzqcgList = new ZZQCGList().getPageList();
	}

	private List<DataInfo> filter(List<DataInfo> nmztbList,
			List<DataInfo> gjzbList, List<DataInfo> zzqcgList,
			List<DataInfo> zgcgList) throws Exception {
		Filter filter = new Filter();
		Map<String, Map<String, List<DataInfo>>> zgcgMap = null;
		Map<String, Map<String, List<DataInfo>>> gjzbMap = null;
		Map<String, Map<String, List<DataInfo>>> zzqcgMap = null;
		Map<String, Map<String, List<DataInfo>>> nmztbMap = null;
		if (nmztbList != null) {
			log.debug(ReqConstant.NMZTB + " is not null, classifyTite");
			nmztbMap = filter.classifyTitle(nmztbList);
		}
		if (gjzbList != null) {
			log.debug(ReqConstant.GJZB + " is not null, classifyTite");
			gjzbMap = filter.classifyTitle(gjzbList);
		}
		if (zzqcgList != null) {
			log.debug(ReqConstant.ZZQCG + " is not null, classifyTite");
			zzqcgMap = filter.classifyTitle(zzqcgList);
		}
		if (zgcgList != null) {
			log.debug(ReqConstant.ZGCG + " is not null, classifyTite");
			zgcgMap = filter.classifyTitle(zgcgList);
		}
		FilterList fl = new FilterList();
		return fl.finalList(zgcgMap, gjzbMap, zzqcgMap, nmztbMap);
	}

}
