package org.yx.sumk.batis;

import java.io.InputStream;
import java.util.Map;

public interface DBResource {
	InputStream dbIni() throws Exception;

	Map<String, InputStream> sqlXmls() throws Exception;
}
