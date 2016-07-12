package com.datacap.mail.dao;

import java.util.List;

public interface ISendMailDao {

	/**
	 * 新增记录
	 * 
	 * @param params
	 * @return
	 */
	public boolean newSender(List<Object> params);

	/**
	 * 查询记录
	 * 
	 * @param params
	 * @return
	 */
	public boolean findSender(List<Object> params);

	/**
	 * 删除记录
	 * 
	 * @param params
	 * @return
	 */
	public boolean deleteSender(List<Object> params);

}
