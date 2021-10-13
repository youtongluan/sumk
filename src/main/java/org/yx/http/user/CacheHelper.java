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

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.yx.log.Log;

public final class CacheHelper {
	public static void expire(Map<String, TimedCachedObject> map, long duration) {
		Logger log = Log.get("sumk.http.session");
		try {
			expire0(map, duration, log);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private static void expire0(Map<String, TimedCachedObject> map, long duration, Logger log) {
		int beginSize = map.size();
		long begin = System.currentTimeMillis() - duration;
		Iterator<Map.Entry<String, TimedCachedObject>> it = map.entrySet().iterator();
		while (it.hasNext()) {

			Map.Entry<String, TimedCachedObject> en = it.next();
			if (en == null) {
				continue;
			}
			TimedCachedObject t = en.getValue();
			if (t == null) {
				continue;
			}
			if (t.refreshTime < begin) {
				log.trace("{} remove from cache", en.getKey());
				it.remove();
			}
		}
		if (log.isTraceEnabled()) {
			log.trace("catch size from {} to {},duration:{}", beginSize, map.size(), duration);
		}
	}

}
