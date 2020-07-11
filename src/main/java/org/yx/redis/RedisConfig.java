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

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.yx.util.Asserts;
import org.yx.util.StringUtil;

@SuppressWarnings("rawtypes")
public class RedisConfig extends GenericObjectPoolConfig {

	private final String hosts;
	private int db = 0;
	private int connectionTimeout = 5000;
	private int timeout = 2000;
	private String password;
	private int maxAttempts = 3;
	private String master;
	private String alias;

	/**
	 * 1 cluster 2 sentinel 其它是普通
	 */
	private String type;

	public RedisConfig(String host) {
		Asserts.notEmpty(host, "redis host cannot be empty");
		this.hosts = StringUtil.toLatin(host.trim());

		setTestWhileIdle(true);
		setMinEvictableIdleTimeMillis(60000);
		setTimeBetweenEvictionRunsMillis(30000);
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String hosts() {
		return hosts;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDb() {
		return db;
	}

	public void setDb(int db) {
		this.db = db;
	}

	public int getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	@Override
	public String toString() {
		return "hosts=" + hosts + ", db=" + db + ", password=" + password + ", timeout=" + timeout + ", maxAttempts="
				+ maxAttempts + " , " + super.toString();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
