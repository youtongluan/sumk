package org.yx.log;

import org.apache.ibatis.logging.LogFactory;
import org.slf4j.Logger;

public class MybatisLog implements org.apache.ibatis.logging.Log {

	private Logger log;

	public MybatisLog(String clazz) {
		log = Log.get(clazz);
	}

	@Override
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	@Override
	public boolean isTraceEnabled() {
		return log.isTraceEnabled();
	}

	@Override
	public void error(String s, Throwable e) {
		log.error(s, e);
	}

	@Override
	public void error(String s) {
		log.error(s);
	}

	@Override
	public void debug(String s) {
		log.debug(s);
	}

	@Override
	public void trace(String s) {
		log.trace(s);
	}

	@Override
	public void warn(String s) {
		log.info(s);
	}

	public static void enableMybatisLog() {
		LogFactory.useCustomLogging(MybatisLog.class);
	}

}
