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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.yx.conf.Const;
import org.yx.db.sql.DBSettings;
import org.yx.exception.SumkException;
import org.yx.log.Logs;

public class DefaultManagerSelector implements Function<String, DataSourceManager> {

	/**
	 * 这个属性是不可变的
	 */
	private Map<String, DataSourceManager> factoryMap = Collections.emptyMap();

	private Function<String, DataSourceManager> managerFactory = name -> new DataSourceManagerImpl(name);

	public void setManagerFactory(Function<String, DataSourceManager> managerFactory) {
		this.managerFactory = Objects.requireNonNull(managerFactory);
	}

	public synchronized DataSourceManager addManager(String dbName, DataSourceManager factory) {
		Map<String, DataSourceManager> map = new HashMap<>(this.factoryMap);
		DataSourceManager pre = map.putIfAbsent(dbName, factory);
		if (pre == null) {
			this.factoryMap = map;
		}
		return pre;
	}

	public synchronized DataSourceManager removeManager(String dbName) throws Exception {
		Map<String, DataSourceManager> map = new HashMap<>(this.factoryMap);
		DataSourceManager old = map.remove(dbName);
		if (old != null) {
			this.factoryMap = map;
			old.destroy();
		}
		return old;
	}

	protected String reform(String dbName) {
		if (Const.DEFAULT_DB_NAME.equals(dbName) && DBSettings.customDbName() != null) {
			return DBSettings.customDbName();
		}
		return dbName;
	}

	@Override
	public DataSourceManager apply(String dbName) {
		dbName = reform(dbName);
		DataSourceManager factory = factoryMap.get(dbName);
		if (factory != null) {
			return factory;
		}
		try {
			for (int i = 0; i < 3; i++) {
				synchronized (this) {
					if ((factory = factoryMap.get(dbName)) != null) {
						return factory;
					}
					factory = managerFactory.apply(dbName);
					if (factory == null) {
						continue;
					}
					Logs.db().info("create dataSource manager: {}", dbName);
					DataSourceManager pre = this.addManager(dbName, factory);
					if (pre == null) {
						return factory;
					}
					Logs.db().info("create dataSource manager '{}' twice!!", dbName);
					factory.destroy();
					return pre;
				}
			}
			throw new SumkException(100234321, "get DataSourceManager [" + dbName + "] failed");
		} catch (Exception e) {
			throw new SumkException(100234325, "create DataSourceManager [" + dbName + "] failed", e);
		}
	}

}
