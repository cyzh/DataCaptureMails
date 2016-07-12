package cn.j.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

public class TableConfig {

	private static final Logger log = Logger.getLogger(TableConfig.class);
	private static TableConfig config = null;
	private String tableName = null;

	private TableConfig() {
		log.debug("read hibernate file");
	}

	public static TableConfig getInstance() {
		if (config == null) {
			config = new TableConfig();
		}
		return config;
	}

	public void doTableConfig() {
		String path = TableConfig.class.getClassLoader().getResource("/")
				.getPath()
				+ "cn/j/data/";
		System.out.println("path: " + path);
		String currentYear = DateTime.currentDate().split("-")[0];
		tableName = "datacapture_" + currentYear;
		try {
			SAXBuilder builder = new SAXBuilder();
			// 关闭dtd验证
			builder.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-external-dtd",
					false);
			Document document = builder.build(new File(path
					+ "dao/Datacapture.hbm.xml"));

			Element root = document.getRootElement();
			Element classroot = root.getChild("class");
			log.debug("get classroot");
			classroot.setAttribute("table", tableName);
			log.debug("set config table params: " + tableName);
			XMLOutputter out = new XMLOutputter();
			out.output(document, new FileOutputStream(path
					+ "dao/Datacapture.hbm.xml"));
			log.debug("write config file");
		} catch (FileNotFoundException e) {
			System.out.println("---->>1");
			e.printStackTrace();
		} catch (JDOMException e) {
			System.out.println("---->>2");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("---->>3");
			e.printStackTrace();
		}
		// 建表
		createTable();
	}

	/**
	 * 建表
	 */
	private void createTable() {
		String sql = "SHOW TABLES LIKE '" + tableName + "'";
		JdbcManager manager = JdbcManager.getJdbcManager();
		Map<String, Object> tablename = null;
		try {
			tablename = manager.getSingleResult(sql, null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 如果表为空，则需要新建一张表
		if (tablename == null || tablename.isEmpty()) {
			log.debug("表不存在，创建表");
			sql = "CREATE TABLE `" + tableName + "` ("
					+ "`id` int(11) unsigned NOT NULL AUTO_INCREMENT,"
					+ "`source` varchar(2000) DEFAULT NULL,"
					+ "`area` varchar(2000) DEFAULT NULL,"
					+ "`industry` varchar(2000) DEFAULT NULL,"
					+ "`publishdate` date DEFAULT NULL,"
					+ "`startdate` date DEFAULT NULL,"
					+ "`enddate` date DEFAULT NULL,"
					+ "`tenderee` varchar(2000) DEFAULT NULL,"
					+ "`capital` varchar(2000) DEFAULT NULL,"
					+ "`competitor` varchar(2000) DEFAULT NULL,"
					+ "`pageurl` varchar(2000) DEFAULT NULL,"
					+ "`title` varchar(2000) DEFAULT NULL,"
					+ "`province_id` int(11) DEFAULT NULL,"
					+ "`city_id` int(11) DEFAULT NULL,"
					+ "`area_id` int(11) DEFAULT NULL," + "`content` longtext,"
					+ "`is_download` int(11) DEFAULT 0,"
					+ "PRIMARY KEY (`id`),"
					+ "KEY `titleIndex` (`title`(255)) USING BTREE"
					+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT";
			try {
				manager.upDBData(sql, null);
				log.debug("create table sql: " + sql);
			} catch (SQLException e) {
				log.debug("表不存在，创建表异常");
				log.error(e.toString());
				e.printStackTrace();
			}
		}
	}

	public String getTableName() {
		return tableName;
	}

}
