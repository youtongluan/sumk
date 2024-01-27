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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.yx.common.util.S;
import org.yx.conf.AppInfo;
import org.yx.http.kit.HttpSettings;
import org.yx.log.Logs;
import org.yx.util.Task;

public class LocalUserSession extends AbstractUserSession {

	protected Map<String, String> userSessionMap = Collections
			.synchronizedMap(new LinkedHashMap<String, String>(128, 0.75f, true) {
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean removeEldestEntry(Entry<String, String> eldest) {
					return this.size() > AppInfo.getInt("sumk.http.localsession.maxSize", 1000);
				}

			});

	public LocalUserSession() {
		log.info("$$$use local user session");
		long seconds = AppInfo.getLong("sumk.http.session.period", 30L);
		Task.scheduleAtFixedRate(() -> CacheHelper.expire(cache, HttpSettings.httpSessionTimeoutInMs()), seconds,
				seconds, TimeUnit.SECONDS);
	}

	@Override
	public boolean setSession(String sessionId, SessionObject sessionObj, byte[] key, boolean singleLogin) {
		TimedCachedObject to = new TimedCachedObject(S.json().toJson(sessionObj), key);
		to.refreshTime = System.currentTimeMillis();
		if (cache.putIfAbsent(sessionId, to) != null) {
			return false;
		}
		if (!singleLogin) {
			return true;
		}
		String oldSessionId = userSessionMap.put(sessionObj.getUserId(), sessionId);
		if (oldSessionId != null && !oldSessionId.equals(sessionId)) {
			cache.remove(oldSessionId);
		}
		return true;
	}

	protected TimedCachedObject loadTimedCachedObject(String sid, boolean needRefresh) {
		if (sid == null) {
			return null;
		}
		TimedCachedObject to = cache.get(sid);
		if (to == null) {
			return null;
		}
		long now = System.currentTimeMillis();
		if (to.refreshTime + HttpSettings.httpSessionTimeoutInMs() < now) {
			Logs.http().debug("session:{}实际上已经过期了", sid);
			cache.remove(sid);
			return null;
		}
		if (needRefresh) {
			to.refreshTime = now;
		}
		return to;
	}

	@Override
	public void removeSession(String sessionId) {
		if (sessionId == null) {
			return;
		}
		TimedCachedObject to = cache.remove(sessionId);
		if (to == null || userSessionMap.isEmpty()) {
			return;
		}
		SessionObject obj = S.json().fromJson(to.json, SessionObject.class);
		this.userSessionMap.remove(obj.userId);
	}

	@Override
	public String getSessionIdByUserFlag(String userId) {
		return this.userSessionMap.get(userId);
	}

}
