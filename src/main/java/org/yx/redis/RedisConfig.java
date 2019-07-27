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

import org.yx.exception.SumkException;
import org.yx.util.StringUtil;

public class RedisConfig {

	private int timeout = 3000;
	private String password = null;
	private int db = 0;
	private int tryCount = 3;
	private String hosts;
	private String masterName;

	public static final String DEFAULT = "default";
	public static final String SESSION = "session";
	public static final String COUNTER = "counter";

	public static RedisConfig create(String host) {
		return new RedisConfig(null, host);
	}

	public static RedisConfig createSentinel(String masterName, String... hosts) {
		return new RedisConfig(masterName, hosts);
	}

	/**
	 * 构造函数
	 * 
	 * @param masterName
	 *            Sentinel模式下才用到，非Sentinel模式传null就好
	 * @param hosts
	 *            redis地址，格式为ip:port,如果port为redis默认的6379，可以不填
	 */
	public RedisConfig(String masterName, String... hosts) {
		this.hosts = String.join(",", hosts);

		RedisParamter p = RedisParamter.create(this.hosts);
		if (p.hosts() == null || p.hosts().isEmpty()) {
			SumkException.throwException(23432565, "redis的hosts参数配置错误");
		}
		if (p.hosts().size() > 1) {
			this.masterName = masterName;
		}
	}

	public String masterName() {
		return this.masterName;
	}

	public String hosts() {
		return hosts;
	}

	public int getTimeout() {
		return timeout;
	}

	public RedisConfig setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public RedisConfig setPassword(String password) {
		this.password = password;
		return this;
	}

	public int getDb() {
		return db;
	}

	public RedisConfig setDb(int db) {
		this.db = db;
		return this;
	}

	public int getTryCount() {
		return tryCount;
	}

	public RedisConfig setTryCount(int tryCount) {
		this.tryCount = tryCount;
		return this;
	}

	@Override
	public String toString() {
		return "RedisConfig [masterName=" + masterName + ", hosts=" + hosts + ", db=" + db + ", password=" + password
				+ ", timeout=" + timeout + ", tryCount=" + tryCount + "]";
	}

	public String toSumkConfigValue() {
		StringBuilder sb = new StringBuilder();
		if (StringUtil.isNotEmpty(this.masterName)) {
			sb.append(this.masterName).append("-");
		}
		sb.append(hosts).append("#").append(db).append("#").append(password != null ? password : "").append("#")
				.append(timeout).append("#").append(tryCount);
		return sb.toString();
	}

}
