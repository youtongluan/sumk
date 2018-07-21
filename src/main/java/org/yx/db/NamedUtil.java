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
package org.yx.db;

import java.util.List;
import java.util.Map;

import org.yx.db.kit.LocalSqlHolder;
import org.yx.exception.SumkException;

public class NamedUtil {

	public static int execute(String name, Map<String, Object> map) {
		String sql = LocalSqlHolder.findSql(name);
		return NamedDB.execute(sql, map);
	}

	public static List<Map<String, Object>> list(String name, Map<String, Object> map) {
		String sql = LocalSqlHolder.findSql(name);
		return NamedDB.list(sql, map);
	}

	public static List<?> singleColumnList(String name, Map<String, Object> map) {
		String sql = LocalSqlHolder.findSql(name);
		return NamedDB.singleColumnList(sql, map);
	}

	public static int count(String name, Map<String, Object> map) {
		String sql = LocalSqlHolder.findSql(name);
		return NamedDB.count(sql, map);
	}

	public static Map<String, Object> selectOne(String name, Map<String, Object> map) {
		List<Map<String, Object>> list = list(name, map);
		if (list == null || list.size() != 1) {
			SumkException.throwException(521343, name + " -- result is not only one");
		}
		return list.get(0);
	}

}
