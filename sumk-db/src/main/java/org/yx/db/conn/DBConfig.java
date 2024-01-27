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
package org.yx.db.conn;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.yx.common.util.S;
import org.yx.conf.AppInfo;
import org.yx.db.enums.DBType;
import org.yx.db.sql.DBSettings;
import org.yx.exception.SumkException;
import org.yx.log.Logs;

public class DBConfig {

	final DBType type;
	final int weight;
	final int readWeight;
	final String index;
	final Map<String, String> properties;

	public DBConfig(String index, DBType type, int weight, int readWeight, Map<String, String> properties) {
		this.index = index;
		this.type = type;
		this.weight = weight;
		this.readWeight = readWeight;
		this.properties = properties;
	}

	public String getProperty(String name) {
		return this.properties.get(name);
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public String getIndex() {
		return index;
	}

	public DBType getType() {
		return type;
	}

	public int getWeight() {
		return weight;
	}

	public int getReadWeight() {
		return readWeight;
	}

	@Override
	public String toString() {
		return "DBConfig [type=" + type + ", weight=" + weight + ", readWeight=" + readWeight + ", index=" + index
				+ ", properties=" + properties + "]";
	}

	public static DBConfig create(String index, Map<String, String> p) throws Exception {
		DBType type = DBType.ANY;
		int weight = 0, readWeight = 0;
		Map<String, String> properties = new HashMap<>();

		Set<String> set = p.keySet();
		for (String key : set) {
			String v = p.get(key);
			if (v == null || v.isEmpty()) {
				Logs.db().debug("db config {}={} isempty,ignore it.", key, v);
				continue;
			}
			switch (key.toLowerCase()) {
			case "type":
				type = DBConfig.parseFromConfigFile(v);
				break;
			case "weight":
				weight = Integer.parseInt(v);
				break;
			case "password":

				if (AppInfo.getBoolean("sumk.db.password.encry", false)) {
					byte[] bs = S.base64().decode(v.getBytes());
					v = new String(S.cipher().decrypt(bs, DBSettings.getPasswordKey()));
				}
				properties.put(key, v);
				break;
			case "read_weight":
			case "readweight":
				readWeight = Integer.parseInt(v);
				break;
			default:
				properties.put(key, v);
				break;
			}
		}
		return new DBConfig(index, type, weight, readWeight, properties);
	}

	private static DBType parseFromConfigFile(String type) {
		String type2 = type.toLowerCase();
		switch (type2) {
		case "w":
		case "write":
			return DBType.WRITE;
		case "r":
		case "read":
			return DBType.READONLY;
		case "wr":
		case "rw":
		case "any":
			return DBType.ANY;
		default:
			throw new SumkException(2342312, type + " is not correct db type");
		}
	}

}
