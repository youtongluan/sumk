package org.yx.sumk.batis;

public interface DBResourceFactory {

	DBResource create(String dbName) throws Exception;
}
