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

import java.util.concurrent.TimeUnit;

import org.yx.conf.AppInfo;
import org.yx.http.kit.HttpSettings;
import org.yx.redis.Redis;
import org.yx.util.S;
import org.yx.util.StringUtil;
import org.yx.util.Task;

public class RemoteUserSession extends AbstractUserSession {
	private static final byte[] NX = { 'N', 'X' };
	private static final byte[] PX = { 'P', 'X' };

	protected int maxSize = AppInfo.getInt("sumk.http.session.cache.maxsize", 5000);
	protected int noFreshTime = AppInfo.getInt("sumk.http.session.cache.nofreshtime", 1000 * 2);

	private final Redis redis;

	public RemoteUserSession(Redis redis) {
		this.redis = redis;
		long seconds = AppInfo.getInt("sumk.http.session.period", 30);
		Task.scheduleAtFixedRate(() -> {

			long duration = AppInfo.getLong("sumk.http.session.remote.duration", 1000L * 60 * 5);
			if (duration > HttpSettings.httpSessionTimeoutInMs()) {
				duration = HttpSettings.httpSessionTimeoutInMs();
			}
			duration -= cache.size() * 10;
			if (duration < this.noFreshTime) {
				duration = this.noFreshTime + 100L;
			}
			CacheHelper.expire(cache, duration);
		}, seconds, seconds, TimeUnit.SECONDS);
	}

	protected final String singleKey(String userId) {
		return "_SINGLE_SES_".concat(userId);
	}

	protected final byte[] redisSessionKey(String sessionId) {
		if (sessionId == null || sessionId.isEmpty()) {
			return null;
		}
		return ("_SES_#" + sessionId).getBytes(AppInfo.UTF8);
	}

	@Override
	protected TimedCachedObject loadTimedCachedObject(String sid, boolean needRefresh) {
		if (sid == null) {
			return null;
		}
		byte[] bigKey = this.redisSessionKey(sid);
		TimedCachedObject to = cache.get(sid);
		long now = System.currentTimeMillis();
		if (to == null) {
			byte[] bv = redis.get(bigKey);
			to = TimedCachedObject.deserialize(bv);
			if (to == null) {
				if (log.isTraceEnabled()) {
					log.trace("{} cannot found from redis", sid);
				}
				return null;
			}
			if (cache.size() < this.maxSize && needRefresh) {
				if (log.isTraceEnabled()) {
					log.trace("{} add to local cache", sid);
				}
				cache.put(sid, to);
			}
		}

		if (needRefresh && to.refreshTime + this.noFreshTime < now) {
			long durationInMS = HttpSettings.httpSessionTimeoutInMs();
			Long v = redis.pexpire(bigKey, durationInMS);
			if (v == null || v.intValue() == 0) {
				cache.remove(sid);
				log.trace("{} was pexpire by redis,and remove from local cache", sid);
				return null;
			}
			if (log.isTraceEnabled()) {
				log.trace("{} was refresh in redis", sid);
			}
			to.refreshTime = now;
		}
		return to;
	}

	@Override
	public void removeSession(String sessionId) {
		byte[] bigKey = this.redisSessionKey(sessionId);
		if (bigKey == null) {
			return;
		}
		if (HttpSettings.isSingleLogin()) {
			SessionObject t = getUserObjectBySessionId(sessionId);
			if (t != null) {
				redis.del(this.singleKey(t.getUserId()));
			}
		}
		this.cache.remove(sessionId);
		redis.del(bigKey);
	}

	@Override
	public boolean setSession(String sessionId, SessionObject sessionObj, byte[] key, boolean singleLogin) {
		long sessionTimeout = HttpSettings.httpSessionTimeoutInMs();
		byte[] bigKey = this.redisSessionKey(sessionId);
		String json = S.json().toJson(sessionObj);
		byte[] data = TimedCachedObject.toBytes(json, key);
		String ret = redis.set(bigKey, data, NX, PX, sessionTimeout);
		if (!"OK".equalsIgnoreCase(ret) && !"1".equals(ret)) {
			return false;
		}
		if (!singleLogin) {
			return true;
		}

		String singleUserKey = singleKey(sessionObj.userId);
		String oldSessionId = redis.get(singleUserKey);
		if (StringUtil.isNotEmpty(oldSessionId) && !oldSessionId.equals(sessionId)) {
			redis.del(redisSessionKey(oldSessionId));
			cache.remove(oldSessionId);
		}
		long expireTime = sessionObj.getExpiredTime() != null ? sessionObj.getExpiredTime() - System.currentTimeMillis()
				: -1;
		if (expireTime < 1) {

			expireTime = AppInfo.getLong("sumk.http.session.single.maxTime", 1000L * 3600 * 20);
		}
		redis.psetex(singleUserKey, expireTime, sessionId);
		return true;
	}

	@Override
	public String getSessionIdByUserFlag(String userId) {
		return redis.get(this.singleKey(userId));
	}
}
