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

public interface UserSession {
	/**
	 * 获取当前的session的加密key
	 * 
	 * @param sid
	 * @return null表示未登陆
	 */
	byte[] getKey(String sid);

	void putKey(String sid, byte[] key);

	/**
	 * 获取存储到session中的用户信息
	 * 
	 * @return
	 */
	<T extends SessionObject> T getUserObject(Class<T> clz);

	void flushSession();

	/**
	 * @param key
	 * @param sessionObj
	 */
	void setSession(String key, SessionObject sessionObj);

	/**
	 * 更新session中的用户信息
	 * 
	 * @param sessionObj
	 */
	void updateSession(SessionObject sessionObj);

	void removeSession();
}
