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
import java.util.Collections;
import java.util.List;

import org.yx.common.Host;
import org.yx.util.Asserts;

import redis.clients.jedis.Protocol;

public class RedisParamter {
	static final int DEFAULT_TRY_COUNT = 3;
	static final int DEFAULT_TIMEOUT = 3000;

	public static RedisParamter create(String host) {
		return new RedisParamter(host);
	}

	private int timeout = DEFAULT_TIMEOUT;
	private String password = null;
	private int db = 0;
	private int tryCount = DEFAULT_TRY_COUNT;
	private final List<Host> hosts;
	private String masterName;

	public RedisParamter(String host) {
		Asserts.notEmpty(host, "redis host cannot be empty");
		host = host.replace('　', ' ').replace('，', ',').replace('：', ':').replaceAll("\\s", "");
		int index = host.indexOf('-');
		if (index > -1) {
			this.masterName = host.substring(0, index);
			host = host.substring(index + 1);
		}
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
		if (hosts.size() == 1) {
			this.hosts = Collections.singletonList(hosts.get(0));
		} else {
			hosts.sort(null);
			this.hosts = Collections.unmodifiableList(hosts);
		}
	}

	public String masterName() {
		return this.masterName;
	}

	public List<Host> hosts() {
		return hosts;
	}

	public int getTimeout() {
		return timeout;
	}

	public RedisParamter setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public RedisParamter setPassword(String password) {
		this.password = password;
		return this;
	}

	public int getDb() {
		return db;
	}

	public RedisParamter setDb(int db) {
		this.db = db;
		return this;
	}

	public int getTryCount() {
		return tryCount;
	}

	public RedisParamter setTryCount(int tryCount) {
		this.tryCount = tryCount;
		return this;
	}

	@Override
	public String toString() {
		return "RedisParamter [masterName=" + masterName + ", hosts=" + hosts + ", db=" + db + ", password=" + password
				+ ", timeout=" + timeout + ", tryCount=" + tryCount + "]";
	}

}
