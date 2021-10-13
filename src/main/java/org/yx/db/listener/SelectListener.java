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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.yx.annotation.Bean;
import org.yx.common.listener.SumkListener;
import org.yx.conf.Const;
import org.yx.db.DBJson;
import org.yx.db.conn.ConnectionPool;
import org.yx.db.enums.CacheType;
import org.yx.db.event.QueryEvent;
import org.yx.db.sql.DBSettings;
import org.yx.db.sql.PojoMeta;
import org.yx.db.visit.RecordRepository;

@Bean
public class SelectListener implements SumkListener {

	@Override
	public Collection<String> acceptType() {
		return Collections.singletonList(Const.LISTENER_DB_QUERY);
	}

	@Override
	public void listen(Object ev) throws Exception {
		if (!(ev instanceof QueryEvent)) {
			return;
		}
		QueryEvent event = (QueryEvent) ev;
		PojoMeta pm = event.getTableMeta();

		if (pm == null || pm.isNoCache() || event.getResult() == null || ConnectionPool.get().existModifyEvent(pm)) {
			return;
		}
		List<Map<String, Object>> in = event.getIn();
		if (in == null) {
			return;
		}
		if (in.size() != 1) {
			if (pm.isPrimeKeySameWithCache() && pm.getDatabaseIds().size() == 1 && pm.cacheType() == CacheType.SINGLE) {
				this.singleIdCache(pm, event.getResult());
			}
			return;
		}

		Map<String, Object> where = in.get(0);
		if (!pm.isOnlyCacheID(where)) {
			return;
		}
		String id = pm.getCacheID(where, false);
		if (id == null) {
			return;
		}

		List<Object> list = new ArrayList<>(4);
		for (Object obj : event.getResult()) {
			if (id.equals(pm.getCacheID(obj, false))) {
				list.add(obj);
			}
		}
		if (list.isEmpty()) {
			return;
		}

		if (pm.cacheType() == CacheType.LIST) {
			RecordRepository.set(pm, id, DBJson.operator().toJson(list));
			return;
		}
		if (list.size() != 1 || list.get(0) == null) {
			return;
		}
		RecordRepository.set(pm, id, DBJson.operator().toJson(list.get(0)));
	}

	private void singleIdCache(PojoMeta pm, List<?> result) throws Exception {
		int size = result.size();
		int max = DBSettings.maxSingleKeyToCache();
		if (size > max) {
			size = max;
			result = new ArrayList<>(result);
			Collections.shuffle(result, ThreadLocalRandom.current());
		}
		for (int i = 0; i < size; i++) {
			Object obj = result.get(i);
			if (obj == null) {
				continue;
			}
			String id = pm.getCacheID(obj, false);
			if (id == null) {
				continue;
			}
			RecordRepository.set(pm, id, DBJson.operator().toJson(obj));
		}

	}
}
