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

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.yx.conf.SimpleBeanUtil;
import org.yx.exception.SumkException;
import org.yx.log.Log;

public class DBCPDataSourceManager implements DataSourceManager {

	@Override
	public DataSource create(Map<String, String> properties) {
		BasicDataSource basic = new BasicDataSource();
		try {
			SimpleBeanUtil.copyProperties(basic, properties);
		} catch (Exception e) {
			Log.get("sumk.db").error(e.getMessage(), e);
			SumkException.throwException(23434, e.getMessage(), e);
		}
		return basic;
	}

	@Override
	public Map<String, Integer> status(DataSource datasource) {
		BasicDataSource ds = null;
		try {
			ds = datasource.unwrap(BasicDataSource.class);
		} catch (Exception e) {
			Log.get("sumk.db").error(e.toString(), e);
		}
		if (ds == null) {
			Log.get("sumk.db").info("ds.class({}) is not instance form BasicDataSource",
					datasource.getClass().getName());
			return Collections.emptyMap();
		}

		Map<String, Integer> map = new HashMap<>();
		map.put("active", ds.getNumActive());
		map.put("idle", ds.getNumIdle());
		map.put("minIdle", ds.getMinIdle());
		map.put("maxIdle", ds.getMaxIdle());
		map.put("maxTotal", ds.getMaxTotal());
		return map;
	}

}
