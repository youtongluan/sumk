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
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.sql.DataSource;

import org.yx.db.DBType;
import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.util.S;

public final class DataSourceManagerImpl implements DataSourceManager {

	private WeightedDataSourceRoute read;
	private WeightedDataSourceRoute write;

	private final String db;

	public DataSourceManagerImpl(String dbName) {
		this.db = Objects.requireNonNull(dbName);
		try {
			this.parseDatasource();
		} catch (Exception e) {
			Logs.db().error(e.getMessage(), e);
			throw new SumkException(1432543, dbName + "创建DataSourceManager" + "失败");
		}
	}

	public DataSourceManagerImpl(String dbName, WeightedDataSourceRoute write, WeightedDataSourceRoute read)
			throws Exception {
		this.db = Objects.requireNonNull(dbName);
		this.write = write;
		this.read = read;
	}

	@Override
	public String status() {
		Set<DataSource> set = new HashSet<>();
		set.addAll(this.read.allDataSource());
		set.addAll(this.write.allDataSource());
		Map<String, Map<String, Integer>> statusMap = new HashMap<>();
		for (DataSource datasource : set) {
			statusMap.put(datasource.toString(), DSFactory.factory().status(datasource));
		}
		return S.json.toJson(statusMap);
	}

	@Override
	public void destroy() {

	}

	private void parseDatasource() throws Exception {
		if (this.write != null || this.read != null) {
			Logs.db().info("{} has inited datasource", this.db);
			return;
		}
		Map<DBType, WeightedDataSourceRoute> map = DSRouteFactory.create(this.db);
		this.write = map.get(DBType.WRITE);
		this.read = map.get(DBType.READ);
	}

	@Override
	public String toString() {
		return "datasource[" + db + "] write=" + write + " ,read=" + read;
	}

	@Override
	public SumkDataSource writeDataSource() {
		return this.write.datasource();
	}

	@Override
	public SumkDataSource readDataSource() {
		return this.read.datasource();
	}

}
