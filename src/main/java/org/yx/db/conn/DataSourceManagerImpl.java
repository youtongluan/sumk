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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import org.yx.bean.IOC;
import org.yx.common.route.Router;
import org.yx.conf.Const;
import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.util.S;

public final class DataSourceManagerImpl implements DataSourceManager {

	private Router<SumkDataSource> read;
	private Router<SumkDataSource> write;

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

	public DataSourceManagerImpl(String dbName, Router<SumkDataSource> write, Router<SumkDataSource> read)
			throws Exception {
		this.db = Objects.requireNonNull(dbName);
		this.write = write;
		this.read = read;
	}

	@Override
	public String status() {
		Map<String, SumkDataSource> map = new TreeMap<>();
		for (SumkDataSource datasource : this.allDataSources()) {
			map.put(datasource.toString(), datasource);
		}
		StringBuilder sb = new StringBuilder(100);
		for (SumkDataSource datasource : map.values()) {
			sb.append(datasource).append(" = ").append(S.json().toJson(datasource.status())).append(Const.LN);
		}
		return sb.toString();
	}

	@Override
	public void destroy() {
		for (SumkDataSource ds : this.allDataSources()) {
			ds.close();
		}
	}

	private void parseDatasource() throws Exception {
		if (this.write != null || this.read != null) {
			Logs.db().info("{} has inited datasource", this.db);
			return;
		}
		RouterFactory factory = IOC.get(RouterFactory.class);
		if (factory == null) {
			factory = new WeightedRouterFactory();
		}
		List<Router<SumkDataSource>> container = factory.create(this.db);
		this.write = container.get(0);
		this.read = container.get(1);
	}

	@Override
	public String toString() {
		return "datasource[" + db + "] write=" + write + " ,read=" + read;
	}

	@Override
	public SumkDataSource writeDataSource() {
		return this.write.select();
	}

	@Override
	public SumkDataSource readDataSource() {
		return this.read.select();
	}

	@Override
	public Set<SumkDataSource> allDataSources() {
		Set<SumkDataSource> set = new HashSet<>();
		set.addAll(this.read.allSources());
		set.addAll(this.write.allSources());
		return set;
	}

}
