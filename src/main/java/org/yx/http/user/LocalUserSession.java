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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.yx.conf.AppInfo;
import org.yx.http.kit.HttpSettings;
import org.yx.log.Logs;
import org.yx.main.SumkThreadPool;
import org.yx.util.S;
import org.yx.util.StringUtil;

public class LocalUserSession implements UserSession {

	protected final ConcurrentMap<String, TimedCachedObject> map = new ConcurrentHashMap<>();

	protected Map<String, String> userSessionMap = Collections.synchronizedMap(new LinkedHashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(Entry<String, String> eldest) {
			return this.size() > AppInfo.getInt("sumk.http.localsession.maxSize", 1000);
		}

	});

	private String singleKey(String userId) {
		return userId;
	}

	@Override
	public boolean setSession(String sessionId, SessionObject sessionObj, byte[] key, boolean singleLogin) {
		long now = System.currentTimeMillis();
		TimedCachedObject to = new TimedCachedObject(S.json.toJson(sessionObj), key, now);
		if (map.putIfAbsent(sessionId, to) != null) {
			return false;
		}
		if (!singleLogin) {
			return true;
		}
		String oldSessionId = userSessionMap.put(singleKey(sessionObj.getUserId()), sessionId);
		if (oldSessionId != null) {
			map.remove(oldSessionId);
		}
		return true;
	}

	public LocalUserSession() {
		Logs.http().info("use local user session");
		long seconds = AppInfo.getInt("sumk.http.session.period", 30);
		SumkThreadPool.scheduledExecutor().scheduleWithFixedDelay(
				() -> CacheHelper.expire(map, HttpSettings.getHttpSessionTimeoutInMs()), seconds, seconds,
				TimeUnit.SECONDS);
	}

	protected TimedCachedObject loadUserObject(String sid) {
		if (sid == null) {
			return null;
		}
		TimedCachedObject to = map.get(sid);
		if (to == null) {
			return null;
		}
		long now = System.currentTimeMillis();
		if (to.refreshTime + HttpSettings.getHttpSessionTimeoutInMs() < now) {
			map.remove(sid);
			return null;
		}
		to.refreshTime = now;
		return to;
	}

	@Override
	public <T extends SessionObject> T getUserObject(String sessionId, Class<T> clz) {
		TimedCachedObject to = this.loadUserObject(sessionId);
		if (to == null) {
			return null;
		}
		return S.json.fromJson(to.getJson(), clz);
	}

	@Override
	public void removeSession(String sessionId) {
		if (sessionId == null) {
			return;
		}
		map.remove(sessionId);
	}

	@Override
	public byte[] getKey(String sid) {
		TimedCachedObject to = this.loadUserObject(sid);
		if (to == null) {
			return null;
		}
		return to.getKey();
	}

	@Override
	public boolean isLogin(String userId) {
		if (StringUtil.isEmpty(userId)) {
			return false;
		}
		return this.userSessionMap.containsKey(singleKey(userId));
	}

	@Override
	public int localCacheSize() {
		return this.map.size();
	}

	@Override
	public boolean valid(String sessionId) {
		return true;
	}

}
