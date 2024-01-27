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
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.yx.common.util.kit.Asserts;
import org.yx.conf.SimpleBeanUtil;
import org.yx.exception.SumkException;
import org.yx.log.Logs;

public class DBCPDataSourceFactory implements DataSourceFactory {

	private static final Map<String, String> DEFAULT_PROPERTIES = new HashMap<>();

	static {
		DEFAULT_PROPERTIES.put("driverClassName", "com.mysql.jdbc.Driver");

		DEFAULT_PROPERTIES.put("maxTotal", "50");
		DEFAULT_PROPERTIES.put("minIdle", "5");
		DEFAULT_PROPERTIES.put("maxIdle", "30");
		DEFAULT_PROPERTIES.put("maxWaitMillis", "10000");
		DEFAULT_PROPERTIES.put("testOnBorrow", "false");
		DEFAULT_PROPERTIES.put("testOnReturn", "false");
		DEFAULT_PROPERTIES.put("testWhileIdle", "true");
		DEFAULT_PROPERTIES.put("removeAbandonedOnBorrow", "false");
		DEFAULT_PROPERTIES.put("removeAbandonedOnMaintenance", "true");
		DEFAULT_PROPERTIES.put("removeAbandonedTimeout", "300");
		DEFAULT_PROPERTIES.put("logAbandoned", "true");
		DEFAULT_PROPERTIES.put("timeBetweenEvictionRunsMillis", "30000");
		DEFAULT_PROPERTIES.put("softMinEvictableIdleTimeMillis", "60000");

		DEFAULT_PROPERTIES.put("logExpiredConnections", "false");
		DEFAULT_PROPERTIES.put("poolPreparedStatements", "false");

	}

	private boolean valid(Map<String, String> properties) {
		return properties.get("url") != null && properties.get("username") != null
				&& properties.get("password") != null;
	}

	@Override
	public DataSource create(Map<String, String> properties, boolean readonly) {
		Asserts.requireTrue(this.valid(properties), "url,username,password should not be null");
		BasicDataSource basic = new BasicDataSource();
		try {
			Map<String, String> map = new HashMap<>(DEFAULT_PROPERTIES);
			if (properties != null && properties.size() > 0) {
				map.putAll(properties);
			}
			SimpleBeanUtil.copyProperties(basic, map);
			basic.setDefaultReadOnly(readonly);
		} catch (Exception e) {
			Logs.db().error(e.getMessage(), e);
			throw new SumkException(23434, e.getMessage(), e);
		}
		return basic;
	}

	@Override
	public Map<String, Number> status(final DataSource datasource) {
		BasicDataSource ds = null;
		if (datasource instanceof BasicDataSource) {
			ds = (BasicDataSource) datasource;
		} else {
			try {
				ds = datasource.unwrap(BasicDataSource.class);
			} catch (Exception e) {
				Logs.db().error(e.getLocalizedMessage(), e);
			}
		}
		if (ds == null) {
			Logs.db().info("ds.class({}) is not instance from BasicDataSource", datasource.getClass().getName());
			return Collections.emptyMap();
		}

		Map<String, Number> map = new LinkedHashMap<>();
		map.put("active", ds.getNumActive());
		map.put("idle", ds.getNumIdle());
		map.put("minIdle", ds.getMinIdle());
		map.put("maxIdle", ds.getMaxIdle());
		map.put("maxTotal", ds.getMaxTotal());
		return map;
	}

}
