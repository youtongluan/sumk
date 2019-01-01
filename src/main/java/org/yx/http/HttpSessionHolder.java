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
package org.yx.http;

import org.yx.bean.IOC;
import org.yx.conf.AppInfo;
import org.yx.exception.BizException;
import org.yx.http.filter.LocalUserSession;
import org.yx.http.filter.RemoteUserSession;
import org.yx.http.filter.SessionObject;
import org.yx.http.filter.UserSession;
import org.yx.log.Log;
import org.yx.redis.Redis;
import org.yx.redis.RedisConstants;
import org.yx.redis.RedisPool;

public class HttpSessionHolder {
	static UserSession session;

	public static UserSession userSession() {
		if (session == null) {
			initSession();
		}
		return session;
	}

	public static UserSession loadUserSession() {
		if (session == null) {
			initSession();
		}
		if (session == null) {
			Log.get("session").info("session has not created");
			BizException.throwException(HttpErrorCode.SESSION_ERROR, "请重新登陆.");
		}
		return session;
	}

	public static <T extends SessionObject> T getUserObject(Class<T> clz) {
		return loadUserSession().getUserObject(clz);
	}

	public static void remove() {
		userSession();
		if (session == null) {
			Log.get("session").debug("has removed");
			return;
		}
		session.removeSession();
	}

	public static void updateUserObject(SessionObject sessionObj) {
		loadUserSession().updateSession(sessionObj);
	}

	public static boolean isSingleLogin(String type) {
		boolean single = AppInfo.getBoolean("http.session.single", false);
		if (type == null || type.isEmpty()) {
			return single;
		}
		return AppInfo.getBoolean("http.session.single." + type, single);
	}

	private static synchronized void initSession() {
		if (session != null) {
			return;
		}
		Redis redis = RedisPool.getRedisExactly(RedisConstants.SESSION);
		HttpSessionFactory factory = IOC.get(HttpSessionFactory.class);
		if (factory == null) {
			factory = () -> redis == null ? new LocalUserSession() : new RemoteUserSession(redis);
		}
		session = factory.create();
		if (LocalUserSession.class.isInstance(session)) {
			Log.get("loginAction").info("use local session.");
		}
	}
}
