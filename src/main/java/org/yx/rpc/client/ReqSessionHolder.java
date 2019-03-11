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
package org.yx.rpc.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.common.Host;

public class ReqSessionHolder {
	private static Map<Host, Object> map = new ConcurrentHashMap<>();

	public static void addClient(Host url, ReqSession s) {
		map.putIfAbsent(url, s);
	}

	public static ReqSession getSession(Host url) {
		Object obj = map.get(url);
		if (obj == null) {
			ReqSession ses = createSession(url);
			Object ses0 = map.putIfAbsent(url, ses);
			if (ses0 != null) {
				ses.close();
				return _getSession(ses0);
			}
			return ses;
		}
		return _getSession(obj);
	}

	@SuppressWarnings("unchecked")
	public static ReqSession _getSession(Object obj) {
		if (ReqSession.class.isInstance(obj)) {
			return (ReqSession) obj;
		}

		return ((List<ReqSession>) obj).get(0);
	}

	private static ReqSession createSession(Host url) {
		return new ReqSession(url);
	}
}
