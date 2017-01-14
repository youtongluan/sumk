/**
 * Copyright (C) 2016 - 2017 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.http.start;

import org.yx.bean.IOC;
import org.yx.exception.BizException;
import org.yx.http.ErrorCode;
import org.yx.http.filter.LoginServlet;
import org.yx.http.filter.UserSession;
import org.yx.log.Log;

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
	 * 如果没有登陆，会抛出异常，而不是提示登录
	 * 
	 * @return
	 */
	public static UserSession loadUserSession() {
		if (session == null) {
			LoginServlet serv = IOC.get(LoginServlet.class);
			session = serv.userSession();
		}
		if (session == null) {
			Log.get("session").info("session has not created");
			BizException.throwException(ErrorCode.SESSION_ERROR, "请重新登陆.");
		}
		return session;
	}

	/**
	 * 获取session中的用户信息
	 * 
	 * @return
	 */
	public static Object getUserObject(Class<?> clz) {
		return loadUserSession().getUserObject(clz);
	}

	/**
	 * 移除session中的用户信息
	 */
	public static void remove() {
		userSession();
		if (session == null) {
			Log.get("session").debug("has removed");
			return;
		}
		session.removeSession();
	}

	/**
	 * 用新的对象更新session中的用户信息
	 * 
	 * @param sessionObj
	 */
	public static void updateUserObject(Object sessionObj) {
		loadUserSession().updateSession(sessionObj);
	}

}
