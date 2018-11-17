/**
 * Copyright (C) 2016 - 2030 youtongluan.
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
package org.yx.util;

import javax.servlet.http.HttpServletRequest;

import org.yx.http.HttpHeadersHolder;
import org.yx.http.HttpSessionHolder;
import org.yx.http.filter.SessionObject;

public final class HttpUtil {
	/**
	 * 从session中获取对象
	 * 
	 * @param clz
	 *            session中对象的class类型
	 * @return session中存储的对象
	 */
	public static <T extends SessionObject> T getUserObject(Class<T> clz) {
		return HttpSessionHolder.getUserObject(clz);
	}

	/**
	 * 设置session中的对象，如果原来已经存在，会覆盖原来的
	 * 
	 * @param sessionObj
	 *            要设置的session对象
	 */
	public static void setUserObject(SessionObject sessionObj) {
		HttpSessionHolder.updateUserObject(sessionObj);
	}

	/**
	 * 移除session内容
	 */
	public static void removeUserObject() {
		HttpSessionHolder.remove();
	}

	/**
	 * 获取http请求中的header头部 <BR>
	 * <B>注意：不能在自定义的servlet中调用</B>
	 * 
	 * @param name
	 *            header头部的名字
	 * @return header中相应的value
	 */
	public static String getHeader(String name) {
		return HttpHeadersHolder.getHeader(name);
	}

	/**
	 * 获取http请求中的HttpServletRequest对象 <BR>
	 * <B>注意：不能在自定义的servlet中调用</B>
	 * 
	 * @return HttpServletRequest对象
	 */
	public static HttpServletRequest getHttpRequest() {
		return HttpHeadersHolder.getHttpRequest();
	}

	/**
	 * 获取sessionId <BR>
	 * <B>注意：不能在自定义的servlet中调用</B>
	 * 
	 * @return sessionId
	 */
	public static String sessionId() {
		return HttpHeadersHolder.sessionId();
	}
}
