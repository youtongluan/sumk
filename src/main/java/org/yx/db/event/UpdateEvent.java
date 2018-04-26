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
package org.yx.db.event;

import java.util.List;
import java.util.Map;

public class UpdateEvent extends ModifyEvent {

	private final Map<String, Object> to;
	private final Map<String, Number> incrMap;
	private final List<Map<String, Object>> wheres;
	private final boolean fullUpdate;
	private final boolean updateDBID;

	public UpdateEvent(String table, Map<String, Object> to, Map<String, Number> incrMap,
			List<Map<String, Object>> wheres, boolean fullUpdate, boolean updateDBID) {
		super(table);
		this.to = to;
		this.incrMap = incrMap == null || incrMap.isEmpty() ? null : incrMap;
		this.wheres = wheres;
		this.fullUpdate = fullUpdate;
		this.updateDBID = updateDBID;
	}

	public boolean isFullUpdate() {
		return this.fullUpdate;
	}

	public boolean isUpdateDBID() {
		return this.updateDBID;
	}

	public Map<String, Object> getTo() {
		return to;
	}

	public List<Map<String, Object>> getWheres() {
		return wheres;
	}

	public Map<String, Number> getIncrMap() {
		return incrMap;
	}

}
