package org.yx.db.visit;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultSetUtils {
	public static List<Map<String, Object>> toMapList(ResultSet rs) throws java.sql.SQLException {
		List<Map<String, Object>> list = new ArrayList<>();
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
}
