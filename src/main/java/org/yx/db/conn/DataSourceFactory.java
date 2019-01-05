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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.bean.IOC;
import org.yx.conf.AppInfo;
import org.yx.conf.DBConfig;
import org.yx.conf.DBConfigFactory;
import org.yx.db.DBType;
import org.yx.exception.SumkException;
import org.yx.log.Log;

public class DataSourceFactory {

	public static Map<DBType, WeightedDataSourceRoute> create(String db) throws Exception {
		Map<String, DBConfig> configs = getConfigs(db);
		List<WeightedDS> readDSList = new ArrayList<>(1);
		List<WeightedDS> writeDSList = new ArrayList<>(1);

		for (String key : configs.keySet()) {
			DBConfig dc = configs.get(key);
			DataSourceWraper ds = dc.createDS(key);
			if (ds.getType().isWritable()) {
				ds.setDefaultReadOnly(false);
				WeightedDS w = new WeightedDS(ds);
				w.setWeight(dc.getWeight() > 0 ? dc.getWeight() : 1);
				writeDSList.add(w);
				if (ds.getType() == DBType.ANY) {
					WeightedDS r = new WeightedDS(ds);
					r.setWeight(dc.getRead_weight() > 0 ? dc.getRead_weight() : 1);
					readDSList.add(r);
				}
			} else if (ds.getType().isReadable()) {
				ds.setDefaultReadOnly(true);
				WeightedDS r = new WeightedDS(ds);
				int w = dc.getRead_weight() > 0 ? dc.getRead_weight() : dc.getWeight();
				r.setWeight(w > 0 ? w : 1);
				readDSList.add(r);
			}
		}

		if (readDSList.isEmpty()) {
			if (AppInfo.getBoolean("sumk.db.empty.allow", false)) {
				Log.get("sumk.db.conf").warn("you have not config any read datasource for [{}]", db);
			} else {
				SumkException.throwException(83587871, "you have not config read datasource for " + db);
			}
		}
		if (writeDSList.isEmpty()) {
			if (AppInfo.getBoolean("sumk.db.empty.allow", false)) {
				Log.get("sumk.db.conf").warn("you have not config any write datasource for [{}]", db);
			} else {
				SumkException.throwException(83587871, "you have not config write datasource for " + db);
			}
		}
		WeightedDataSourceRoute read = new WeightedDataSourceRoute(readDSList);
		WeightedDataSourceRoute write = new WeightedDataSourceRoute(writeDSList);
		Map<DBType, WeightedDataSourceRoute> poolMap = new HashMap<>();
		poolMap.put(DBType.READ, read);
		poolMap.put(DBType.WRITE, write);
		return poolMap;
	}

	/**
	 * @param db
	 * @return
	 * @throws Exception
	 */
	private static Map<String, DBConfig> getConfigs(String db) throws Exception {
		List<DBConfigFactory> factorys = IOC.getBeans(DBConfigFactory.class);
		for (DBConfigFactory factory : factorys) {
			Map<String, DBConfig> configs = factory.create(db);
			if (configs != null) {
				return configs;
			}
		}
		throw new SumkException(83587875, "no DBConfigFactory for " + db);
	}

}
