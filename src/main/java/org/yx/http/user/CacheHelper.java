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
package org.yx.http.user;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.yx.log.Log;

public final class CacheHelper {
	public static void expire(ConcurrentMap<String, TimedCachedObject> map, long duration) {
		Logger log = Log.get("sumk.http.session");
		int beginSize = map.size();
		Set<String> set = map.keySet();
		long begin = System.currentTimeMillis() - duration;
		for (String key : set) {
			TimedCachedObject t = map.get(key);
			if (t == null) {
				continue;
			}
			if (t.refreshTime < begin) {
				log.trace("{} remove from cache", key);
				map.remove(key);
			}
		}
		if (log.isTraceEnabled()) {
			log.trace("catch size from {} to {},duration:{}", beginSize, map.size(), duration);
			;
		}
	}

}
