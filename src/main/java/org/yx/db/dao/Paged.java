package org.yx.db.dao;

public class Paged implements Pagable {

	private int pageIndex = 1;
	private int pageSize = 50;

	@Override
	public Integer getBeginDATAIndex() {
		if (pageSize < 1) {
			return null;
		}
		return (pageIndex - 1) * pageSize;
	}

	@Override
	public void setPageSize(int size) {
		this.pageSize = size;

	}

	@Override
	public void setPageIndex(int index) {
		this.pageIndex = index;
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	public Paged(int pageSize) {
		this.pageSize = pageSize;
	}

	public Paged() {
	}

}
