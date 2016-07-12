package com.datacap.mail.utils;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Constant {
	private final static Logger log = Logger.getLogger(Constant.class);
	private final String PATH = Constant.class.getResource("/").getPath();
	private static Constant constant;
	private String[] mailFilter = null;

	private Constant() {
		lodeFilter();
	}

	public static Constant getInstance() {
		if (constant == null) {
			constant = new Constant();
		}
		return constant;
	}

	public String[] getMailFilter() {
		return this.mailFilter;
	}

	private void lodeFilter() {
		// { "必选", "入围", "入库", "招标代理" };
		SAXBuilder builder = new SAXBuilder();
		Document document = null;
		try {
			document = builder.build(PATH + "keywords.xml");
		} catch (JDOMException e) {
			e.printStackTrace();
			log.debug("keywords.xml解析异常");
			log.error(e);
			return;
		} catch (IOException e) {
			e.printStackTrace();
			log.debug("keywords.xml读取异常");
			log.error(e);
		}
		if (document != null) {
			Element keywords = document.getRootElement();
			List<Element> keywordList = keywords.getChildren("keyword");
			mailFilter = new String[keywordList.size()];
			for (int i = 0; i < keywordList.size(); i++) {
				mailFilter[i] = keywordList.get(i).getText();
			}
		}
	}
}
