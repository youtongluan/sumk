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

public interface Pagable {
	/**
	 * 
	 * @return 分页查询开始的记录数，开始为0
	 */
	Integer getBeginDATAIndex();

	/**
	 * 每页的条数，不需要分页就设置为-1
	 * 
	 * @param size
	 */
	void setPageSize(int size);

	/**
	 * 当前页码，其实为1
	 * 
	 * @param index
	 */
	void setPageIndex(int index);

	/**
	 * 
	 * @return 分页时截止的索引（不包含本索引）
	 */
	int getPageSize();

}
