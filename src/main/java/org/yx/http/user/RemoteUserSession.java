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

import org.yx.http.HttpGson;
import org.yx.http.HttpHeadersHolder;
import org.yx.http.HttpSettings;
import org.yx.redis.Redis;
import org.yx.util.StringUtil;

public class RemoteUserSession implements UserSession {

	private final Redis redis;
	private static final String AES_KEY = "KEY";

	private static final String SESSION_OBJECT = "OBJ";

	private String singleKey(String userId, String type) {
		StringBuilder sb = new StringBuilder();
		sb.append("SINGLE_SES_").append(userId);
		if (type != null && type.length() > 0) {
			sb.append('_').append(type);
		}
		return sb.toString();
	}

	private String bigKey() {
		return bigKey(org.yx.http.HttpHeadersHolder.sessionId());
	}

	private String bigKey(String sessionId) {
		if (sessionId == null || sessionId.isEmpty()) {
			return null;
		}
		return "S#" + sessionId;
	}

	public RemoteUserSession(Redis redis) {
		this.redis = redis;
	}

	@Override
	public void putKey(String sessionId, byte[] key, String userId, String type) {
		String bigkey = bigKey(sessionId);
		if (bigkey == null) {
			return;
		}
		redis.hset(bigkey.getBytes(Redis.UTF8), AES_KEY.getBytes(Redis.UTF8), key);
		redis.expire(bigkey, HttpSettings.httpSessionTimeout(type));
		if ((!WebSessions.isSingleLogin(type)) || StringUtil.isEmpty(userId)) {
			return;
		}

		String userSessionKey = singleKey(userId, type);
		String oldSessionId = redis.get(userSessionKey);
		if (StringUtil.isNotEmpty(oldSessionId)) {
			redis.del(bigKey(oldSessionId));
		}
		redis.setex(userSessionKey, HttpSettings.singleSessionTimeout(type), sessionId);
	}

	@Override
	public byte[] getKey(String sid) {
		String bigKey = this.bigKey(sid);
		if (bigKey == null) {
			return null;
		}
		return redis.hget(bigKey.getBytes(Redis.UTF8), AES_KEY.getBytes(Redis.UTF8));
	}

	@Override
	public <T extends SessionObject> T getUserObject(Class<T> clz) {
		String bigKey = this.bigKey();
		if (bigKey == null) {
			return null;
		}
		String json = redis.hget(bigKey, SESSION_OBJECT);
		if (json == null) {
			return null;
		}
		return HttpGson.gson().fromJson(json, clz);
	}

	@Override
	public void flushSession() {

		String bigKey = this.bigKey();
		if (bigKey == null) {
			return;
		}
		redis.expire(bigKey, HttpSettings.httpSessionTimeout(HttpHeadersHolder.getType()));
	}

	@Override
	public void setSession(String sid, SessionObject sessionObj) {
		String bigKey = this.bigKey(sid);
		String json = HttpGson.gson().toJson(sessionObj);
		redis.hset(bigKey, SESSION_OBJECT, json);
		redis.expire(bigKey, HttpSettings.httpSessionTimeout(HttpHeadersHolder.getType()));
	}

	@Override
	public void removeSession() {
		String bigKey = this.bigKey();
		if (bigKey == null) {
			return;
		}
		redis.del(bigKey);
	}

	@Override
	public void updateSession(SessionObject sessionObj) {
		String bigKey = this.bigKey();
		if (bigKey == null) {
			return;
		}
		setSession(bigKey, sessionObj);
	}

	@Override
	public boolean isLogin(String userId, String type) {
		if (StringUtil.isEmpty(userId)) {
			return false;
		}
		return Boolean.TRUE.equals(redis.exists(this.singleKey(userId, type)));
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
