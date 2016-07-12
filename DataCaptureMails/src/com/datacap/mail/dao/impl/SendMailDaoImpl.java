package com.datacap.mail.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.datacap.mail.dao.ISendMailDao;
import com.datacap.mail.utils.JdbcManager;

public class SendMailDaoImpl implements ISendMailDao {
	private static Logger log = Logger.getLogger(SendMailDaoImpl.class);

	@Override
	public boolean newSender(List<Object> params) {
		String sql = "insert into send(source,title,date) values(?,?,?)";
		JdbcManager manager = JdbcManager.getJdbcManager();
		boolean flag = false;
		try {
			flag = manager.upDBData(sql, params);
			log.debug("新增sql: " + sql);
		} catch (SQLException e) {
			log.debug("新增发送记录异常");
			log.error(e.toString());
			e.printStackTrace();
			return false;
		}
		return flag;
	}

	@Override
	public boolean findSender(List<Object> params) {
		String sql = "select count(*) as count from send where source = ? and title = ? and date = ?";
		JdbcManager manager = JdbcManager.getJdbcManager();
		Map<String, Object> map = null;
		try {
			map = manager.getSingleResult(sql, params);
			log.debug("查询sql: " + sql);
		} catch (SQLException e) {
			log.debug("新增发送记录异常");
			log.error(e.toString());
			e.printStackTrace();
			return false;
		}
		if (map == null || map.isEmpty()) {
			log.debug("列表为空");
			return false;
		}
		long count = (long) map.get("count");
		if (count <= 0) {
			log.debug("列表为空");
			return false;
		}
		log.debug("列表不为空: " + map.toString());
		return true;
	}

	@Override
	public boolean deleteSender(List<Object> params) {
		return false;
	}

}
