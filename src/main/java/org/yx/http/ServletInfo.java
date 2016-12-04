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
