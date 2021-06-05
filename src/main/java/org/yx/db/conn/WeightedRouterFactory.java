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
import java.util.Arrays;
import java.util.List;

import org.yx.bean.IOC;
import org.yx.common.route.Router;
import org.yx.common.route.Routes;
import org.yx.common.route.WeightedServer;
import org.yx.conf.AppInfo;
import org.yx.db.enums.DBType;
import org.yx.exception.SumkException;
import org.yx.log.Logs;

public class WeightedRouterFactory implements RouterFactory {

	@Override
	public List<Router<SumkDataSource>> create(String dbName) throws Exception {
		List<DBConfig> configs = parseDBConfig(dbName);
		List<WeightedServer<SumkDataSource>> readDSList = new ArrayList<>(1);
		List<WeightedServer<SumkDataSource>> writeDSList = new ArrayList<>(1);

		for (DBConfig dc : configs) {
			SumkDataSource ds = DSFactory.create(dbName, dc.index, dc.type, dc.properties);
			if (ds.getType().isWritable()) {
				WeightedServer<SumkDataSource> w = new WeightedDataSource(ds);
				w.setWeight(dc.getWeight() > 0 ? dc.getWeight() : 1);
				writeDSList.add(w);
				if (ds.getType() == DBType.ANY) {
					WeightedServer<SumkDataSource> r = new WeightedDataSource(ds);
					r.setWeight(dc.getReadWeight() > 0 ? dc.getReadWeight() : 1);
					readDSList.add(r);
				}
			} else if (ds.getType().isReadable()) {
				WeightedServer<SumkDataSource> r = new WeightedDataSource(ds);
				int w = dc.getReadWeight() > 0 ? dc.getReadWeight() : dc.getWeight();
				r.setWeight(w > 0 ? w : 1);
				readDSList.add(r);
			}
		}

		Router<SumkDataSource> read = createWeightedRouter(dbName, DBType.READ_PREFER, readDSList);
		Router<SumkDataSource> write = createWeightedRouter(dbName, DBType.WRITE, writeDSList);
		return Arrays.asList(write, read);
	}

	protected Router<SumkDataSource> createWeightedRouter(String name, DBType type,
			List<WeightedServer<SumkDataSource>> wds) {
		if (wds.isEmpty()) {
			if (AppInfo.getBoolean("sumk.db.empty.allow", false)) {
				Logs.db().warn("you have not config any read datasource for [{}]", name);
			} else {
				throw new SumkException(83587871, "you have not config " + type + " datasource for " + name);
			}
		}
		return Routes.createWeightedRouter(wds);
	}

	protected List<DBConfig> parseDBConfig(String db) throws Exception {
		List<DBConfigFactory> factorys = IOC.getBeans(DBConfigFactory.class);
		for (DBConfigFactory factory : factorys) {
			List<DBConfig> configs = factory.create(db);
			if (configs != null) {
				return configs;
			}
		}
		throw new SumkException(83587875, "no DBConfigFactory for " + db);
	}

}
