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
import org.yx.db.DBType;
import org.yx.log.Log;

public final class DSFactory {
	private static DataSourceManager manager;
	static {
		try {
			manager = new DBCPDataSourceManager();
		} catch (Exception e) {
			Logger log = Log.get("sumk.db.factory");
			if (log.isInfoEnabled()) {
				log.error("cannot create dbcp2 factory," + e, e);
			} else {
				log.error("cannot create dbcp2 factory");
			}
		}
	}

	public static DataSourceManager manager() {
		return manager;
	}

	public static void manager(DataSourceManager factory) {
		DSFactory.manager = Objects.requireNonNull(factory);
	}

	public static SumkDataSource create(String name, DBType type, Map<String, String> properties) {
		DataSource basic = manager.create(properties);
		Log.get("sumk.db.factory").debug("create ds: {}", basic);
		return new SumkDataSource(name, type, basic);
	}

}
