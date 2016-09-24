package org.yx.db.exec;

public class ResultContainer {
	Object result;
	private Object param;
	private String db;

	public static ResultContainer create(String db) {
		return new ResultContainer(db, null);
	}

	public static ResultContainer create(String db, Object param) {
		return new ResultContainer(db, param);
	}

	public ResultContainer(String db, Object param) {
		super();
		this.db = db;
		this.param = param;
	}

	public String getDb() {
		return db;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

	public Object getResult() {
		return result;
	}

}
