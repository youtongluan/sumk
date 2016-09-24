package org.yx.db;

import java.io.IOException;

public interface DBAction {
	void commit() throws IOException;

	void rollback() throws IOException;
}
