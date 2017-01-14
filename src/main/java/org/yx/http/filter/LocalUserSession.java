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
import org.yx.log.Log;

public class LocalUserSession implements UserSession {

	private Map<String, TimedObject> map = new ConcurrentHashMap<>();
	private Map<String, byte[]> keyMap = new ConcurrentHashMap<>();

	/**
	 * 
	 * @param sessionId
	 * @param key
	 * @return true表示保存成功，flase失败
	 */
	public void put(String sessionId, byte[] key) {
		keyMap.put(sessionId, key);
	}

	public LocalUserSession() {
		Log.get("session").info("use local user session");
		Thread t = new Thread() {

			@Override
			public void run() {
				while (true) {
					try {
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
						Thread.sleep(TimeUnit.MINUTES.toMillis(1));
					} catch (Exception e) {
						Log.printStack(e);
					}
				}
			}

		};
		t.setDaemon(true);
		t.start();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getUserObject(Class<T> clz) {
		TimedObject to = map.get(HttpHeadersHolder.token());
		if (to == null) {
			return null;
		}
		return (T) to.getTarget();
	}

	@Override
	public void flushSession() {
		TimedObject to = map.get(HttpHeadersHolder.token());
		if (to == null) {
			return;
		}
		to.setEvictTime(System.currentTimeMillis() + AppInfo.httpSessionTimeout * 1000);

	}

	@Override
	public void setSession(String key, Object sessionObj) {
		TimedObject to = new TimedObject();
		to.setTarget(sessionObj);
		to.setEvictTime(System.currentTimeMillis() + AppInfo.httpSessionTimeout * 1000);
		map.put(key, to);
	}

	@Override
	public void removeSession() {
		String token = HttpHeadersHolder.token();
		if (token == null) {
			return;
		}
		map.remove(token);
		keyMap.remove(token);
	}

	@Override
	public byte[] getkey(String sid) {
		return this.keyMap.get(sid);
	}

	@Override
	public void updateSession(Object sessionObj) {
		String token = HttpHeadersHolder.token();
		if (token == null) {
			return;
		}
		setSession(token, sessionObj);
	}

}
