package org.yx.http.start;

import org.yx.bean.IOC;
import org.yx.http.filter.LoginServlet;
import org.yx.http.filter.UserSession;

public class UserSessionHolder {
	static UserSession session;

	public static UserSession userSession() {
		if (session == null) {
			LoginServlet serv = IOC.get(LoginServlet.class);
			session = serv.userSession();
		}
		return session;
	}

	/**
	 * 获取session中的用户信息
	 * 
	 * @return
	 */
	public static Object getUserObject(Class<?> clz) {
		return userSession().getUserObject(clz);
	}

	/**
	 * 移除session中的用户信息
	 */
	public static void remove() {
		userSession().removeSession();
	}

	/**
	 * 用新的对象更新session中的用户信息
	 * 
	 * @param sessionObj
	 */
	public static void updateUserObject(Object sessionObj) {
		userSession().updateSession(sessionObj);
	}

}
