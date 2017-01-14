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
package org.yx.db.event;

import java.util.Map;

public class InsertEvent extends ModifyEvent {

	private Map<String, Object> pojo;

	/**
	 * 
	 * @param table
	 * @param pojo
	 * @param idMap
	 *            主键列表，包含所有的主键，即使它是null
	 */
	public InsertEvent(String table, Map<String, Object> pojo) {
		super(table);
		this.pojo = pojo;
	}

	public Map<String, Object> getPojo() {
		return pojo;
	}

}
