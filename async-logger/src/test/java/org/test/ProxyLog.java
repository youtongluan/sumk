package org.test;

import org.slf4j.spi.LocationAwareLogger;
import org.yx.log.Log;

public class ProxyLog {

	private static LocationAwareLogger log=(LocationAwareLogger)Log.get("proxy-log");
	private static final String FQCN=ProxyLog.class.getName();
	public static void log(String msg,Object... params) {
		log.log(null, FQCN, LocationAwareLogger.INFO_INT, msg, params, null);
	}
	
	public static void error(String msg,Throwable t) {
		log.log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, null, t);
	}
}
