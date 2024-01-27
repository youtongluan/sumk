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
import org.yx.log.Logs;
import org.yx.rpc.transport.TransportClient;
import org.yx.rpc.transport.Transports;

public class TransportClientHolder {
	private static ConcurrentMap<Host, TransportClient> sessions = new ConcurrentHashMap<>();

	public static void addClientIfAbsent(Host url, TransportClient s) {
		sessions.putIfAbsent(url, s);
	}

	public static boolean remove(Host url, TransportClient expect) {
		return sessions.remove(url, expect);
	}

	public static TransportClient getSession(Host url) {
		TransportClient obj = sessions.get(url);
		if (obj != null) {
			return obj;
		}
		TransportClient ses = Transports.factory().connect(url);
		TransportClient ses0 = sessions.putIfAbsent(url, ses);
		if (ses0 == null) {
			return ses;
		}
		ses.closeIfPossibble();
		return ses0;
	}

	public static Map<Host, TransportClient> view() {
		return Collections.unmodifiableMap(sessions);
	}

	public static synchronized void cleanReqSession() {
		Logs.rpc().debug("begin clean idle client transport");
		Map<Host, TransportClient> map = sessions;
		for (Host h : map.keySet()) {
			TransportClient session = map.get(h);
			if (session == null || !session.isIdle()) {
				continue;
			}
			Logs.rpc().info("remove idle client transport {}", session);

			map.remove(h, session);
			session.closeIfPossibble();
		}
	}
}
