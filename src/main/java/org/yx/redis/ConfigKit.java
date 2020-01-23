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
package org.yx.redis;

import java.util.ArrayList;
import java.util.List;

import org.yx.common.Host;

import redis.clients.jedis.Protocol;

public final class ConfigKit {
	public static List<Host> parseHosts(String host) {
		host = host.replace('　', ' ').replace('，', ',').replace('：', ':').replaceAll("\\s", "");
		String h = host;
		String[] hs = h.split(",");
		List<Host> hosts = new ArrayList<>(hs.length);
		for (String addr : hs) {
			if (addr.isEmpty()) {
				continue;
			}
			if (!addr.contains(":")) {
				hosts.add(Host.create(addr, Protocol.DEFAULT_PORT));
				continue;
			}
			hosts.add(Host.create(addr));
		}
		return hosts;
	}
}
