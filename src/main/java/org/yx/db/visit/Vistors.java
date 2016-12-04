package org.yx.db.visit;

public class Vistors {
	static ResultHandler pojoHandler;
	static ResultHandler pojoListHandler;

	public static ResultHandler pojoHandler() {
		return pojoHandler;
	}

	public static ResultHandler pojoListHandler() {
		return pojoListHandler;
	}
}
