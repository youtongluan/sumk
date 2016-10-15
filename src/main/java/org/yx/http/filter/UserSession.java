package org.yx.http.filter;

public interface UserSession {
	/**
	 * 获取当前的session的加密key
	 * 
	 * @param sid
	 * @return null表示未登陆
	 */
	byte[] getkey(String sid);

	void put(String sid, byte[] key);

	/**
	 * 获取存储到session中的用户信息
	 * 
	 * @return
	 */
	<T> T getUserObject(Class<T> clz);

	void flushSession();

	/**
	 * @param key
	 * @param sessionObj
	 */
	void setSession(String key, Object sessionObj);

	/**
	 * 更新session中的用户信息
	 * 
	 * @param sessionObj
	 */
	void updateSession(Object sessionObj);

	void removeSession();
}
