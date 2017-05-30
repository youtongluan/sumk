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

import org.yx.conf.AppInfo;
import org.yx.http.HttpSessionHolder;
import org.yx.http.HttpSettings;
import org.yx.redis.Redis;
import org.yx.util.GsonUtil;
import org.yx.util.StringUtils;

public class RemoteUserSession implements UserSession {

	private final Redis redis;
	private static final String AES_KEY = "KEY";

	private static final String SESSION_OBJECT = "OBJ";

	private static final String SINGLE_SESSION_PRE = "SINGLE_SES_";

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
	public void putKey(String sessionId, byte[] key, String userId) {
		String bigkey = bigKey(sessionId);
		redis.hset(bigkey, AES_KEY, key);
		redis.expire(bigkey, HttpSettings.httpSessionTimeout());
		if ((!HttpSessionHolder.isSingleLogin()) || StringUtils.isEmpty(userId)) {
			return;
		}

		String userSessionKey = SINGLE_SESSION_PRE + userId;
		String oldSessionId = redis.get(userSessionKey);
		if (StringUtils.isNotEmpty(oldSessionId)) {
			redis.del(bigKey(oldSessionId));
		}
		redis.setex(userSessionKey, AppInfo.getInt("http.session.single.timeout", 3600 * 24), sessionId);
	}

	@Override
	public byte[] getKey(String sid) {
		String bigKey = this.bigKey(sid);
		if (bigKey == null) {
			return null;
		}
		return redis.hgetBinarry(bigKey, AES_KEY);
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
		return GsonUtil.fromJson(json, clz);
	}

	@Override
	public void flushSession() {

		String bigKey = this.bigKey();
		if (bigKey == null) {
			return;
		}
		redis.expire(bigKey, HttpSettings.httpSessionTimeout());
	}

	@Override
	public void setSession(String sid, SessionObject sessionObj) {
		String bigKey = this.bigKey(sid);
		String json = GsonUtil.toJson(sessionObj);
		redis.hset(bigKey, SESSION_OBJECT, json);
		redis.expire(bigKey, HttpSettings.httpSessionTimeout());
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
	public boolean isLogin(String userId) {
		if (StringUtils.isEmpty(userId)) {
			return false;
		}
		return redis.exists(SINGLE_SESSION_PRE + userId) == Boolean.TRUE;
	}

}
