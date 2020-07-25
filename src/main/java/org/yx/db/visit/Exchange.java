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
package org.yx.db.visit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.yx.db.sql.PojoMeta;
import org.yx.log.Log;
import org.yx.redis.RecordRepository;
import org.yx.redis.RedisPool;
import org.yx.util.StringUtil;

public class Exchange {

	private List<Map<String, Object>> leftIn;

	private List<String> data;

	public Exchange(List<Map<String, Object>> leftIn) {
		this.leftIn = leftIn == null ? null : Collections.unmodifiableList(leftIn);
	}

	public List<Map<String, Object>> getLeftIn() {
		return this.leftIn;
	}

	public List<String> getData() {
		return data;
	}

	public void findFromCache(PojoMeta pm) {
		List<Map<String, Object>> origin = this.leftIn;

		if (origin == null || origin.isEmpty() || RedisPool.defaultRedis() == null) {
			return;
		}
		try {
			List<String> redisList = new ArrayList<>(origin.size());
			List<Map<String, Object>> redisConditions = new ArrayList<>(origin.size());

			List<Map<String, Object>> notFound = new ArrayList<>(origin.size());
			for (Map<String, Object> map : origin) {
				if (pm.isOnlyCacheID(map)) {
					redisList.add(pm.getCacheID(map, false));
					redisConditions.add(map);
				} else {
					notFound.add(map);
				}
			}
			if (redisList.isEmpty()) {
				return;
			}
			List<String> redisData = RecordRepository.getMultiValue(pm, redisList);
			if (redisData == null || redisData.isEmpty()) {
				return;
			}
			this.data = new ArrayList<>(redisData.size());
			for (int i = 0; i < redisConditions.size(); i++) {
				Map<String, Object> conditon = redisConditions.get(i);

				if (i < redisData.size() && StringUtil.isNotEmpty(redisData.get(i))) {
					this.data.add(redisData.get(i));
					continue;
				}

				notFound.add(conditon);
			}
			this.leftIn = Collections.unmodifiableList(notFound);
		} catch (Exception e) {
			this.data = null;
			Log.printStack("sumk.sql", e);
		}
	}

}
