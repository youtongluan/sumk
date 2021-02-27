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

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.yx.common.Host;

public class ReqSessionHolder {
	private static ConcurrentMap<Host, ReqSession> sessions = new ConcurrentHashMap<>();

	public static void addClientIfAbsent(Host url, ReqSession s) {
		sessions.putIfAbsent(url, s);
	}

	public static boolean remove(Host url, ReqSession expect) {
		return sessions.remove(url, expect);
	}

	public static ReqSession getSession(Host url) {
		ReqSession obj = sessions.get(url);
		if (obj != null) {
			return obj;
		}
		ReqSession ses = new ReqSession(url);
		ReqSession ses0 = sessions.putIfAbsent(url, ses);
		if (ses0 == null) {
			return ses;
		}
		ses.closeOnFlush();
		return ses0;
	}

	public static Map<Host, ReqSession> view() {
		return Collections.unmodifiableMap(sessions);
	}

}
