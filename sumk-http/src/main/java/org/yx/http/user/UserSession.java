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

public interface UserSession {

	byte[] getEncryptKey(String sessionId);

	<T extends SessionObject> T getUserObject(String sessionId, Class<T> clz);

	<T extends SessionObject> T loadAndRefresh(String sessionId, Class<T> clz);

	boolean setSession(String sessionId, SessionObject sessionObj, byte[] key, boolean singleLogin);

	void removeSession(String sessionId);

	String sessionId(String userId);

	int localCacheSize();
}
