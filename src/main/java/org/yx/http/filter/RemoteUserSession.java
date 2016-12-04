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
