package org.yx.http.filter;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginServlet {
	UserSession userSession();

	void init(ServletConfig config);

	void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

}
