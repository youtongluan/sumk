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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.yx.db.DBGson;
import org.yx.db.DBType;
import org.yx.exception.SumkException;
import org.yx.log.Log;

public class ConnectionFactoryImpl implements ConnectionFactory {

	private WeightedDataSourceRoute read;
	private WeightedDataSourceRoute write;

	private String db;

	public ConnectionFactoryImpl(String dbName) throws Exception {
		this.db = dbName;
		this.parseDatasource();
	}

	public ConnectionFactoryImpl(String dbName, WeightedDataSourceRoute write, WeightedDataSourceRoute read)
			throws Exception {
		this.db = dbName;
		this.write = write;
		this.read = read;
	}

	public String status() {
		Set<DataSource> set = new HashSet<>();
		set.addAll(this.read.allDataSource());
		set.addAll(this.write.allDataSource());
		Map<String, Map<String, Integer>> statusMap = new HashMap<>();
		for (DataSource datasource : set) {
			if (!BasicDataSource.class.isInstance(datasource)) {
				Log.get(this.getClass(), 25345).info("ds.class({}) is not instance form BasicDataSource",
						datasource.getClass().getName());
				continue;
			}
			@SuppressWarnings("resource")
			BasicDataSource ds = (BasicDataSource) datasource;
			Map<String, Integer> map = new HashMap<>();
			map.put("active", ds.getNumActive());
			map.put("idle", ds.getNumIdle());
			map.put("minIdle", ds.getMinIdle());
			map.put("maxIdle", ds.getMaxIdle());
			map.put("maxTotal", ds.getMaxTotal());
			statusMap.put(ds.toString(), map);
		}
		return DBGson.toJson(statusMap);
	}

	public void destroy() {

	}

	private void parseDatasource() throws Exception {
		if (this.write != null || this.read != null) {
			Log.get(this.getClass(), 34534543).info("{} has init datasource", this.db);
			return;
		}
		Map<DBType, WeightedDataSourceRoute> map = DataSourceFactory.create(this.db);
		this.write = map.get(DBType.WRITE);
		this.read = map.get(DBType.READ);
	}

	public Connection getConnection(DBType type, Connection writeConn) {
		if (!type.isWritable()) {
			try {
				DataSource ds = this.read.datasource();
				if (writeConn != null && DataSourceWraper.class.isInstance(ds)) {
					@SuppressWarnings("resource")
					DataSourceWraper wrapper = (DataSourceWraper) ds;

					wrapper.readConnection(writeConn);
				}
				return ds.getConnection();
			} catch (SQLException e) {
				SumkException.throwException(100001, "获取" + db + "读连接失败", e);
			}
		}
		try {
			return this.write.datasource().getConnection();
		} catch (SQLException e) {
			SumkException.throwException(100001, "获取" + db + "写连接失败", e);
		}
		return null;
	}

	@Override
	public String toString() {
		return "datasource[" + db + "] write=" + write + " ,read=" + read;
	}

}
