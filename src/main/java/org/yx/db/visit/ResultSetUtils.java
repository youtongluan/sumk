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
package org.yx.db.visit;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.db.sql.ColumnMeta;
import org.yx.db.sql.PojoMeta;
import org.yx.util.Assert;

public class ResultSetUtils {
	public static List<Map<String, Object>> toMapList(ResultSet rs) throws java.sql.SQLException {
		List<Map<String, Object>> list = new ArrayList<>(5);
		if (rs == null) {
			return list;
		}
		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount();
		Map<String, Object> rowData = new HashMap<>();
		while (rs.next()) {
			rowData = new HashMap<>(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);
		}
		return list;
	}

	public static List<Map<String, Object>> toMapList(ResultSet rs, PojoMeta pm) throws java.sql.SQLException {
		List<Map<String, Object>> list = new ArrayList<>(10);
		if (rs == null) {
			return list;
		}
		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount();
		Map<String, Object> rowData = new HashMap<>();
		while (rs.next()) {
			rowData = new HashMap<>(columnCount * 2);
			for (int i = 1; i <= columnCount; i++) {
				if (pm == null) {
					rowData.put(md.getColumnName(i).toLowerCase(), rs.getObject(i));
					continue;
				}
				ColumnMeta cm = pm.getByColumnDBName(md.getColumnName(i));
				Assert.notNull(cm, md.getColumnName(i) + " has no mapper");
				rowData.put(cm.getFieldName(), rs.getObject(i));
			}
			list.add(rowData);
		}
		rs.close();
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List toList(ResultSet rs) throws java.sql.SQLException {
		List list = new ArrayList<>(10);
		if (rs == null) {
			return list;
		}
		ResultSetMetaData md = rs.getMetaData();
		Assert.isTrue(md.getColumnCount() == 1, "result data column is " + md.getColumnCount() + ", not 1");
		while (rs.next()) {
			list.add(rs.getObject(1));
		}
		rs.close();
		return list;
	}
}
