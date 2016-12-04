package org.yx.db;

import java.io.IOException;
import java.sql.SQLException;

public interface DBAction {
	void commit() throws IOException, SQLException;

	void rollback() throws IOException, SQLException;
}
