package org.yx.db.sql;

public enum Operation {
	BIG(">"), BIG_EQUAL(">="), LESS("<"), LESS_EQUAL("<="), LIKE(" LIKE "), NOT(" != "), IN("in");

	final String op;

	private Operation(String op) {
		this.op = op;
	}

	public String op() {
		return op;
	}

}
