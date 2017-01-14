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
package org.yx.db.listener;

import java.util.HashMap;
import java.util.Map;

import org.yx.bean.Bean;
import org.yx.db.annotation.CacheType;
import org.yx.db.event.UpdateEvent;
import org.yx.db.sql.PojoMeta;
import org.yx.db.sql.PojoMetaHolder;
import org.yx.listener.SumkEvent;
import org.yx.log.Log;
import org.yx.redis.RecordReq;
import org.yx.util.GsonUtil;

@Bean
public class UpdateListener implements DBListener<UpdateEvent> {

	@Override
	public boolean accept(SumkEvent event) {
		return UpdateEvent.class.isInstance(event);
	}

	@Override
	public void listen(UpdateEvent event) {
		try {
			PojoMeta pm = PojoMetaHolder.getTableMeta(event.getTable());
			if (pm == null || pm.isNoCache()) {
				return;
			}
			Map<String, Object> where = event.getWhere();
			String id = pm.getRedisID(where, true);
			if (event.isFullUpdate()) {
				String id_new = pm.getRedisID(event.getTo(), true);
				if (!id.equals(id_new)) {
					RecordReq.del(pm, id);
				}
				if (pm.cacheType() == CacheType.LIST) {
					RecordReq.del(pm, id_new);
				} else {
					RecordReq.set(pm, id_new, GsonUtil.toJson(event.getTo()));
				}
				return;
			}

			RecordReq.del(pm, id);

			Map<String, Object> to = event.getTo();
			Map<String, Object> where2 = new HashMap<>(where);
			where2.putAll(to);
			String id_new = pm.getRedisID(where2, true);

			if (id.equals(id_new)) {
				return;
			}
			RecordReq.del(pm, id_new);

		} catch (Exception e) {
			Log.printStack("db-listener", e);
		}
	}

	@Override
	public String[] getTags() {
		return null;
	}

}
