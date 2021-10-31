package org.yx.db.sql;

import java.util.List;

import org.yx.util.CollectionUtil;

public class GroupAND extends AbstractOperationGroup {

	public static GroupAND create() {
		return new GroupAND();
	}

	public GroupAND and(String name, Operation type, Object value) {
		return and(new ColumnOperation(name, type, value));
	}

	public GroupAND and(CompareOperation op) {
		this.addOperation(op);
		return this;
	}

	@Override
	public CharSequence buildSql(SelectBuilder select, List<Object> paramters) {
		return this.buildSql(select, paramters, " AND ");
	}

	GroupAND unmodifyFirstLevel() {
		if (this.compare != null) {
			this.compare = CollectionUtil.unmodifyList(this.compare);
		}
		return this;
	}

}
