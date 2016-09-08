package org.yx.db.dao;

public interface Pagable {
	/**
	 * 
	 * @return 分页查询开始的记录数，开始为0
	 */
	Integer getBeginDATAIndex();
	/**
	 * 每页的条数，不需要分页就设置为-1
	 * @param size
	 */
	void setPageSize(int size);
	/**
	 * 当前页码，其实为1
	 * @param index
	 */
	void setPageIndex(int index);
	/**
	 * 
	 * @return 分页时截止的索引（不包含本索引）
	 */
	int getPageSize();

}
