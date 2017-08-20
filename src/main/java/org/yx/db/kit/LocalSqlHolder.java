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
package org.yx.db.kit;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.yx.bean.Loader;
import org.yx.conf.AppInfo;
import org.yx.conf.MultiResourceLoader;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.util.Assert;
import org.yx.util.DateUtil;

public class LocalSqlHolder {
	private static Map<String, String> SQLS = new HashMap<>();
	static {
		try {
			MultiResourceLoader loader = createLoader();
			loadSql(loader);
			startListen(loader);
		} catch (Throwable e) {
			Log.printStack(e);
		}
	}

	public static String findSql(String name) {
		String sql = SQLS.get(name);
		if (sql == null) {
			SumkException.throwException(64342451, "sql [" + name + "] can not found ");
		}
		return sql;
	}

	private static void startListen(MultiResourceLoader loader) {
		loader.startListen(load -> {
			try {
				Log.get("sumk.sql").info("local sql changed at {}", DateUtil.toDateTimeString(new Date()));
				loadSql(load);
			} catch (Exception e) {
				Log.printStack(e);
			}
		});
	}

	private static void loadSql(MultiResourceLoader loader) throws Exception {
		Map<String, InputStream> inputMap = loader.openInputs(null);
		Map<String, String> sqlMap = new HashMap<>();
		if (inputMap != null && inputMap.size() > 0) {
			inputMap.forEach((name, in) -> {
				try {
					Map<String, String> map = SqlUtil.loadMap(in);
					if (map == null || map.isEmpty()) {
						return;
					}
					map.forEach((key, sql) -> {
						Log.get("sumk.db.sql").debug("SQL: {} -- {}", name, key);
						if (sqlMap.put(key, sql) != null) {
							SumkException.throwException(435436, name + "-" + key + " is duplicate");
						}
					});
				} catch (IOException e) {
					Log.printStack(e);
				}
			});
		}
		SQLS = sqlMap;
	}

	private static MultiResourceLoader createLoader() throws Exception {
		String resourceFactory = AppInfo.get("sumk.db.sql.loader", LocalSqlLoader.class.getName());
		Class<?> factoryClz = Loader.loadClass(resourceFactory);
		Assert.isTrue(MultiResourceLoader.class.isAssignableFrom(factoryClz),
				resourceFactory + " should extend from MultiResourceLoader");
		return (MultiResourceLoader) factoryClz.newInstance();
	}
}
