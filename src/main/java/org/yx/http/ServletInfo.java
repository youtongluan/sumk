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
package org.yx.http;

import javax.servlet.Servlet;

public class ServletInfo {
	private String path;
	private Class<? extends Servlet> servletClz;

	public String getPath() {
		return path;
	}

	public ServletInfo setPath(String path) {
		this.path = path;
		return this;
	}

	public Class<? extends Servlet> getServletClz() {
		return servletClz;
	}

	public ServletInfo setServletClz(Class<? extends Servlet> servletClz) {
		this.servletClz = servletClz;
		return this;
	}

}
