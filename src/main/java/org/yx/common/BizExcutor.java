package org.yx.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.yx.db.conn.ConnectionPool;

public abstract class BizExcutor {

	public static Object exec(Method m, Object obj, Object[] params) throws Throwable {
		try {
			return m.invoke(obj, params);
		} catch (Exception e) {
			ConnectionPool.clossLeakConnection();

			if (InvocationTargetException.class.isInstance(e)) {
				InvocationTargetException te = (InvocationTargetException) e;
				if (te.getTargetException() != null) {
					throw te.getTargetException();
				}
			}
			throw e;
		}
	}

}
