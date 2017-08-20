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
package org.yx.http.filter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.yx.common.TimedObject;
import org.yx.conf.AppInfo;
import org.yx.http.HttpHeadersHolder;
import org.yx.http.HttpSettings;
import org.yx.log.Log;
import org.yx.main.SumkThreadPool;
import org.yx.util.StringUtil;

public class LocalUserSession implements UserSession {

	private Map<String, TimedObject> map = new ConcurrentHashMap<>();
	private Map<String, byte[]> keyMap = new ConcurrentHashMap<>();

	private Map<String, String> userSessionMap = new ConcurrentHashMap<>();

	@Override
	public void putKey(String sessionId, byte[] key, String userId) {
		keyMap.put(sessionId, key);
		if (StringUtil.isEmpty(userId)) {
			return;
		}
		String oldSession = userSessionMap.put(userId, sessionId);
		if (oldSession != null) {
			keyMap.remove(oldSession);
			map.remove(oldSession);
		}
	}

	public LocalUserSession() {
		Log.get("session").info("use local user session");
		long seconds = AppInfo.getInt("http.localsession.period", 60);
		SumkThreadPool.scheduledExecutor.scheduleWithFixedDelay(() -> {
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
		TimedObject to = map.get(HttpHeadersHolder.sessionId());
		if (to == null) {
			return null;
		}
		return (T) to.getTarget();
	}

	@Override
	public void flushSession() {
		TimedObject to = map.get(HttpHeadersHolder.sessionId());
		if (to == null) {
			return;
		}
		to.setEvictTime(System.currentTimeMillis() + HttpSettings.httpSessionTimeout() * 1000);

	}

	@Override
	public void setSession(String sessionId, SessionObject sessionObj) {
		TimedObject to = new TimedObject();
		to.setTarget(sessionObj);
		to.setEvictTime(System.currentTimeMillis() + HttpSettings.httpSessionTimeout() * 1000);
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
	public boolean isLogin(String userId) {
		if (StringUtil.isEmpty(userId)) {
			return false;
		}
		return this.userSessionMap.containsKey(userId);
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
