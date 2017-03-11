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
import org.yx.redis.Redis;
import org.yx.util.GsonUtil;

public class RemoteUserSession implements UserSession {

	private final Redis redis;
	private static final String AES_KEY = "KEY";
	private static final String SESSION_OBJECT = "OBJ";

	/**
	 * 需要判断返回值是不是null,它不会返回空字符串
	 * 
	 * @return
	 */
	private String bigKey() {
		return bigKey(org.yx.http.HttpHeadersHolder.token());
	}

	private String bigKey(String token) {
		if (token == null || token.isEmpty()) {
			return null;
		}
		return "S#" + token;
	}

	public RemoteUserSession(Redis redis) {
		this.redis = redis;
	}

	@Override
	public void putKey(String token, byte[] key) {
		String bigkey = bigKey(token);
		redis.hset(bigkey, AES_KEY, key);
		redis.expire(bigkey, AppInfo.httpSessionTimeout);
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
		redis.expire(bigKey, AppInfo.httpSessionTimeout);
	}

	@Override
	public void setSession(String sid, SessionObject sessionObj) {
		String bigKey = this.bigKey(sid);
		String json = GsonUtil.toJson(sessionObj);
		redis.hset(bigKey, SESSION_OBJECT, json);
		redis.expire(bigKey, AppInfo.httpSessionTimeout);
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

}
