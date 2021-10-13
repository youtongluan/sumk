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

import org.slf4j.Logger;
import org.yx.http.kit.HttpSettings;
import org.yx.log.Log;
import org.yx.util.S;
import org.yx.util.StringUtil;
import org.yx.util.SumkDate;

public abstract class AbstractUserSession implements UserSession {
	protected final Logger log = Log.get("sumk.http.session");
	protected final ConcurrentMap<String, TimedCachedObject> cache = new ConcurrentHashMap<>();

	protected abstract TimedCachedObject loadTimedCachedObject(String sessionId, boolean needRefresh);

	public abstract String getSessionIdByUserFlag(String userId);

	@Override
	public byte[] getEncryptKey(String sid) {
		TimedCachedObject obj = this.loadTimedCachedObject(sid, false);
		if (obj == null) {
			return null;
		}
		return obj.getKey();
	}

	@Override
	public <T extends SessionObject> T getUserObject(String sessionId, Class<T> clz) {
		return this._getUserObject(sessionId, clz, false);
	}

	@Override
	public <T extends SessionObject> T loadUserObject(String sessionId, Class<T> clz) {
		return this._getUserObject(sessionId, clz, true);
	}

	private <T extends SessionObject> T _getUserObject(String sessionId, Class<T> clz, boolean refreshTTL) {
		TimedCachedObject obj = this.loadTimedCachedObject(sessionId, refreshTTL);
		if (obj == null) {
			return null;
		}
		return S.json().fromJson(obj.json, clz);
	}

	@Override
	public String sessionId(String userId) {
		if (!HttpSettings.isSingleLogin()) {
			log.warn("只有设置了sumk.http.session.single=1,本方法才有意义");
			return null;
		}
		if (StringUtil.isEmpty(userId)) {
			return null;
		}
		String sessionId = this.getSessionIdByUserFlag(userId);
		if (sessionId == null) {
			return null;
		}
		SessionObject obj = this.getUserObjectBySessionId(sessionId);
		if (obj == null) {
			return null;
		}
		if (obj.getExpiredTime() == null) {
			return null;
		}
		if (obj.getExpiredTime().longValue() < System.currentTimeMillis()) {
			log.debug("该过期session的截止时间:{}", SumkDate.of(obj.getExpiredTime()));
			return null;
		}
		return obj.getUserId();
	}

	protected SessionObject getUserObjectBySessionId(String sessionId) {
		TimedCachedObject obj = this.loadTimedCachedObject(sessionId, false);
		if (obj == null) {
			return null;
		}
		return S.json().fromJson(obj.json, SessionObject.class);
	}

	@Override
	public int localCacheSize() {
		return this.cache.size();
	}
}
