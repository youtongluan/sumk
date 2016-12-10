package org.yx.db.visit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.yx.db.DBType;
import org.yx.db.conn.ConnectionPool;
import org.yx.db.conn.EventLane;
import org.yx.db.sql.MapedSql;
import org.yx.db.sql.SqlBuilder;
import org.yx.log.Log;

public class DmlVisitor implements SumkDbVisitor<Integer> {

	public static DmlVisitor visitor = new DmlVisitor();

	@Override
	public Integer visit(SqlBuilder builder) throws Exception {
		MapedSql maped = builder.toMapedSql();
		if (Log.get("sumk.SQL.raw").isEnable(Log.ON)) {
			Log.get("sumk.SQL.raw").trace(maped);
		}
		Connection conn = ConnectionPool.get().connection(DBType.WRITE);
		PreparedStatement statement = conn.prepareStatement(maped.getSql());
		List<Object> params = maped.getParamters();
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				statement.setObject(i + 1, params.get(i));
			}
		}
		if (Log.get("sumk.SQL").isEnable(Log.DEBUG)) {
			Log.get("sumk.SQL").debug(" <== {}", statement);
		}
		int ret = statement.executeUpdate();
		EventLane.pubuish(conn, maped.getEvent());
		return ret;
	}

}
