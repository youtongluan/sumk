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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.yx.conf.AppInfo;
import org.yx.http.kit.HttpSettings;
import org.yx.log.Log;
import org.yx.redis.Redis;
import org.yx.util.S;
import org.yx.util.StringUtil;
import org.yx.util.Task;

public class RemoteUserSession implements UserSession {
	private static final byte[] NX = { 'N', 'X' };
	private static final byte[] PX = { 'P', 'X' };
	private Logger log = Log.get("sumk.http.session");

	protected final ConcurrentMap<String, TimedCachedObject> cache = new ConcurrentHashMap<>();
	protected int maxSize = AppInfo.getInt("sumk.http.session.catch.maxsize", 5000);
	protected int noFreshTime = AppInfo.getInt("sumk.http.session.catch.nofreshtime", 1000 * 2);

	private final Redis redis;

	protected final String singleKey(String userId) {
		return "_SINGLE_SES_".concat(userId);
	}

	protected final byte[] bigKey(String sessionId) {
		if (sessionId == null || sessionId.isEmpty()) {
			return null;
		}
		return ("_SES_#" + sessionId).getBytes(AppInfo.UTF8);
	}

	public RemoteUserSession(Redis redis) {
		this.redis = redis;
		long seconds = AppInfo.getInt("sumk.http.session.period", 30);
		Task.scheduleAtFixedRate(() -> {

			long duration = AppInfo.getLong("sumk.http.session.remote.duration", 1000L * 60 * 5);
			if (duration > HttpSettings.getHttpSessionTimeoutInMs()) {
				duration = HttpSettings.getHttpSessionTimeoutInMs();
			}
			duration -= cache.size() * 10;
			if (duration < this.noFreshTime) {
				duration = this.noFreshTime + 100;
			}
			CacheHelper.expire(cache, duration);
		}, seconds, seconds, TimeUnit.SECONDS);
	}

	protected TimedCachedObject load(byte[] bigKey, long refreshTime) {
		byte[] bv = redis.get(bigKey);
		return TimedCachedObject.deserialize(bv, refreshTime);
	}

	protected TimedCachedObject loadUserObject(String sid) {
		if (sid == null) {
			return null;
		}
		byte[] bigKey = this.bigKey(sid);
		TimedCachedObject to = cache.get(sid);
		long now = System.currentTimeMillis();
		if (to == null) {
			to = this.load(bigKey, now);
			if (to == null) {
				log.trace("{} cannot found from redis", sid);
				return null;
			}
			if (cache.size() < this.maxSize) {
				log.trace("{} add to local cache", sid);
				cache.put(sid, to);
			}
		}

		if (to.refreshTime + this.noFreshTime < now) {
			long durationInMS = HttpSettings.getHttpSessionTimeoutInMs();
			Long v = redis.pexpire(bigKey, durationInMS);
			if (v == null || v.intValue() == 0) {
				cache.remove(sid);
				log.trace("{} was pexpire by redis,and remove from local cache", sid);
				return null;
			}
			log.trace("{} was refresh in redis", sid);
			to.refreshTime = now;
		}
		return to;
	}

	@Override
	public byte[] getKey(String sid) {
		TimedCachedObject obj = this.loadUserObject(sid);
		if (obj == null) {
			return null;
		}
		return obj.getKey();
	}

	@Override
	public <T extends SessionObject> T getUserObject(String sessionId, Class<T> clz) {
		TimedCachedObject obj = this.loadUserObject(sessionId);
		if (obj == null) {
			return null;
		}
		return S.json().fromJson(obj.json, clz);
	}

	@Override
	public void removeSession(String sessionId) {
		byte[] bigKey = this.bigKey(sessionId);
		if (bigKey == null) {
			return;
		}
		redis.del(bigKey);
		this.cache.remove(sessionId);
	}

	@Override
	public boolean isLogin(String userId) {
		if (StringUtil.isEmpty(userId)) {
			return false;
		}
		return Boolean.TRUE.equals(redis.exists(this.singleKey(userId)));
	}

	@Override
	public boolean setSession(String sessionId, SessionObject sessionObj, byte[] key, boolean singleLogin) {
		long sessionTimeout = HttpSettings.getHttpSessionTimeoutInMs();
		byte[] bigKey = this.bigKey(sessionId);
		String json = S.json().toJson(sessionObj);
		byte[] data = TimedCachedObject.toBytes(json, key);
		String ret = redis.set(bigKey, data, NX, PX, sessionTimeout);
		if (!"OK".equalsIgnoreCase(ret) && !"1".equals(ret)) {
			return false;
		}
		if (!singleLogin) {
			return true;
		}

		String userSessionKey = singleKey(sessionObj.userId);
		String oldSessionId = redis.get(userSessionKey);
		if (StringUtil.isNotEmpty(oldSessionId)) {
			redis.del(bigKey(oldSessionId));
			cache.remove(oldSessionId);
		}
		redis.psetex(userSessionKey, sessionTimeout, sessionId);
		return true;
	}

	@Override
	public int localCacheSize() {
		return this.cache.size();
	}

	@Override
	public boolean valid(String sessionId) {
		return true;
	}

}
