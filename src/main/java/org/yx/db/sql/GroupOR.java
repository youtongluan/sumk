package org.yx.db.sql;

import java.util.List;

public class GroupOR extends AbstractOperationGroup {

	public static GroupOR create() {
		return new GroupOR();
	}

	public GroupOR or(String name, Operation type, Object value) {
		return or(new ColumnOperation(name, type, value));
	}

	public GroupOR or(CompareOperation op) {
		this.addOperation(op);
		return this;
	}

	@Override
	public CharSequence buildSql(SelectBuilder select, List<Object> paramters) {
		return this.buildSql(select, paramters, " OR ");
	}

}
