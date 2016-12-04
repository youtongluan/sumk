package org.yx.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.yx.db.sql.MapedSql;
import org.yx.db.sql.SqlBuilder;
import org.yx.db.visit.DmlVisitor;
import org.yx.exception.SumkException;

public class RawDB {
	public static int execute(String sql, Object... params) {
		try {
			return DmlVisitor.visitor.visit(new RawSqlBuilder(sql, params));
		} catch (Exception e) {
			throw SumkException.create(e);
		}
	}
}

class RawSqlBuilder implements SqlBuilder {
	private String _sql;
	private List<Object> _params;

	public RawSqlBuilder(String sql, Object[] params) {
		this._sql = sql;
		if (params == null || params.length == 0) {
			this._params = new ArrayList<>();
			return;
		}
		this._params = Arrays.asList(params);
	}

	@Override
	public MapedSql toMapedSql() throws Exception {
		MapedSql ms = new MapedSql();
		ms.setSql(_sql);
		ms.addParams(_params);
		return ms;
	}

}
