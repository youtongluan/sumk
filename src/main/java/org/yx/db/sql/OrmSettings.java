/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
package org.yx.db.sql;

import org.yx.conf.AppInfo;
import org.yx.db.annotation.ColumnType;
import org.yx.log.Log;

class OrmSettings {
	static boolean FAIL_IF_PROPERTY_NOT_MAPPED;
	static boolean FROM_CACHE;
	static boolean TO_CACHE;

	static ColumnType modifyByColumnType = ColumnType.ID_DB;

	static void register() {
		AppInfo.addObserver((a, b) -> {
			try {
				FAIL_IF_PROPERTY_NOT_MAPPED = AppInfo.getBoolean("sumk.sql.failIfPropertyNotMapped", true);
				FROM_CACHE = AppInfo.getBoolean("sumk.sql.fromCache", true);
				TO_CACHE = AppInfo.getBoolean("sumk.sql.toCache", true);
				String byType = AppInfo.get("sumk.orm.update.byType", "ID_DB");
				if ("ID_DB".equals(byType)) {
					modifyByColumnType = ColumnType.ID_DB;
				} else {
					modifyByColumnType = ColumnType.ID_CACHE;
				}
			} catch (Exception e) {
				Log.get("sumk.appInfo").info(e.getMessage(), e);
			}
		});
	}
}
