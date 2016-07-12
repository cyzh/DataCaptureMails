package cn.j.utils;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 配置文件的类
 * 
 * @author WenC
 * 
 */
public class ConfFile {

	private static final Logger log = Logger.getLogger(ConfFile.class);
	private static ConfFile conf = null;
	// 开始时间
	private String startTime = null;
	// 截止时间
	private String endTime = null;

	// 公司名非法字段
	private String[] companyIllage = null;
	// 公司名过滤字段
	private String[] companyDelete = null;

	// 允许抓取的标题包含的字段
	private String[] titleAllow = null;
	// 不合适的标题字段
	private String[] titleDenial = null;

	private ConfFile() {
		try {
			readFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ConfFile getInstance() {
		if (conf == null) {
			conf = new ConfFile();
		}
		return conf;
	}

	private void readFile() throws Exception {
		String path = ConfFile.class.getResource("/").getPath();
		log.debug("config file path: " + path);
		SAXReader reader = new SAXReader();
		Document document = reader.read(path + "datatender.xml");
		Element root = document.getRootElement();
		Element company = root.element("company");
		Element title = root.element("title-type");
		Element time = root.element("run-time");
		readCompany(company);
		readTitle(title);
		readTime(time);
	}

	private void readCompany(Element company) {
		String illegal = company.element("illegal").getTextTrim();
		companyIllage = illegal.split(";");
		String delete = company.element("delete").getTextTrim();
		companyDelete = delete.split(";");
	}

	private void readTitle(Element title) {
		String allow = title.element("allow").getTextTrim();
		titleAllow = allow.split(";");
		String denial = title.element("denial").getTextTrim();
		titleDenial = denial.split(";");
	}

	private void readTime(Element time) {
		startTime = time.attributeValue("start");
		endTime = time.attributeValue("end");
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public String[] getCompanyIllage() {
		return companyIllage;
	}

	public String[] getCompanyDelete() {
		return companyDelete;
	}

	public String[] getTitleAllow() {
		return titleAllow;
	}

	public String[] getTitleDenial() {
		return titleDenial;
	}

}
