package org.test.web.demo;

import org.yx.http.filter.AbstractSessionFilter;
import org.yx.http.filter.LoginObject;

public class MyLoginServlet extends AbstractSessionFilter {
	private static final long serialVersionUID = 1345345345L;


	protected LoginObject login(String user, String password, String validCode) {
		if (!"9999".equals(validCode)) {
			return LoginObject.error("验证码错误");
		}
		if ("admin".equals(user) && "123456".equals(password)) {
			return LoginObject.success(null);
		}
		return LoginObject.error("用户名或密码错误");
	}
}
