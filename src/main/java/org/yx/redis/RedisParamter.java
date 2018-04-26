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

import redis.clients.jedis.Protocol;

public class RedisParamter {
	final static int DEFAULT_TRY_COUNT = 2;
	final static int DEFAULT_Timeout = 3000;

	public static RedisParamter create(String ip) {
		return new RedisParamter(ip);
	}

	public static RedisParamter create(String ip, int port) {
		RedisParamter p = new RedisParamter(ip);
		p.setPort(port);
		return p;
	}

	private int timeout = DEFAULT_Timeout;
	private String password = null;
	private String ip;
	private int db = 0;
	private int tryCount = DEFAULT_TRY_COUNT;
	private int port = Protocol.DEFAULT_PORT;

	public RedisParamter(String ip) {
		super();
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public RedisParamter setPort(int port) {
		this.port = port;
		return this;
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

}
