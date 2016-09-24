package org.test.web.demo;

import javax.servlet.http.HttpServletRequest;

import org.yx.http.Login;
import org.yx.http.filter.AbstractSessionFilter;
import org.yx.http.filter.LoginObject;

@Login(dbName = "test")
public class MyLoginServlet extends AbstractSessionFilter {

	@Override
	protected LoginObject login(String token, String user, HttpServletRequest req) {

		String password = req.getParameter("password");
		String validCode = req.getParameter("code");
		if (!"9999".equals(validCode)) {
			return LoginObject.error("验证码错误");
		}
		if ("admin".equals(user) && "123456".equals(password)) {
			this.userSession().setSession(token, "admin");
			return LoginObject.success(null);
		}

		return LoginObject.error("用户名或密码错误");
	}

}
