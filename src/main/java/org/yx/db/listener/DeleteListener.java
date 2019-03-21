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

import java.util.List;
import java.util.Map;

import org.yx.annotation.Bean;
import org.yx.db.event.DeleteEvent;
import org.yx.db.sql.PojoMeta;
import org.yx.db.sql.PojoMetaHolder;
import org.yx.listener.SumkEvent;
import org.yx.log.Log;
import org.yx.redis.RecordReq;

@Bean
public class DeleteListener implements DBListener<DeleteEvent> {

	@Override
	public boolean accept(SumkEvent event) {
		return DeleteEvent.class.isInstance(event);
	}

	@Override
	public void listen(DeleteEvent event) {
		try {
			PojoMeta pm = PojoMetaHolder.getTableMeta(event.getTable());
			if (pm == null || pm.isNoCache()) {
				return;
			}
			List<Map<String, Object>> wheres = event.getWheres();
			if (wheres == null || wheres.isEmpty()) {
				return;
			}
			for (Map<String, Object> src : wheres) {
				String id = pm.getRedisID(src, true);
				RecordReq.del(pm, id);
			}
		} catch (Exception e) {
			Log.printStack("db-listener", e);
		}
	}

}
