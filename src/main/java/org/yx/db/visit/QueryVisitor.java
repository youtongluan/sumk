package org.yx.db.visit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.db.DBType;
import org.yx.db.conn.ConnectionPool;
import org.yx.db.sql.ColumnMeta;
import org.yx.db.sql.MapedSql;
import org.yx.db.sql.PojoMeta;
import org.yx.db.sql.SelectBuilder;
import org.yx.db.sql.SqlBuilder;
import org.yx.log.Log;
import org.yx.util.Assert;

public class QueryVisitor implements SumkDbVisitor<List<Map<String, Object>>> {

	public static QueryVisitor visitor = new QueryVisitor();

	@Override
	public List<Map<String, Object>> visit(SqlBuilder builder) throws Exception {
		MapedSql maped = builder.toMapedSql();
		if (Log.get("SQL").isEnable(Log.DEBUG)) {
			Log.get("SQL").debug(maped);
		}
		Connection conn = ConnectionPool.get().connection(DBType.ANY);
		PreparedStatement statement = conn.prepareStatement(maped.getSql());
		List<Object> params = maped.getParamters();
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				statement.setObject(i + 1, params.get(i));
			}
		}
		if (Log.get("SQL").isEnable(Log.ON)) {
			Log.get("SQL").trace(statement);
		}
		ResultSet ret = statement.executeQuery();
		PojoMeta pm = null;
		if (SelectBuilder.class.isInstance(builder)) {
			pm = ((SelectBuilder) builder).getPojoMeta();
		}
		return toMapList(ret, pm);
	}

	/**
	 * 返回的是pojo类的字段名
	 * 
	 * @param rs
	 * @param pm
	 * @return
	 * @throws java.sql.SQLException
	 */
	public static List<Map<String, Object>> toMapList(ResultSet rs, PojoMeta pm) throws java.sql.SQLException {
		List<Map<String, Object>> list = new ArrayList<>();
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
					rowData.put(md.getColumnName(i), rs.getObject(i));
					continue;
				}
				ColumnMeta cm = pm.getByColumnDBName(md.getColumnName(i));
				Assert.notNull(cm, md.getColumnName(i) + " has no mapper");
				rowData.put(cm.getFieldName(), rs.getObject(i));
			}
			list.add(rowData);
		}
		return list;
	}
}
