package org.yx.common;

import java.lang.reflect.Method;

import org.yx.db.DBSessionContext;

public abstract class BizExcutor {

	public static Object exec(Method m, Object obj, Object[] params) throws Exception {
		try {
			return m.invoke(obj, params);
		} catch (Exception e) {
			DBSessionContext.clossLeakSession();
			throw e;
		}
	}

}
