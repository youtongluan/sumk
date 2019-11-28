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

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import org.yx.exception.SumkException;
import org.yx.util.Asserts;

public final class DataSources {

	private static final ConcurrentMap<String, DataSourceManager> factoryMap = new ConcurrentHashMap<>();
	private static Function<String, DataSourceManager> managerFactory = name -> new DataSourceManagerImpl(name);

	public static void setManagerFactory(Function<String, DataSourceManager> managerFactory) {
		DataSources.managerFactory = Objects.requireNonNull(managerFactory);
	}

	public static SumkDataSource writeDataSource(String dbName) {
		return getManager(dbName).writeDataSource();
	}

	public static SumkDataSource readDataSource(String dbName) {
		return getManager(dbName).readDataSource();
	}

	public static DataSourceManager getManager(String dbName) {
		try {
			Asserts.hasText(dbName, "db name can not be empty");
			dbName = dbName.trim();
			DataSourceManager factory = factoryMap.get(dbName);
			if (factory != null) {
				return factory;
			}
			synchronized (DataSources.class) {
				factory = factoryMap.get(dbName);
				if (factory != null) {
					return factory;
				}
				factory = managerFactory.apply(dbName);
				factoryMap.put(dbName, factory);
			}
			return factory;
		} catch (Exception e) {
			throw new SumkException(100234325, "create factory failed", e);
		}
	}

	public static synchronized void put(String dbName, DataSourceManager factory) {
		factoryMap.put(dbName, factory);
	}

	public static void remove(String dbName) throws Exception {
		Asserts.hasText(dbName, "db name can not be empty");
		dbName = dbName.trim();
		DataSourceManager factory = factoryMap.get(dbName);
		if (factory == null) {
			return;
		}
		DataSourceManager old = factoryMap.remove(dbName);
		if (old != null) {
			old.destroy();
		}
	}

}
