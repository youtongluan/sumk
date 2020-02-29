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
import org.yx.log.Logs;

public class DefaultManagerContainer implements Function<String, DataSourceManager> {

	private final ConcurrentMap<String, DataSourceManager> factoryMap = new ConcurrentHashMap<>();

	private Function<String, DataSourceManager> managerFactory = name -> new DataSourceManagerImpl(name);

	public void setManagerFactory(Function<String, DataSourceManager> managerFactory) {
		this.managerFactory = Objects.requireNonNull(managerFactory);
	}

	public void put(String dbName, DataSourceManager factory) {
		factoryMap.putIfAbsent(dbName, factory);
	}

	public void remove(String dbName) throws Exception {
		DataSourceManager old = factoryMap.remove(dbName);
		if (old != null) {
			old.destroy();
		}
	}

	@Override
	public DataSourceManager apply(String dbName) {
		DataSourceManager factory = factoryMap.get(dbName);
		if (factory != null) {
			return factory;
		}
		try {
			for (int i = 0; i < 3; i++) {
				factory = factoryMap.computeIfAbsent(dbName, managerFactory);
				if (factory != null) {
					Logs.db().info("create dataSource manager of {}", dbName);
					return factory;
				}
				factory = factoryMap.get(dbName);
				if (factory != null) {
					return factory;
				}
			}
			throw new SumkException(100234321, "get DataSourceManager [" + dbName + "] failed");
		} catch (Exception e) {
			throw new SumkException(100234325, "create DataSourceManager [" + dbName + "] failed", e);
		}
	}

}
