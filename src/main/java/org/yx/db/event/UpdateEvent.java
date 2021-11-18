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
import java.util.Map.Entry;

import org.yx.annotation.doc.NotNull;
import org.yx.common.sumk.map.ListMap;
import org.yx.db.sql.DBFlag;
import org.yx.db.sql.DBSettings;
import org.yx.util.BitUtil;

public class UpdateEvent extends ModifyEvent {

	private final Map<String, Object> to;
	private final Map<String, Number> incrMap;
	private final List<Map<String, Object>> wheres;

	public UpdateEvent(String table, int flag, Map<String, Object> to, Map<String, Number> incrMap,
			List<Map<String, Object>> wheres) {
		super(table, flag);
		this.incrMap = incrMap == null || incrMap.isEmpty() ? null : incrMap;
		this.wheres = wheres;
		this.to = isFullUpdate() ? to : removeNull(to);
	}

	public boolean isFullUpdate() {
		return BitUtil.getBit(flag, DBFlag.UPDATE_FULL_UPDATE);
	}

	public boolean isUpdateDBID() {
		return BitUtil.getBit(flag, DBFlag.UPDATE_UPDATE_DBID);
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

	public boolean canUpdateCache() {
		return wheres.size() == 1 && getAffected() == 1 && incrMap == null && isFullUpdate()
				&& BitUtil.getBit(DBSettings.flag(), DBFlag.UPDATE_TO_CACHE);
	}

	private <K, V> Map<K, V> removeNull(@NotNull Map<K, V> map) {
		Map<K, V> ret = new ListMap<>();
		for (Entry<K, V> entry : map.entrySet()) {
			K k = entry.getKey();
			V v = entry.getValue();
			if (k == null || v == null) {
				continue;
			}
			ret.put(k, v);
		}
		return ret;
	}
}
