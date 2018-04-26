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

import org.yx.conf.DBConfig;
import org.yx.conf.DBConfigUtils;
import org.yx.db.DBType;
import org.yx.util.Assert;

public class DataSourceFactory {

	public static Map<DBType, WeightedDataSourceRoute> create(String db) throws Exception {
		Map<String, Map<String, String>> hmap = parse(db);
		List<WeightedDS> readDSList = new ArrayList<>(4);
		List<WeightedDS> writeDSList = new ArrayList<>(4);

		for (String key : hmap.keySet()) {
			Map<String, String> p = hmap.get(key);
			DBConfig dc = new DBConfig();
			dc.setProperties(p);
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
		Assert.isTrue(readDSList.size() > 0, "you have not config any read datasource");
		Assert.isTrue(writeDSList.size() > 0, "you have not config any write datasource");
		WeightedDataSourceRoute read = new WeightedDataSourceRoute(readDSList);
		WeightedDataSourceRoute write = new WeightedDataSourceRoute(writeDSList);
		Map<DBType, WeightedDataSourceRoute> poolMap = new HashMap<>();
		poolMap.put(DBType.READ, read);
		poolMap.put(DBType.WRITE, write);
		return poolMap;
	}

	static Map<String, Map<String, String>> parse(String db) throws Exception {
		return DBConfigUtils.parseIni(DBConfigUtils.openConfig(db));
	}

}
