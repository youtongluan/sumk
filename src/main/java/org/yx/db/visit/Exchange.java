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
package org.yx.db.visit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yx.db.sql.PojoMeta;
import org.yx.log.Log;
import org.yx.redis.RecordReq;
import org.yx.redis.RedisPool;

public class Exchange {

	private List<Map<String, Object>> leftIn;

	private List<String> data;

	private List<Map<String, Object>> canToRedis;

	public Exchange(List<Map<String, Object>> leftIn) {
		this.leftIn = leftIn;
	}

	public List<Map<String, Object>> getLeftIn() {
		return leftIn;
	}

	public List<String> getData() {
		return data;
	}

	public List<Map<String, Object>> getCanToRedis() {
		return canToRedis;
	}

	public void findFromCache(PojoMeta pm) {
		List<Map<String, Object>> origin = this.leftIn;
		if (origin == null || origin.isEmpty() || RedisPool.defaultRedis() == null) {
			return;
		}
		try {
			boolean[] onlyRedis = new boolean[origin.size()];
			List<String> redisList = new ArrayList<>();
			for (int i = 0; i < onlyRedis.length; i++) {
				Map<String, Object> map = origin.get(i);
				if (pm.isOnlyRedisID(map)) {
					redisList.add(pm.getRedisID(map, false));
					onlyRedis[i] = true;
				}
			}
			List<String> fromRedis = RecordReq.getMultiValue(pm, redisList);
			if (fromRedis == null || fromRedis.isEmpty()) {
				return;
			}

			this.leftIn = new ArrayList<>();
			int k = 0;
			this.data = new ArrayList<>();
			for (int i = 0; i < onlyRedis.length; i++) {
				Map<String, Object> conditon = origin.get(i);

				if (!onlyRedis[i]) {
					this.leftIn.add(conditon);
					continue;
				}

				String value = fromRedis.get(k);
				k++;

				if (value != null && value.length() > 0) {
					this.data.add(value);
					continue;
				}

				if (this.canToRedis == null) {
					this.canToRedis = new ArrayList<>();
				}
				this.canToRedis.add(conditon);
				this.leftIn.add(conditon);

			}
		} catch (Exception e) {
			this.leftIn = origin;
			this.data = null;
			this.canToRedis = null;
			Log.printStack("sumk.sql", e);
		}
	}

}
