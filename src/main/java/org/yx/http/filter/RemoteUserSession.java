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
import org.yx.http.HttpHeadersHolder;
import org.yx.redis.Redis;
import org.yx.util.GsonUtil;

public class RemoteUserSession implements UserSession {

	private final Redis redis;
	private static final String KEY_PRE = "USR_KEY_";
	private static final String SESSION_OBJECT_PRE = "USR_OBJ_";

	private String sessionKey() {
		return KEY_PRE + HttpHeadersHolder.token();
	}

	private String sessionKey(String sid) {
		return KEY_PRE + sid;
	}

	private String userObjectKey() {
		return SESSION_OBJECT_PRE + HttpHeadersHolder.token();
	}

	private String userObjectKey(String sid) {
		return SESSION_OBJECT_PRE + sid;
	}

	public RemoteUserSession(Redis redis) {
		this.redis = redis;
	}

	@Override
	public void put(String sessionId, byte[] key) {
		redis.setex(sessionKey(sessionId), AppInfo.httpSessionTimeout, key);
	}

	@Override
	public byte[] getkey(String sid) {
		return redis.getBytes(this.sessionKey(sid));
	}

	@Override
	public <T> T getUserObject(Class<T> clz) {
		String key = userObjectKey();
		if (key == null) {
			return null;
		}
		String json = redis.get(key);
		if (json == null) {
			return null;
		}
		return GsonUtil.fromJson(json, clz);
	}

	@Override
	public void flushSession() {

		redis.expire(userObjectKey(), AppInfo.httpSessionTimeout + 300);
		redis.expire(sessionKey(), AppInfo.httpSessionTimeout);
	}

	@Override
	public void setSession(String sid, Object sessionObj) {
		String json = GsonUtil.toJson(sessionObj);
		redis.setex(this.userObjectKey(sid), AppInfo.httpSessionTimeout + 300, json);
	}

	@Override
	public void removeSession() {
		String token = HttpHeadersHolder.token();
		if (token == null) {
			return;
		}
		redis.del(sessionKey(), userObjectKey());
	}

	@Override
	public void updateSession(Object sessionObj) {
		String token = HttpHeadersHolder.token();
		if (token == null) {
			return;
		}
		setSession(HttpHeadersHolder.token(), GsonUtil.toJson(sessionObj));
	}

}
