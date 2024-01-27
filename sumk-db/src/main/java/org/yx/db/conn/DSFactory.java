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

import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.yx.db.enums.DBType;
import org.yx.log.Logs;

public final class DSFactory {
	private static DataSourceFactory factory;
	static {
		try {

			factory = new DBCPDataSourceFactory();
		} catch (Throwable e) {
			Logger log = Logs.db();
			if (log.isInfoEnabled()) {
				log.error("cannot create dbcp2 factory," + e, e);
			} else {
				log.error("cannot create dbcp2 factory");
			}
		}
	}

	public static DataSourceFactory factory() {
		return factory;
	}

	public static void setFactory(DataSourceFactory factory) {
		DSFactory.factory = Objects.requireNonNull(factory);
	}

	public static SumkDataSource create(String name, String index, DBType type, Map<String, String> properties) {
		DataSource basic = factory.create(properties, !type.isWritable());
		Logs.db().debug("create ds: {}", basic);
		return new SumkDataSource(name, index, type, basic);
	}

}
