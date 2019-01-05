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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.util.Assert;

public class ConnectionFactorys {

	private static final Map<String, ConnectionFactory> factoryMap = new ConcurrentHashMap<>();

	public static ConnectionFactory get(String dbName) {
		try {
			Assert.hasText(dbName, "db name can not be empty");
			dbName = dbName.trim();
			ConnectionFactory factory = factoryMap.get(dbName);
			if (factory != null) {
				return factory;
			}
			synchronized (ConnectionFactorys.class) {
				factory = factoryMap.get(dbName);
				if (factory != null) {
					return factory;
				}
				factory = new ConnectionFactoryImpl(dbName);
				factoryMap.put(dbName, factory);
				Log.get("sumk.db").debug("create {}", factory);
			}
			return factory;
		} catch (Exception e) {
			SumkException.throwException(100234325, "create factory failed", e);
			return null;
		}
	}

	public static void put(String dbName, ConnectionFactory factory) {
		factoryMap.put(dbName, factory);
	}

	public static void remove(String dbName) throws Exception {
		Assert.hasText(dbName, "db name can not be empty");
		dbName = dbName.trim();
		ConnectionFactory factory = factoryMap.get(dbName);
		if (factory == null) {
			return;
		}
		ConnectionFactory old = factoryMap.remove(dbName);
		if (old != null) {
			old.destroy();
		}
	}

}
