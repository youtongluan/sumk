package org.yx.db;

import org.yx.exception.SystemException;

public enum DBType {
	WRITE(true, false), READONLY(false, true), ANY(true, true);

	public static DBType parse(String type) {
		String type2 = type.toLowerCase();
		switch (type2) {
		case "w":
		case "write":
			return WRITE;
		case "r":
		case "read":
		case "readonly":
			return READONLY;
		case "wr":
		case "rw":
		case "any":
			return ANY;
		default:
			SystemException.throwException(2342312, type + " is not correct db type");
			return null;
		}
	}

	private boolean writable;
	private boolean readable;

	private DBType(boolean writable, boolean readable) {
		this.writable = writable;
		this.readable = readable;
	}

	public boolean isWritable() {
		return writable;
	}

	public boolean isReadable() {
		return readable;
	}

}
