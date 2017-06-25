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

/**
 * 用来存放结果集以及总记录数
 */
public class CountedResult<T> {
	private T result;
	private long count;

	public CountedResult(T result, long count) {
		this.result = result;
		this.count = count;
	}

	/**
	 * 当前页的结果集
	 * 
	 * @return
	 */
	public T getResult() {
		return result;
	}

	/**
	 * 符合条件的总记录数
	 * 
	 * @return
	 */
	public long getCount() {
		return count;
	}

}
