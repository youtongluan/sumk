package org.yx.db.dao;

public enum ColumnType {
	NORMAL(0), ID_DB(1), ID_CACHE(2), ID_BOTH(3);

	public boolean accept(ColumnType b) {
		if (b == null) {
			return false;
		}
		return this == b || (this.value & b.value) != 0;
	}

	private int value;

	private ColumnType(int b) {
		this.value = (byte) b;
	}

}
