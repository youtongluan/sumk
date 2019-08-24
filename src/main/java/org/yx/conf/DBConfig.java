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
package org.yx.conf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.yx.db.DBType;
import org.yx.db.conn.DSFactory;
import org.yx.db.conn.SumkDataSource;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.util.Assert;
import org.yx.util.S;

public class DBConfig {

	DBType type = DBType.ANY;
	int weight = 0;
	int read_weight = 0;
	Map<String, String> properties;

	public DBConfig() {
		properties = new HashMap<>();
		properties.put("driverClassName", "com.mysql.jdbc.Driver");

		properties.put("maxTotal", "30");
		properties.put("minIdle", "2");
		properties.put("maxIdle", "10");
		properties.put("maxWaitMillis", "10000");
		properties.put("testOnBorrow", "false");
		properties.put("testOnReturn", "false");
		properties.put("testWhileIdle", "true");
		properties.put("removeAbandonedOnBorrow", "false");
		properties.put("removeAbandonedOnMaintenance", "true");
		properties.put("removeAbandonedTimeout", "30");
		properties.put("logAbandoned", "true");
		properties.put("timeBetweenEvictionRunsMillis", "30000");
		properties.put("softMinEvictableIdleTimeMillis", "60000");

		properties.put("logExpiredConnections", "false");
		properties.put("poolPreparedStatements", "false");
		properties.put("defaultAutoCommit", "false");

	}

	public String getProperty(String name) {
		return this.properties.get(name);
	}

	public void setProperties(Map<String, String> p) throws Exception {
		Set<String> set = p.keySet();
		for (String key : set) {
			String v = p.get(key);
			if (v == null || v.isEmpty()) {
				Log.get("sumk.db.config").debug("db config {}={} isempty,ignore it.", key, v);
				continue;
			}
			switch (key) {
			case "type":
				this.type = parseFromConfigFile(v);
				break;
			case "weight":
				this.weight = Integer.parseInt(v);
				break;
			case "password":

				if (AppInfo.getBoolean("sumk.db.password.encry", false)) {
					byte[] bs = S.base64.decode(v.getBytes());
					v = new String(S.cipher.decrypt(bs, new byte[] { 121, 111, 117, 116, 111, 110, 103, 108, 117, 97,
							110, 64, 115, 117, 109, 107 }));
				}
				properties.put(key, v);
				break;
			case "read_weight":
				this.read_weight = Integer.parseInt(v);
				break;
			default:
				properties.put(key, v);
				break;
			}

		}
	}

	public SumkDataSource createDS(String name) throws Exception {
		Assert.isTrue(this.valid(), "url,username,password,type should not be null");
		return DSFactory.create(name, type, properties);
	}

	public boolean valid() {
		return this.type != null && properties.get("url") != null && properties.get("username") != null
				&& properties.get("password") != null;
	}

	public DBType getType() {
		return type;
	}

	public void setType(DBType type) {
		this.type = type;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getRead_weight() {
		return read_weight;
	}

	public void setRead_weight(int read_weight) {
		this.read_weight = read_weight;
	}

	@Override
	public String toString() {
		return "DBConfig [type=" + type + ", weight=" + weight + ", read_weight=" + read_weight + ", properties="
				+ properties + "]";
	}

	private static DBType parseFromConfigFile(String type) {
		String type2 = type.toLowerCase();
		switch (type2) {
		case "w":
		case "write":
			return DBType.WRITE;
		case "r":
		case "read":
		case "readonly":
			return DBType.READ;
		case "wr":
		case "rw":
		case "any":
			return DBType.ANY;
		default:
			throw new SumkException(2342312, type + " is not correct db type");
		}
	}

}
