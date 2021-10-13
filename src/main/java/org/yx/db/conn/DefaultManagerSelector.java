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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.util.StringUtil;

public class DefaultManagerSelector implements DataSourceManagerSelector {

	/**
	 * factoryMap只能重新赋值，不能修改
	 */
	private Map<String, DataSourceManager> factoryMap = Collections.emptyMap();

	protected Map<String, String> aliasMap;

	private Function<String, DataSourceManager> managerFactory = DataSourceManagerImpl::new;

	public DefaultManagerSelector() {
		Map<String, String> configMap = AppInfo.subMap("s.alias.db.");
		if (configMap.size() > 0) {
			Map<String, String> temp = new HashMap<>();
			for (Map.Entry<String, String> entry : configMap.entrySet()) {
				String name = entry.getKey();
				String value = entry.getValue();
				if (name.isEmpty() || StringUtil.isEmpty(value)) {
					continue;
				}
				List<String> aliases = StringUtil.splitAndTrim(StringUtil.toLatin(value), Const.COMMA);
				for (String alias : aliases) {
					if (temp.putIfAbsent(alias, name) != null) {
						Logs.redis().error("redis别名{}重复了", name);
					}
				}
			}
			if (temp.size() > 0) {
				this.aliasMap = new HashMap<>(temp);
			}
		}
	}

	public void setManagerFactory(Function<String, DataSourceManager> managerFactory) {
		this.managerFactory = Objects.requireNonNull(managerFactory);
	}

	public synchronized DataSourceManager addManagerIfAbsent(String dbName, DataSourceManager factory) {
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
		}
		return old;
	}

	protected String getDbName(String dbName) {
		if (this.aliasMap == null) {
			return dbName;
		}
		return aliasMap.getOrDefault(dbName, dbName);
	}

	@Override
	public DataSourceManager select(String dbName) {
		dbName = getDbName(dbName);
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
					DataSourceManager pre = this.addManagerIfAbsent(dbName, factory);
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

	public Map<String, String> aliasMap() {
		return new HashMap<>(aliasMap);
	}

	public Set<String> dbNames() {
		return new HashSet<>(this.factoryMap.keySet());
	}
}
