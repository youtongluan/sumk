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

import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.http.HttpContextHolder;
import org.yx.http.handler.UploadFile;
import org.yx.http.handler.UploadFileHolder;
import org.yx.http.kit.HttpKit;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.http.user.SessionObject;
import org.yx.http.user.WebSessions;

public final class WebUtil {

	public static <T extends SessionObject> T getUserObject(Class<T> clz) {
		return WebSessions.getUserObject(clz);
	}

	/**
	 * 移除session内容
	 */
	public static void removeUserObject() {
		WebSessions.remove();
	}

	public static String getHeader(String name) {
		return HttpContextHolder.getHeader(name);
	}

	/**
	 * 获取http请求中的HttpServletRequest对象 <BR>
	 * <B>注意：不能在自定义的servlet中调用，否则为null</B>
	 * 
	 * @return HttpServletRequest对象
	 */
	public static HttpServletRequest getHttpRequest() {
		return HttpContextHolder.getHttpRequest();
	}

	/**
	 * 获取http请求中的HttpServletResponse对象 <BR>
	 * <B>注意：不能在自定义的servlet中调用，否则为null</B>
	 * 
	 * @return HttpServletResponse对象
	 */
	public static HttpServletResponse getHttpResponse() {
		return HttpContextHolder.getHttpResponse();
	}

	/**
	 * 获取sessionId <BR>
	 * <B>注意：不能在自定义的servlet中调用</B>
	 * 
	 * @return sessionId
	 */
	public static String sessionId() {
		return HttpContextHolder.sessionId();
	}

	public static Charset charset() {
		return InnerHttpUtil.charset(getHttpRequest());
	}

	public static String getUserId() {
		SessionObject obj = getUserObject(SessionObject.class);
		if (obj == null) {
			return null;
		}
		return obj.getUserId();
	}

	public static List<UploadFile> getUploadFiles() {
		return UploadFileHolder.getFiles();
	}

	public static HttpKit getKit() {
		return InnerHttpUtil.getKit();
	}

	public static void setKit(HttpKit kit) {
		InnerHttpUtil.setKit(kit);
	}
}
