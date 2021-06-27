package org.yx.db.sql;

import java.util.Objects;

public class ColumnOperation {
	final String name;
	final Operation type;
	final Object value;

	public ColumnOperation(String name, Operation type, Object value) {
		this.name = Objects.requireNonNull(name);
		this.type = Objects.requireNonNull(type);
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Operation getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public boolean isSameOperation(ColumnOperation b) {
		return this.name.equals(b.name) && this.type == b.type;
	}
}
