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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.yx.common.date.TimedObject;
import org.yx.conf.AppInfo;
import org.yx.http.HttpHeadersHolder;
import org.yx.http.HttpSettings;
import org.yx.log.Log;
import org.yx.main.SumkThreadPool;
import org.yx.util.StringUtil;

public class LocalUserSession implements UserSession {

	private ConcurrentMap<String, TimedObject> map = new ConcurrentHashMap<>();
	private ConcurrentMap<String, byte[]> keyMap = new ConcurrentHashMap<>();

	private Map<String, String> userSessionMap = Collections.synchronizedMap(new LinkedHashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(Entry<String, String> eldest) {
			return this.size() > AppInfo.getInt("sumk.http.localsession.maxSize", 1000);
		}

	});

	private String singleKey(String userId, String type) {
		StringBuilder sb = new StringBuilder();
		sb.append(userId);
		if (type != null && type.length() > 0) {
			sb.append('_').append(type);
		}
		return sb.toString();
	}

	@Override
	public void putKey(String sessionId, byte[] key, String userId, String type) {
		keyMap.put(sessionId, key);
		if ((!WebSessions.isSingleLogin(type)) || StringUtil.isEmpty(userId)) {
			return;
		}
		String oldSession = userSessionMap.put(singleKey(userId, type), sessionId);
		if (oldSession != null) {
			keyMap.remove(oldSession);
			map.remove(oldSession);
		}
	}

	public LocalUserSession() {
		Log.get("sumk.http").info("use local user session");
		long seconds = AppInfo.getInt("sumk.http.localsession.period", 60);
		SumkThreadPool.scheduledExecutor().scheduleWithFixedDelay(() -> {
			Set<String> set = map.keySet();
			long now = System.currentTimeMillis();
			for (String key : set) {
				TimedObject t = map.get(key);
				if (t == null) {
					continue;
				}
				if (now > t.getEvictTime()) {
					map.remove(key);
					keyMap.remove(key);
				}
			}
		}, seconds, seconds, TimeUnit.SECONDS);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends SessionObject> T getUserObject(Class<T> clz) {
		String sid = HttpHeadersHolder.sessionId();
		if (sid == null) {
			return null;
		}
		TimedObject to = map.get(sid);
		if (to == null) {
			return null;
		}
		return (T) to.getTarget();
	}

	@Override
	public void flushSession() {
		String sid = HttpHeadersHolder.sessionId();
		if (sid == null) {
			return;
		}
		TimedObject to = map.get(sid);
		if (to == null) {
			return;
		}
		to.setEvictTime(
				System.currentTimeMillis() + HttpSettings.httpSessionTimeout(HttpHeadersHolder.getType()) * 1000);

	}

	@Override
	public void setSession(String sessionId, SessionObject sessionObj) {
		TimedObject to = new TimedObject();
		to.setTarget(sessionObj);
		to.setEvictTime(
				System.currentTimeMillis() + HttpSettings.httpSessionTimeout(HttpHeadersHolder.getType()) * 1000);
		map.put(sessionId, to);
	}

	@Override
	public void removeSession() {
		String sessionId = HttpHeadersHolder.sessionId();
		if (sessionId == null) {
			return;
		}
		map.remove(sessionId);
		keyMap.remove(sessionId);
	}

	@Override
	public byte[] getKey(String sid) {
		return this.keyMap.get(sid);
	}

	@Override
	public void updateSession(SessionObject sessionObj) {
		String sessionId = HttpHeadersHolder.sessionId();
		if (sessionId == null) {
			return;
		}
		setSession(sessionId, sessionObj);
	}

	@Override
	public boolean isLogin(String userId, String type) {
		if (StringUtil.isEmpty(userId)) {
			return false;
		}
		return this.userSessionMap.containsKey(singleKey(userId, type));
	}

	@Override
	public String getUserId() {
		SessionObject obj = this.getUserObject(SessionObject.class);
		if (obj == null) {
			return null;
		}
		return obj.getUserId();
	}

}
