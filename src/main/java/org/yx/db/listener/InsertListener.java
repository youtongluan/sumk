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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.yx.annotation.Bean;
import org.yx.common.listener.SumkListener;
import org.yx.conf.Const;
import org.yx.db.DBJson;
import org.yx.db.enums.CacheType;
import org.yx.db.event.InsertEvent;
import org.yx.db.sql.DBSettings;
import org.yx.db.sql.PojoMeta;
import org.yx.db.visit.RecordRepository;

@Bean
public class InsertListener implements SumkListener {

	@Override
	public int order() {
		return 90;
	}

	@Override
	public Collection<String> acceptType() {
		return Collections.singletonList(Const.LISTENER_DB_MODIFY);
	}

	@Override
	public void listen(Object ev) throws Exception {
		if (!DBSettings.toCache() || !(ev instanceof InsertEvent)) {
			return;
		}
		InsertEvent event = (InsertEvent) ev;
		PojoMeta pm = event.getTableMeta();
		List<Map<String, Object>> list = event.getPojos();
		if (pm == null || pm.isNoCache() || list == null) {
			return;
		}
		for (Map<String, Object> map : list) {
			String id = pm.getCacheID(map, false);
			if (id == null) {
				continue;
			}
			if (pm.cacheType() == CacheType.LIST) {
				RecordRepository.del(pm, id);
				return;
			}
			RecordRepository.set(pm, id, DBJson.operator().toJson(map));
		}
	}

}
