/**
 * Copyright (C) 2016 - 2030 youtongluan.
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

import java.util.List;

/**
 * 用来存放结果集以及总记录数
 */
public class CountedResult<T> {
	private List<T> list;
	private long count;

	public CountedResult(List<T> list, long count) {
		this.list = list;
		this.count = count;
	}

	/**
	 * @return 当前页的结果集
	 */
	public List<T> getResult() {
		return list;
	}

	/**
	 * @return 符合条件的总记录数
	 */
	public long getCount() {
		return count;
	}

}
