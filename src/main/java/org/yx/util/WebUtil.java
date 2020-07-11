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
import org.yx.http.handler.MultipartItem;
import org.yx.http.handler.MultipartHolder;
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

	/**
	 * 如果是文件上传的接口，用这个方法获取上传项
	 * 
	 * @return 除了参数外所有的multipart
	 */
	public static List<MultipartItem> getMultiParts() {
		return MultipartHolder.get();
	}

	/**
	 * 根据name获取对应的MultipartItem
	 * 
	 * @param name
	 *            MultipartItem对应的名称，注意该名称是part的名称，而不是文件名
	 * @return 对应的MultipartItem，如果不存在就返回null
	 */
	public static MultipartItem getPart(String name) {
		List<MultipartItem> list = MultipartHolder.get();
		if (list == null || list.isEmpty()) {
			return null;
		}
		for (MultipartItem p : list) {
			if (name.equals(p.getName())) {
				return p;
			}
		}
		return null;
	}

	public static HttpKit getKit() {
		return InnerHttpUtil.getKit();
	}

	public static void setKit(HttpKit kit) {
		InnerHttpUtil.setKit(kit);
	}
}
