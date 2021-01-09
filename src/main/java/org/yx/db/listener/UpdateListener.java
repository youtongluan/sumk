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
package org.yx.db.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.annotation.Bean;
import org.yx.db.DBJson;
import org.yx.db.enums.CacheType;
import org.yx.db.event.UpdateEvent;
import org.yx.db.sql.ColumnMeta;
import org.yx.db.sql.DBSettings;
import org.yx.db.sql.PojoMeta;
import org.yx.db.sql.PojoMetaHolder;
import org.yx.listener.SumkEvent;
import org.yx.log.Log;
import org.yx.redis.RecordRepository;

@Bean
public class UpdateListener implements DBEventListener {

	@Override
	public void listen(SumkEvent ev) {
		if (!DBSettings.toCache() || !(ev instanceof UpdateEvent)) {
			return;
		}
		UpdateEvent event = UpdateEvent.class.cast(ev);
		try {
			PojoMeta pm = PojoMetaHolder.getTableMeta(event.getTable());
			if (pm == null || pm.isNoCache()) {
				return;
			}
			List<Map<String, Object>> wheres = event.getWheres();
			for (Map<String, Object> where : wheres) {

				handleUpdate(event, pm, where);
			}

		} catch (Exception e) {
			Log.printStack("sumk.db.listener", e);
		}
	}

	private void handleUpdate(UpdateEvent event, PojoMeta pm, Map<String, Object> where) throws Exception {
		String id = pm.getCacheID(where, true);
		Map<String, Object> to = new HashMap<>(event.getTo());
		if (!event.isUpdateDBID()) {
			List<ColumnMeta> m_ids = pm.getDatabaseIds();
			if (m_ids != null && m_ids.size() > 0) {
				for (ColumnMeta m : m_ids) {
					String name = m.getFieldName();
					Object v = where.get(name);
					if (v != null) {
						to.put(name, v);
					} else {
						to.remove(name);
					}
				}
			}
		}
		if (event.isFullUpdate()) {
			String id_new = pm.getCacheID(to, true);
			if (!id.equals(id_new)) {
				RecordRepository.del(pm, id);
			}
			if (pm.cacheType() == CacheType.LIST || event.getIncrMap() != null) {
				RecordRepository.del(pm, id_new);
			} else {
				RecordRepository.set(pm, id_new, DBJson.operator().toJson(to));
			}
			return;
		}

		RecordRepository.del(pm, id);

		Map<String, Object> where2 = new HashMap<>(where);
		where2.putAll(to);
		String id_new = pm.getCacheID(where2, true);

		if (id.equals(id_new)) {
			return;
		}
		RecordRepository.del(pm, id_new);
	}

}
