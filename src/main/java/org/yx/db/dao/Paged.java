/**
 * Copyright (C) 2016 - 2017 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
