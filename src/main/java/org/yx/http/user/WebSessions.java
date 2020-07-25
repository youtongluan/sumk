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

import org.yx.bean.IOC;
import org.yx.http.HttpContextHolder;
import org.yx.http.HttpErrorCode;
import org.yx.http.kit.HttpException;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.redis.Redis;
import org.yx.redis.RedisLoader;
import org.yx.redis.RedisPool;

public final class WebSessions {
	private static UserSession session;

	public static UserSession userSession() {
		return session;
	}

	public static UserSession loadUserSession() {
		if (session == null) {
			Log.get("sumk.http.session").info("session has not created");
			throw HttpException.create(HttpErrorCode.SESSION_ERROR, "请重新登录");
		}
		return session;
	}

	public static <T extends SessionObject> T getUserObject(Class<T> clz) {
		return loadUserSession().getUserObject(HttpContextHolder.sessionId(), clz);
	}

	public static void remove() {
		userSession();
		if (session == null) {
			Log.get("sumk.http.session").debug("has removed");
			return;
		}
		session.removeSession(HttpContextHolder.sessionId());
	}

	public static synchronized void initSession() {
		if (session != null) {
			return;
		}
		HttpSessionFactory factory = IOC.get(HttpSessionFactory.class);
		session = factory != null ? factory.create() : createDefaultUserSession();
	}

	private static UserSession createDefaultUserSession() {
		try {
			Redis redis = RedisPool.getRedisExactly(RedisLoader.SESSION);
			return redis == null ? new LocalUserSession() : new RemoteUserSession(redis);
		} catch (NoClassDefFoundError e) {
			Logs.http().debug("use local session because redis cannot load", e);
			return new LocalUserSession();
		}
	}
}
